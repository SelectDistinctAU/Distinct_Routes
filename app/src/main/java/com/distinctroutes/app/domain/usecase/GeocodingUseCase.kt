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
    suspend fun searchAddress(query: String): Result<List<AddressLocation>> {
        return try {
            val response = nominatimApiService.searchAddress(query = query)
            if (response.isSuccessful && response.body() != null) {
                val locations = response.body()!!.mapNotNull { dto ->
                    val lat = dto.lat.toDoubleOrNull()
                    val lon = dto.lon.toDoubleOrNull()
                    if (lat != null && lon != null) {
                        AddressLocation(
                            displayName = formatShortAddress(dto.display_name),
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
            val name = response.body()?.display_name
            if (response.isSuccessful && !name.isNullOrBlank()) {
                Result.success(formatShortAddress(name))
            } else {
                Result.success("Location near (%.4f, %.4f)".format(lat, lon))
            }
        } catch (e: Exception) {
            Result.success("Location near (%.4f, %.4f)".format(lat, lon))
        }
    }

    fun formatShortAddress(fullAddress: String): String {
        if (fullAddress.isBlank()) return fullAddress
        val parts = fullAddress.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        if (parts.isEmpty()) return fullAddress

        // Extract up to first 3 significant place components, filtering postal codes and country names
        val shortParts = mutableListOf<String>()
        for (part in parts) {
            if (part.matches(Regex("^\\d{4,6}$")) ||
                part.equals("Australia", true) ||
                part.equals("Deutschland", true) ||
                part.equals("United Kingdom", true) ||
                part.equals("Germany", true)
            ) {
                continue
            }
            shortParts.add(part)
            if (shortParts.size >= 3) break
        }

        return if (shortParts.isNotEmpty()) shortParts.joinToString(", ") else parts.take(2).joinToString(", ")
    }
}