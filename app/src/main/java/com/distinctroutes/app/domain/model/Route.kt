package com.distinctroutes.app.domain.model

/**
 * Domain model representing a calculated cycling route.
 */
data class Route(
    val id: String,
    val name: String,
    val distanceKm: Double,
    val durationMinutes: Int,
    val waypointsCount: Int,
    val polylinePoints: List<Pair<Double, Double>> = emptyList(),
    val rawGpx: String? = null,
    val safetySummary: String = ""
)