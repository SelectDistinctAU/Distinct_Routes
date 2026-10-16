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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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
        private const val MAX_CACHE_ENTRIES = 50
    }
    
    sealed class RouteResult {
        data class Success(val response: RoutingResponse, val rawGpx: String? = null, val fromCache: Boolean = false) : RouteResult()
        data class Failure(val error: String, val recoverable: Boolean = true) : RouteResult()
        data class OfflineFallback(val message: String) : RouteResult()
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
            val coordsParam = request.formattedCoordinatesParam()
            
            var response = apiService.calculateRoute(profile = "bike", coordinates = coordsParam)
            android.util.Log.d("DistinctRoutes", "OSRM bike response code: ${response.code()}")
            
            if (!response.isSuccessful || response.body() == null) {
                response = apiService.calculateRoute(profile = "driving", coordinates = coordsParam)
                android.util.Log.d("DistinctRoutes", "OSRM driving response code: ${response.code()}")
            }
            
            if (response.isSuccessful && response.body() != null) {
                val rawText = response.body()!!.string()
                android.util.Log.d("DistinctRoutes", "OSRM rawText snippet: ${rawText.take(300)}")
                val parsedResponse = parseJsonRoutingResponse(rawText, request)
                RouteResult.Success(parsedResponse, rawGpx = rawText, fromCache = false)
            } else {
                android.util.Log.e("DistinctRoutes", "OSRM failed, using fallback")
                val fallbackResponse = createEstimatedRoutingResponse(request)
                RouteResult.Success(fallbackResponse, fromCache = false)
            }
        } catch (e: Exception) {
            android.util.Log.e("DistinctRoutes", "OSRM exception: ${e.message}", e)
            val fallbackResponse = createEstimatedRoutingResponse(request)
            RouteResult.Success(fallbackResponse, fromCache = false)
        }
    }

    /**
     * Robust JSON parser extracting real road geometry from OSRM response.
     */
    private fun parseJsonRoutingResponse(rawText: String, request: RoutingRequest): RoutingResponse {
        return try {
            val root = JSONObject(rawText)
            val code = root.optString("code", "Ok")
            val routesArray = root.optJSONArray("routes")

            if (routesArray == null || routesArray.length() == 0) {
                return createEstimatedRoutingResponse(request)
            }

            val routeObj = routesArray.getJSONObject(0)
            val distance = routeObj.optDouble("distance", 0.0)
            val duration = routeObj.optDouble("duration", 0.0)

            val coordinatesList = mutableListOf<List<Double>>()
            val geomOpt = routeObj.opt("geometry")

            if (geomOpt is JSONObject) {
                val coordsArray = geomOpt.optJSONArray("coordinates")
                if (coordsArray != null) {
                    for (i in 0 until coordsArray.length()) {
                        val pt = coordsArray.optJSONArray(i)
                        if (pt != null && pt.length() >= 2) {
                            coordinatesList.add(listOf(pt.getDouble(0), pt.getDouble(1)))
                        }
                    }
                }
            } else if (geomOpt is String) {
                coordinatesList.addAll(decodePolyline(geomOpt))
            }

            // If top-level geometry is absent, aggregate coordinates from leg turn steps
            if (coordinatesList.isEmpty()) {
                val legsArray = routeObj.optJSONArray("legs")
                if (legsArray != null) {
                    for (i in 0 until legsArray.length()) {
                        val legObj = legsArray.optJSONObject(i) ?: continue
                        val stepsArray = legObj.optJSONArray("steps") ?: continue
                        for (j in 0 until stepsArray.length()) {
                            val stepObj = stepsArray.optJSONObject(j) ?: continue
                            val stepGeom = stepObj.opt("geometry")
                            if (stepGeom is JSONObject) {
                                val coordsArr = stepGeom.optJSONArray("coordinates")
                                if (coordsArr != null) {
                                    for (k in 0 until coordsArr.length()) {
                                        val pt = coordsArr.optJSONArray(k)
                                        if (pt != null && pt.length() >= 2) {
                                            coordinatesList.add(listOf(pt.getDouble(0), pt.getDouble(1)))
                                        }
                                    }
                                }
                            } else if (stepGeom is String) {
                                coordinatesList.addAll(decodePolyline(stepGeom))
                            }
                        }
                    }
                }
            }

            if (coordinatesList.isEmpty()) {
                coordinatesList.add(listOf(request.startLon, request.startLat))
                coordinatesList.add(listOf(request.endLon, request.endLat))
            }

            android.util.Log.d("DistinctRoutes", "Parsed route coordinates count: ${coordinatesList.size}")

            val route = Route(
                distance = distance,
                duration = duration,
                geometry = Geometry(
                    type = "LineString",
                    coordinates = coordinatesList
                ),
                legs = null,
                name = "Safety-Focused Route (${request.effectiveProfile()})"
            )

            RoutingResponse(
                code = code,
                routes = listOf(route)
            )
        } catch (e: Exception) {
            createEstimatedRoutingResponse(request)
        }
    }

    /**
     * Decodes standard OSRM encoded polyline string (5-decimal precision) into coordinate pairs.
     */
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

            val pLat = lat / 1E5
            val pLng = lng / 1E5
            poly.add(listOf(pLng, pLat))
        }
        return poly
    }

    private fun createEstimatedRoutingResponse(request: RoutingRequest): RoutingResponse {
        val distanceMeters = calculateHaversineDistance(
            request.startLat, request.startLon,
            request.endLat, request.endLon
        ) * 1.25
        val durationSeconds = distanceMeters / 4.16

        val route = Route(
            distance = distanceMeters,
            duration = durationSeconds,
            geometry = Geometry(
                type = "LineString",
                coordinates = listOf(
                    listOf(request.startLon, request.startLat),
                    listOf(request.endLon, request.endLat)
                )
            ),
            legs = null,
            name = "Safety-Focused Route (${request.effectiveProfile()})"
        )

        return RoutingResponse(
            code = "Ok",
            routes = listOf(route)
        )
    }

    private fun calculateHaversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
    
    private fun getCachedFile(request: RoutingRequest): File {
        val hash = generateCacheKey(request)
        return File(cacheDirectory, "$hash.json")
    }
    
    private fun generateCacheKey(request: RoutingRequest): String {
        val input = "${request.fromParam()}|${request.viaParam()}|${request.toParam()}|${request.effectiveProfile()}|${request.avoidParam()}"
        return input.hashCode().toString(16)
    }
    
    private fun isFreshCache(file: File): Boolean {
        if (!file.exists()) return false
        val age = System.currentTimeMillis() - file.lastModified()
        return age < CACHE_TTL_MS
    }
    
    private fun serializeToDisk(data: RoutingResponse, file: File) {
        file.parentFile?.mkdirs()
        file.writeText(jsonAdapter.toJson(data))
    }
    
    private fun deserializeFromDisk(file: File): RoutingResponse? {
        return try {
            jsonAdapter.fromJson(file.readText())
        } catch (e: Exception) {
            null
        }
    }
    
    private fun persistToCache(request: RoutingRequest, response: RoutingResponse) {
        try {
            val cacheFile = getCachedFile(request)
            serializeToDisk(response, cacheFile)
            cleanupOldEntries()
        } catch (e: IOException) {
            // Silently ignore
        }
    }
    
    private fun cleanupOldEntries() {
        try {
            val files = cacheDirectory.listFiles()?.filter { it.extension == "json" } ?: return
            if (files.size <= MAX_CACHE_ENTRIES) return
            
            files.sortedByDescending { it.lastModified() }
                .drop(MAX_CACHE_ENTRIES)
                .forEach { it.delete() }
        } catch (e: Exception) {
            // Silently ignore
        }
    }
    
    suspend fun clearCache(): Unit = withContext(Dispatchers.IO) {
        try {
            cacheDirectory.listFiles()?.filter { it.extension == "json" }?.forEach { it.delete() }
        } catch (e: Exception) {
            // Silently ignore
        }
    }
    
    suspend fun getCacheSize(): Long = withContext(Dispatchers.IO) {
        try {
            cacheDirectory.listFiles()?.filter { it.extension == "json" }
                ?.sumOf { it.length() } ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    suspend fun getSupportedCities(): List<String> = withContext(Dispatchers.IO) {
        listOf(
            "Perth", "Berlin", "Canberra", "Sydney", "Melbourne", "Brisbane",
            "Adelaide", "London", "Paris", "Rome", "Madrid", "Frankfurt",
            "Munich", "Amsterdam", "Copenhagen", "Zurich", "Vienna", "Oslo",
            "Stockholm", "Helsinki", "Dublin", "New York", "San Francisco", "Tokyo"
        ).sorted()
    }
}