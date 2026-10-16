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
                            displayName = dto.display_name,
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
            if (response.isSuccessful && response.body()?.display_name != null) {
                Result.success(response.body()!!.display_name!!)
            } else {
                Result.success("Location near (%.4f, %.4f)".format(lat, lon))
            }
        } catch (e: Exception) {
            Result.success("Location near (%.4f, %.4f)".format(lat, lon))
        }
    }
}