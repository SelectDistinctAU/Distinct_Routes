package com.distinctroutes.app.domain.usecase

import com.distinctroutes.app.data.api.NominatimApiService
import javax.inject.Inject

data class AddressLocation(
    val displayName: String,
    val lat: Double,
    val lon: Double
)

class GeocodingUseCase @Inject constructor(
    private val nominatimApiService: NominatimApiService
) {
    suspend fun searchAddress(query: String, cityContext: String? = null): Result<List<AddressLocation>> {
        return try {
            val viewbox = if (!cityContext.isNullOrBlank()) getCityViewbox(cityContext) else null
            val scopedQuery = if (!cityContext.isNullOrBlank() && !query.contains(cityContext, ignoreCase = true)) {
                "$query, $cityContext"
            } else {
                query
            }

            val response = nominatimApiService.searchAddress(
                query = scopedQuery,
                viewbox = viewbox,
                bounded = if (viewbox != null) 1 else null
            )
            if (response.isSuccessful && response.body() != null) {
                val locations = response.body()!!.mapNotNull { dto ->
                    val lat = dto.lat.toDoubleOrNull()
                    val lon = dto.lon.toDoubleOrNull()
                    if (lat != null && lon != null) {
                        // Extract house number if user entered one in query
                        val formatted = formatShortAddress(dto.display_name)
                        val houseNumMatch = Regex("^\\d{1,6}[a-zA-Z]?\\b").find(query.trim())
                        val finalDisplayName = if (houseNumMatch != null && !formatted.startsWith(houseNumMatch.value)) {
                            "${houseNumMatch.value} $formatted"
                        } else {
                            formatted
                        }

                        AddressLocation(
                            displayName = finalDisplayName,
                            lat = lat,
                            lon = lon
                        )
                    } else null
                }
                Result.success(locations)
            } else {
                Result.failure(Exception("Address search failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reverseGeocode(lat: Double, lon: Double): Result<String> {
        return try {
            val response = nominatimApiService.reverseGeocode(lat = lat, lon = lon)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val addrMap = dto.address
                if (addrMap != null) {
                    val featureName = addrMap["amenity"] ?: addrMap["building"] ?: addrMap["shop"] ?: addrMap["historic"]
                    val road = addrMap["road"] ?: addrMap["pedestrian"] ?: addrMap["footway"] ?: addrMap["cycleway"]
                    val suburb = addrMap["suburb"] ?: addrMap["neighbourhood"] ?: addrMap["town"] ?: addrMap["city"]
                    val houseNumber = addrMap["house_number"]

                    val constructed = buildList {
                        if (featureName != null) add(featureName)
                        if (road != null) {
                            if (houseNumber != null) add("$houseNumber $road") else add(road)
                        }
                        if (suburb != null && suburb != featureName) add(suburb)
                    }.filter { it.isNotBlank() }.distinct()

                    if (constructed.isNotEmpty()) {
                        return Result.success(constructed.joinToString(", "))
                    }
                }
                if (!dto.display_name.isNullOrBlank()) {
                    return Result.success(formatShortAddress(dto.display_name))
                }
            }
            Result.success("Map Pin Location (%.4f, %.4f)".format(lat, lon))
        } catch (e: Exception) {
            Result.success("Map Pin Location (%.4f, %.4f)".format(lat, lon))
        }
    }

    fun getCityViewbox(city: String): String? {
        return when (city.lowercase().trim()) {
            "perth" -> "115.5,-32.4,116.2,-31.5"
            "berlin" -> "13.0,52.3,13.8,52.7"
            "canberra" -> "149.0,-35.5,149.3,-35.1"
            "sydney" -> "150.8,-34.1,151.4,-33.5"
            "melbourne" -> "144.7,-38.1,145.4,-37.5"
            "brisbane" -> "152.8,-27.7,153.3,-27.2"
            "adelaide" -> "138.4,-35.1,138.8,-34.7"
            "london" -> "-0.5,51.2,0.3,51.7"
            "paris" -> "2.1,48.7,2.5,49.0"
            "rome" -> "12.3,41.7,12.7,42.0"
            "madrid" -> "-3.9,40.3,-3.5,40.6"
            else -> null
        }
    }

    fun formatShortAddress(fullAddress: String): String {
        if (fullAddress.isBlank()) return fullAddress
        val parts = fullAddress.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        if (parts.isEmpty()) return fullAddress

        // Extract up to first 3 significant place components, combining house numbers with street names
        val shortParts = mutableListOf<String>()
        var i = 0
        while (i < parts.size) {
            val part = parts[i]
            if (part.matches(Regex("^\\d{1,6}[a-zA-Z]?$")) && i + 1 < parts.size && !parts[i + 1].matches(Regex("^\\d{4,6}$"))) {
                shortParts.add("$part ${parts[i + 1]}")
                i += 2
                continue
            }
            if (part.matches(Regex("^\\d{4,6}$")) ||
                part.equals("Australia", true) ||
                part.equals("Deutschland", true) ||
                part.equals("United Kingdom", true) ||
                part.equals("Germany", true)
            ) {
                i++
                continue
            }
            shortParts.add(part)
            i++
            if (shortParts.size >= 3) break
        }

        return if (shortParts.isNotEmpty()) shortParts.joinToString(", ") else parts.take(2).joinToString(", ")
    }
}