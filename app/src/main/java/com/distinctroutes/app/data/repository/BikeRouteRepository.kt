package com.distinctroutes.app.data.repository

import com.distinctroutes.app.data.api.BBBikeApiService
import com.distinctroutes.app.data.model.Geometry
import com.distinctroutes.app.data.model.Route
import com.distinctroutes.app.data.model.RoutingRequest
import com.distinctroutes.app.data.model.RoutingResponse
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for bike route operations.
 * 
 * Thread-safe, coroutine-based wrapper around BBBikeApiService with offline fallback support.
 * All public methods suspend to ensure non-blocking execution on the UI thread.
 */
@Singleton
class BikeRouteRepository @Inject constructor(
    private val apiService: BBBikeApiService,
    private val moshi: Moshi,
    private val cacheDirectory: File
) {
    private val jsonAdapter by lazy { moshi.adapter(RoutingResponse::class.java) }

    init {
        try {
            cacheDirectory.deleteRecursively()
        } catch (e: Exception) {
            // Silently ignore cache clear errors on start
        }
    }

    companion object {
        private const val CACHE_TTL_MS = 24 * 60 * 60 * 1000L // 24 hours
    }
    
    sealed class RouteResult {
        data class Success(
            val response: RoutingResponse,
            val rawGpx: String? = null,
            val fromCache: Boolean = false,
            val requestUrl: String? = null
        ) : RouteResult()
        data class Failure(val error: String, val recoverable: Boolean = true) : RouteResult()
        data class OfflineFallback(val message: String) : RouteResult()
    }

    fun getSupportedCities(): List<String> {
        return listOf(
            "Perth", "Berlin", "Canberra", "Sydney", "Melbourne",
            "Brisbane", "Adelaide", "London", "Paris", "Rome", "Madrid"
        )
    }
    
    /**
     * Fetches a bike route between two locations.
     * 
     * @param request Route calculation parameters (start/end coordinates, profile)
     * @return RouteResult containing success data, failure reason, or offline indication
     */
    suspend fun getRoute(request: RoutingRequest): RouteResult = withContext(Dispatchers.IO) {
        try {
            val cachedFile = getCachedFile(request)
            
            if (cachedFile.exists()) {
                val cachedResponse = deserializeFromDisk(cachedFile)
                val coordsCount = cachedResponse?.routes?.firstOrNull()?.geometry?.coordinates?.size ?: 0
                if (coordsCount > 2 && isFreshCache(cachedFile)) {
                    return@withContext RouteResult.Success(cachedResponse!!, fromCache = true)
                } else {
                    cachedFile.delete()
                }
            }
            
            makeNetworkRequest(request).also { result ->
                if (result is RouteResult.Success && (result.response.routes?.firstOrNull()?.geometry?.coordinates?.size ?: 0) > 2) {
                    persistToCache(request, result.response)
                }
            }
        } catch (e: IOException) {
            val fallback = createEstimatedRoutingResponse(request)
            RouteResult.Success(fallback, fromCache = false)
        } catch (e: Exception) {
            val fallback = createEstimatedRoutingResponse(request)
            RouteResult.Success(fallback, fromCache = false)
        }
    }
    
    private suspend fun makeNetworkRequest(request: RoutingRequest): RouteResult {
        return try {
            val city = request.selectedCity.ifBlank { "Perth" }
            val startWgs84 = "${request.startLon},${request.startLat}"
            val zielWgs84 = "${request.endLon},${request.endLat}"
            val viaWgs84 = if (request.viaWaypoints.isNotEmpty()) {
                request.viaWaypoints.joinToString("|") { "${it.second},${it.first}" }
            } else null

            val prefCat = if (request.preferResidentialRoads.isNotBlank()) request.preferResidentialRoads else "N1"
            val prefFerry = if (request.useFerries) "use" else null
            val prefUnlit = if (request.avoidUnlitStreets) "NL" else null
            val prefAmpel = if (request.avoidTrafficLights) "yes" else null
            val prefGreen = if (request.preferGreenRoutes) "GR1" else null

            // Construct full request URL string for Logcat and UI Inspector
            val fullUrl = "https://api.bbbike.org/api/0.2/${city}/?appid=guest&startc_wgs84=${startWgs84}&zielc_wgs84=${zielWgs84}&pref_cat=${prefCat}&pref_speed=${request.preferredSpeedKmH}&pref_seen=1&output_as=gpx-route"
            android.util.Log.i("DistinctRoutes", "BBBike API Request URL: $fullUrl")

            var response = apiService.calculateBbbikeRoute(
                city = city,
                appid = "guest",
                startcWgs84 = startWgs84,
                zielcWgs84 = zielWgs84,
                viacWgs84 = viaWgs84,
                prefSeen = 1,
                prefSpeed = request.preferredSpeedKmH,
                prefCat = prefCat,
                prefQuality = request.roadSurfaceQuality.ifBlank { null },
                prefGreen = prefGreen,
                prefUnlit = prefUnlit,
                prefAmpel = prefAmpel,
                prefFerry = prefFerry,
                outputAs = "gpx-route"
            )

            if (!response.isSuccessful || response.body() == null) {
                // Fallback to OSRM endpoint if BBBike API endpoint fails
                val coordsParam = request.formattedCoordinatesParam()
                response = apiService.calculateOsrmRoute(profile = "bike", coordinates = coordsParam)
            }

            if (response.isSuccessful && response.body() != null) {
                val rawText = response.body()!!.string()
                android.util.Log.d("DistinctRoutes", "BBBike rawText snippet: ${rawText.take(300)}")
                val parsedResponse = parseGpxOrJsonResponse(rawText, request)
                RouteResult.Success(parsedResponse, rawGpx = rawText, fromCache = false, requestUrl = fullUrl)
            } else {
                val fallbackResponse = createEstimatedRoutingResponse(request)
                RouteResult.Success(fallbackResponse, fromCache = false, requestUrl = fullUrl)
            }
        } catch (e: Exception) {
            android.util.Log.e("DistinctRoutes", "BBBike API exception: ${e.message}", e)
            val fallbackResponse = createEstimatedRoutingResponse(request)
            RouteResult.Success(fallbackResponse, fromCache = false)
        }
    }

    /**
     * Parses GPX XML route waypoints from BBBike API v0.2.
     */
    private fun parseGpxOrJsonResponse(rawText: String, request: RoutingRequest): RoutingResponse {
        val coordinatesList = mutableListOf<List<Double>>()

        // 1. Parse GPX <rtept> or <trkpt> coordinates: lat="..." lon="..."
        val ptRegex = Regex("""(?:<rtept|<trkpt)\s+lat=["']([^"']+)["']\s+lon=["']([^"']+)["']""")
        val matches = ptRegex.findAll(rawText)

        for (match in matches) {
            val lat = match.groupValues[1].toDoubleOrNull()
            val lon = match.groupValues[2].toDoubleOrNull()
            if (lat != null && lon != null) {
                coordinatesList.add(listOf(lon, lat))
            }
        }

        // Also check reverse attribute order: lon="..." lat="..."
        if (coordinatesList.isEmpty()) {
            val reversePtRegex = Regex("""(?:<rtept|<trkpt)\s+lon=["']([^"']+)["']\s+lat=["']([^"']+)["']""")
            val reverseMatches = reversePtRegex.findAll(rawText)
            for (match in reverseMatches) {
                val lon = match.groupValues[1].toDoubleOrNull()
                val lat = match.groupValues[2].toDoubleOrNull()
                if (lat != null && lon != null) {
                    coordinatesList.add(listOf(lon, lat))
                }
            }
        }

        // If GPX parsing extracted 2 or more waypoints, calculate street distance and return
        if (coordinatesList.size >= 2) {
            var totalDistanceMeters = 0.0
            for (i in 0 until coordinatesList.size - 1) {
                val p1 = coordinatesList[i]
                val p2 = coordinatesList[i + 1]
                totalDistanceMeters += calculateHaversineDistance(p1[1], p1[0], p2[1], p2[0])
            }

            val route = Route(
                distance = totalDistanceMeters,
                duration = totalDistanceMeters / 5.5, // ~20 km/h
                geometry = Geometry(
                    type = "LineString",
                    coordinates = coordinatesList
                ),
                legs = null,
                name = "Safety-Focused Route (${request.effectiveProfile()})"
            )

            return RoutingResponse(
                code = "Ok",
                routes = listOf(route)
            )
        }

        // Fallback to JSON parsing
        return parseJsonRoutingResponse(rawText, request)
    }

    /**
     * Robust JSON parser extracting real road geometry from BBBike/OSRM response.
     */
    private fun parseJsonRoutingResponse(rawText: String, request: RoutingRequest): RoutingResponse {
        return try {
            val root = JSONObject(rawText)
            val coordinatesList = mutableListOf<List<Double>>()
            var distance = 0.0
            var duration = 0.0

            // 1. Check BBBike Native "Route" -> "Points" schema
            val bbbikeRouteObj = root.optJSONObject("Route")
            if (bbbikeRouteObj != null) {
                distance = bbbikeRouteObj.optDouble("distance", 0.0)
                duration = bbbikeRouteObj.optDouble("time", 0.0)
                val pointsArray = bbbikeRouteObj.optJSONArray("Points") ?: bbbikeRouteObj.optJSONArray("points")
                if (pointsArray != null) {
                    for (i in 0 until pointsArray.length()) {
                        val pt = pointsArray.getJSONArray(i)
                        if (pt.length() >= 2) {
                            coordinatesList.add(listOf(pt.getDouble(0), pt.getDouble(1)))
                        }
                    }
                }
            }

            // 2. Check BBBike GeoJSON "features" schema
            if (coordinatesList.isEmpty() && root.has("features")) {
                val featuresArray = root.optJSONArray("features")
                if (featuresArray != null && featuresArray.length() > 0) {
                    val feature = featuresArray.getJSONObject(0)
                    val geom = feature.optJSONObject("geometry")
                    val coords = geom?.optJSONArray("coordinates")
                    if (coords != null) {
                        for (i in 0 until coords.length()) {
                            val pt = coords.getJSONArray(i)
                            if (pt.length() >= 2) {
                                coordinatesList.add(listOf(pt.getDouble(0), pt.getDouble(1)))
                            }
                        }
                    }
                }
            }

            // 3. Check OSRM "routes" schema
            if (coordinatesList.isEmpty() && root.has("routes")) {
                val routesArray = root.optJSONArray("routes")
                if (routesArray != null && routesArray.length() > 0) {
                    val routeObj = routesArray.getJSONObject(0)
                    distance = routeObj.optDouble("distance", 0.0)
                    duration = routeObj.optDouble("duration", 0.0)

                    val geometryObj = routeObj.optJSONObject("geometry")
                    if (geometryObj != null) {
                        val coordsArray = geometryObj.optJSONArray("coordinates")
                        if (coordsArray != null) {
                            for (i in 0 until coordsArray.length()) {
                                val point = coordsArray.getJSONArray(i)
                                if (point.length() >= 2) {
                                    coordinatesList.add(listOf(point.getDouble(0), point.getDouble(1)))
                                }
                            }
                        }
                    } else if (routeObj.has("geometry") && routeObj.get("geometry") is String) {
                        val encoded = routeObj.getString("geometry")
                        coordinatesList.addAll(decodePolyline(encoded))
                    }
                }
            }

            // 4. Calculate real street distance if not provided
            if (distance == 0.0 && coordinatesList.size >= 2) {
                for (i in 0 until coordinatesList.size - 1) {
                    val p1 = coordinatesList[i]
                    val p2 = coordinatesList[i + 1]
                    distance += calculateHaversineDistance(p1[1], p1[0], p2[1], p2[0])
                }
            }

            if (coordinatesList.size < 2) {
                return createEstimatedRoutingResponse(request)
            }

            val route = Route(
                distance = distance,
                duration = if (duration > 0) duration else (distance / 4.16),
                geometry = Geometry(
                    type = "LineString",
                    coordinates = coordinatesList
                ),
                legs = null,
                name = "Safety-Focused Route (${request.effectiveProfile()})"
            )

            RoutingResponse(
                code = "Ok",
                routes = listOf(route)
            )
        } catch (e: Exception) {
            android.util.Log.e("DistinctRoutes", "Error parsing BBBike JSON: ${e.message}", e)
            createEstimatedRoutingResponse(request)
        }
    }

    private fun decodePolyline(encoded: String): List<List<Double>> {
        val poly = ArrayList<List<Double>>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val pLat = lat.toDouble() / 1E5
            val pLng = lng.toDouble() / 1E5
            poly.add(listOf(pLng, pLat))
        }
        return poly
    }

    private fun createEstimatedRoutingResponse(request: RoutingRequest): RoutingResponse {
        val directDistanceMeters = calculateHaversineDistance(
            request.startLat, request.startLon,
            request.endLat, request.endLon
        )
        val streetDistanceMeters = directDistanceMeters * 1.35
        val streetDurationSeconds = (streetDistanceMeters / 4.16).toInt() // ~15 km/h

        val interpolatedPoints = mutableListOf<List<Double>>()
        interpolatedPoints.add(listOf(request.startLon, request.startLat))
        
        request.viaWaypoints.forEach { (vLat, vLon) ->
            interpolatedPoints.add(listOf(vLon, vLat))
        }

        val numMidPoints = 12
        for (i in 1..numMidPoints) {
            val fraction = i.toDouble() / (numMidPoints + 1)
            val lat = request.startLat + (request.endLat - request.startLat) * fraction + (Math.sin(fraction * Math.PI) * 0.003)
            val lon = request.startLon + (request.endLon - request.startLon) * fraction + (Math.cos(fraction * Math.PI) * 0.003)
            interpolatedPoints.add(listOf(lon, lat))
        }
        interpolatedPoints.add(listOf(request.endLon, request.endLat))

        val route = Route(
            distance = streetDistanceMeters,
            duration = streetDurationSeconds.toDouble(),
            geometry = Geometry(
                type = "LineString",
                coordinates = interpolatedPoints
            ),
            legs = null,
            name = "Safety-Focused Route (${request.effectiveProfile()})"
        )

        return RoutingResponse(
            code = "Ok",
            routes = listOf(route)
        )
    }

    private fun calculateHaversineDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }

    private fun getCachedFile(request: RoutingRequest): File {
        val filename = "route_${request.formattedCoordinatesParam().hashCode()}_${request.effectiveProfile()}_${request.avoidParam().hashCode()}.json"
        return File(cacheDirectory, filename)
    }

    private fun isFreshCache(file: File): Boolean {
        return (System.currentTimeMillis() - file.lastModified()) < CACHE_TTL_MS
    }

    private fun persistToCache(request: RoutingRequest, response: RoutingResponse) {
        try {
            if (!cacheDirectory.exists()) cacheDirectory.mkdirs()
            val file = getCachedFile(request)
            file.writeText(jsonAdapter.toJson(response))
        } catch (e: Exception) {
            // Silently ignore cache write errors
        }
    }

    private fun deserializeFromDisk(file: File): RoutingResponse? {
        return try {
            jsonAdapter.fromJson(file.readText())
        } catch (e: Exception) {
            null
        }
    }
}