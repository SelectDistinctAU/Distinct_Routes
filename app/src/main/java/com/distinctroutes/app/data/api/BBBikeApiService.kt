package com.distinctroutes.app.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service for BBBike / OSRM routing endpoint.
 *
 * Provides access to OSRM biking route calculations returning real street geometry.
 */
interface BBBikeApiService {

    /**
     * Calculates real street-following bike route between coordinates using OSRM biking service.
     *
     * @param profile OSRM profile: "bike", "driving", "foot"
     * @param coordinates Format: "{startLon},{startLat};{endLon},{endLat}"
     */
    @GET("route/v1/{profile}/{coordinates}")
    suspend fun calculateRoute(
        @Path("profile") profile: String = "bike",
        @Path("coordinates", encoded = true) coordinates: String,
        @Query("overview") overview: String = "full",
        @Query("geometries") geometries: String = "geojson",
        @Query("steps") steps: Boolean = true
    ): Response<ResponseBody>

    /**
     * BBBike web API endpoint fallback.
     */
    @GET("bbbike/")
    suspend fun getRoute(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("profile") profile: String = "bike",
        @Query("language") language: String? = null,
        @Query("avoid") avoid: String? = null,
        @Query("output_format") outputFormat: String = "gpx"
    ): Response<ResponseBody>

    /**
     * Health check endpoint.
     */
    @GET("health")
    suspend fun checkHealth(): Response<Unit>
}