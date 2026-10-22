package com.distinctroutes.app.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service for BBBike official REST API v0.2.
 */
interface BBBikeApiService {

    /**
     * Calculates bike route using official BBBike REST API v0.2.
     * Endpoint: https://api.bbbike.org/api/0.2/{city}/
     */
    @GET("api/0.2/{city}/")
    suspend fun calculateBbbikeRoute(
        @Path("city") city: String,
        @Query("appid") appid: String = "guest",
        @Query("startc_wgs84") startcWgs84: String,
        @Query("zielc_wgs84") zielcWgs84: String,
        @Query("viac_wgs84") viacWgs84: String? = null,
        @Query("pref_seen") prefSeen: Int = 1,
        @Query("pref_speed") prefSpeed: Int = 20,
        @Query("pref_cat") prefCat: String? = "N1",
        @Query("pref_quality") prefQuality: String? = null,
        @Query("pref_green") prefGreen: String? = null,
        @Query("pref_unlit") prefUnlit: String? = null,
        @Query("pref_ampel") prefAmpel: String? = null,
        @Query("pref_ferry") prefFerry: String? = null,
        @Query("output_as") outputAs: String = "json"
    ): Response<ResponseBody>

    /**
     * Fallback OSRM route endpoint.
     */
    @GET("route/v1/{profile}/{coordinates}")
    suspend fun calculateOsrmRoute(
        @Path("profile") profile: String = "bike",
        @Path("coordinates", encoded = true) coordinates: String,
        @Query("overview") overview: String = "full",
        @Query("geometries") geometries: String = "geojson",
        @Query("steps") steps: Boolean = true
    ): Response<ResponseBody>
}