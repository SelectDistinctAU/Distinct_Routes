package com.distinctroutes.app.data.api

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Retrofit interface for OpenStreetMap Nominatim geocoding API.
 */
interface NominatimApiService {

    @GET("search")
    suspend fun searchAddress(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 5,
        @Header("User-Agent") userAgent: String = "DistinctRoutes/0.1.0 (Android)"
    ): Response<List<GeocodeResultDto>>

    @GET("reverse")
    suspend fun reverseGeocode(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("format") format: String = "json",
        @Header("User-Agent") userAgent: String = "DistinctRoutes/0.1.0 (Android)"
    ): Response<ReverseGeocodeDto>
}

@JsonClass(generateAdapter = true)
data class GeocodeResultDto(
    val place_id: Long,
    val display_name: String,
    val lat: String,
    val lon: String
)

@JsonClass(generateAdapter = true)
data class ReverseGeocodeDto(
    val place_id: Long?,
    val display_name: String?,
    val address: Map<String, String>?
)