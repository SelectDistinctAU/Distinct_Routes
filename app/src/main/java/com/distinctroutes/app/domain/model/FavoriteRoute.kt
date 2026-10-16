package com.distinctroutes.app.domain.model

import com.squareup.moshi.JsonClass

/**
 * Represents a saved favorite route for quick reloading and navigation handoff.
 */
@JsonClass(generateAdapter = true)
data class FavoriteRoute(
    val id: String,
    val title: String,
    val startAddress: String,
    val startLat: Double,
    val startLon: Double,
    val endAddress: String,
    val endLat: Double,
    val endLon: Double,
    val viaAddress: String? = null,
    val viaLat: Double? = null,
    val viaLon: Double? = null,
    val hasViaPoint: Boolean = false,
    val distanceKm: Double,
    val durationMinutes: Int,
    val bikeTypeDisplayName: String,
    val safetySummary: String,
    val savedTimestamp: Long = System.currentTimeMillis()
)