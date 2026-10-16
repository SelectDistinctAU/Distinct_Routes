package com.distinctroutes.app.data.model

import com.squareup.moshi.JsonClass

/**
 * Request parameter configuration for BBBike route calculation.
 */
@JsonClass(generateAdapter = true)
data class RoutingRequest(
    val startLat: Double,
    val startLon: Double,
    val endLat: Double,
    val endLon: Double,
    val viaWaypoints: List<Pair<Double, Double>> = emptyList(),
    val profile: String = "bike",
    val language: String? = "en",
    val avoidTrafficLights: Boolean = false,
    val avoidHighways: Boolean = false,
    val avoidSteepHills: Boolean = false,
    val hasDogTrailer: Boolean = false,
    val bikeType: String = "bike"
) {
    fun fromParam(): String = "$startLat,$startLon"
    fun toParam(): String = "$endLat,$endLon"
    fun viaParam(): String = viaWaypoints.joinToString(";") { "${it.first},${it.second}" }

    /**
     * Formats coordinate parameter string for OSRM route API:
     * Multi-point: "{startLon},{startLat};{via1Lon},{via1Lat};...;{endLon},{endLat}"
     */
    fun formattedCoordinatesParam(): String {
        return if (viaWaypoints.isNotEmpty()) {
            val viaFormatted = viaWaypoints.joinToString(";") { "${it.second},${it.first}" }
            "$startLon,$startLat;$viaFormatted;$endLon,$endLat"
        } else {
            "$startLon,$startLat;$endLon,$endLat"
        }
    }

    fun avoidParam(): String? {
        val avoidList = mutableListOf<String>()
        if (avoidTrafficLights) avoidList.add("traffic_lights")
        if (avoidHighways) avoidList.add("highways")
        if (avoidSteepHills || hasDogTrailer) avoidList.add("hills")
        return if (avoidList.isNotEmpty()) avoidList.joinToString(",") else null
    }

    fun effectiveProfile(): String {
        return when {
            hasDogTrailer -> "cargo"
            bikeType.lowercase() in listOf("cargo", "tandem") -> "cargo"
            bikeType.lowercase() == "mtb" -> "mtb"
            bikeType.lowercase() == "handbike" -> "handbike"
            else -> profile
        }
    }
}