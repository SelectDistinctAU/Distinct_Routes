package com.distinctroutes.app.data.model

import com.squareup.moshi.JsonClass

/**
 * Response payload from BBBike route calculation.
 *
 * Follows OSRM GeoJSON response schema with BBBike extensions.
 *
 * @see <https://github.com/Project-OSRM/osrm-backend/wiki/Route-Service>
 */
@JsonClass(generateAdapter = true)
data class RoutingResponse(
    /**
     * Route codes indicating success/failure.
     *
     * Possible values:
     * - `Ok`: Route found successfully
     * - `NotFound`: No route between waypoints
     * - `InvalidInput`: Malformed request
     * - `TooManyRequests`: Rate limit exceeded
     */
    val code: String,

    /**
     * Human-readable status message (optional).
     */
    val message: String? = null,

    /**
     * Calculated routes (empty if no route found).
     */
    val routes: List<Route>? = null,

    /**
     * Waypoint information (input validation results).
     */
    val waypoints: List<Waypoint>? = null
) {
    /**
     * Indicates successful route calculation.
     */
    fun isSuccess(): Boolean = code == "Ok"

    /**
     * Indicates failure with reason.
     */
    fun getErrorMessage(): String? =
        when (code) {
            "Ok" -> null
            "NotFound" -> "No route found between specified locations"
            "InvalidInput" -> "Invalid request parameters"
            "TooManyRequests" -> "Rate limit exceeded; please wait before retrying"
            else -> "Unknown error: $code"
        }
}

/**
 * Single calculated route with geometry and metadata.
 */
@JsonClass(generateAdapter = true)
data class Route(
    /**
     * Total distance in metres.
     */
    val distance: Double,

    /**
     * Estimated duration in seconds.
     */
    val duration: Double,

    /**
     * LineString geometry (WGS84, lat-lng order).
     */
    val geometry: Geometry?,

    /**
     * Turn-by-turn instructions (when requested).
     */
    val legs: List<RouteLeg>?,

    /**
     * Weight value used for routing calculations.
     */
    val weight: Double? = null,

    /**
     * Human-readable route name (when available).
     */
    val name: String? = null
) {
    /**
     * Distance in kilometres (human-readable).
     */
    fun distanceKm(): Double = distance / 1000.0

    /**
     * Duration in minutes (human-readable).
     */
    fun durationMinutes(): Int = (duration / 60.0).toInt()
}

/**
 * Geometric representation of route path.
 */
@JsonClass(generateAdapter = true)
data class Geometry(
    /**
     * GeoJSON type (should be "LineString").
     */
    val type: String,

    /**
     * Coordinate pairs as `[lng, lat]` per GeoJSON spec.
     */
    val coordinates: List<List<Double>>
)

/**
 * Route segment between two consecutive waypoints.
 */
@JsonClass(generateAdapter = true)
data class RouteLeg(
    /**
     * Segment distance in metres.
     */
    val distance: Double,

    /**
     * Segment duration in seconds.
     */
    val duration: Double,

    /**
     * Step-by-step turn instructions.
     */
    val steps: List<TurnStep>?,

    /**
     * Start and end waypoint indices.
     */
    val summary: String? = null
) {
    fun distanceKm(): Double = distance / 1000.0
    fun durationMinutes(): Int = (duration / 60.0).toInt()
}

/**
 * Single turn instruction within a route leg.
 */
@JsonClass(generateAdapter = true)
data class TurnStep(
    /**
     * Distance to turn in metres.
     */
    val distance: Double,

    /**
     * Duration until turn in seconds.
     */
    val duration: Double,

    /**
     * Manoeuvre type (e.g., "turn", "fork", "merge").
     */
    val manoeuvre: Manoeuvre?,

    /**
     * Street name at turn location.
     */
    val name: String? = null,

    /**
     * Turning instruction (e.g., "left", "right", "straight").
     */
    val instruction: String? = null,

    /**
     * Exit number for roundabouts/exits (when applicable).
     */
    val exit: Int? = null,

    /**
     * Bearing after manoeuvre (degrees from North).
     */
    val bearingAfter: Int? = null,

    /**
     * Geometry offset into route path.
     */
    val geometry: Geometry? = null
) {
    fun distanceKm(): Double = distance / 1000.0
    fun durationSeconds(): Int = duration.toInt()
}

/**
 * Turn manoeuvre metadata.
 */
@JsonClass(generateAdapter = true)
data class Manoeuvre(
    /**
     * Location coordinates as `[lng, lat]`.
     */
    val location: List<Double>,

    /**
     * Bearing before manoeuvre (degrees from North).
     */
    val bearingBefore: Int,

    /**
     * Bearing after manoeuvre (degrees from North).
     */
    val bearingAfter: Int,

    /**
     * Type of manoeuvre (e.g., "turn", "new name", "depart").
     */
    val type: String,

    /**
     * Direction modifier (e.g., "sharp", "slight", "moderate").
     */
    val modifier: String? = null
)

/**
 * Waypoint validation result from input coordinates.
 */
@JsonClass(generateAdapter = true)
data class Waypoint(
    /**
     * Snapped coordinate as `[lng, lat]`.
     */
    val location: List<Double>,

    /**
     * Name of matched road/location (when available).
     */
    val name: String? = null,

    /**
     * Distance from original coordinate in metres.
     */
    val hint: String? = null
)