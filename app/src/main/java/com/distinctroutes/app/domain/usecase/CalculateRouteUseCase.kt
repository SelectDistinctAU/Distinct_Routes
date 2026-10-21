package com.distinctroutes.app.domain.usecase

import com.distinctroutes.app.data.model.RoutingRequest
import com.distinctroutes.app.data.repository.BikeRouteRepository
import com.distinctroutes.app.domain.model.BikeType
import com.distinctroutes.app.domain.model.Route
import com.distinctroutes.app.domain.model.SafetyPreferences
import java.util.UUID
import javax.inject.Inject

/**
 * UseCase for executing route calculations with vehicle profile and safety preferences applied.
 */
class CalculateRouteUseCase @Inject constructor(
    private val repository: BikeRouteRepository
) {
    suspend fun execute(
        startLat: Double,
        startLon: Double,
        endLat: Double,
        endLon: Double,
        preferences: SafetyPreferences,
        viaWaypoints: List<Pair<Double, Double>> = emptyList()
    ): Result<Route> {
        val request = RoutingRequest(
            startLat = startLat,
            startLon = startLon,
            endLat = endLat,
            endLon = endLon,
            viaWaypoints = viaWaypoints,
            profile = preferences.bikeType.profileName,
            preferResidentialRoads = preferences.preferResidentialRoads,
            useFerries = preferences.useFerries,
            preferredSpeedKmH = preferences.preferredSpeedKmH,
            roadSurfaceQuality = preferences.roadSurfaceQuality,
            avoidTrafficLights = preferences.avoidTrafficLights,
            avoidHighways = preferences.effectiveAvoidHighways(),
            avoidSteepHills = preferences.effectiveAvoidSteepHills(),
            hasDogTrailer = preferences.hasDogTrailer,
            bikeType = preferences.bikeType.displayName
        )

        return when (val result = repository.getRoute(request)) {
            is BikeRouteRepository.RouteResult.Success -> {
                val routeData = result.response.routes?.firstOrNull()
                val distanceKm = routeData?.distanceKm() ?: 0.0

                // Calculate realistic speed & duration for chosen vehicle/mobility profile
                val avgSpeedKmH = when (preferences.bikeType) {
                    BikeType.WHEELCHAIR -> 6.0
                    BikeType.MOBILITY_SCOOTER -> 8.0
                    BikeType.CARGO -> if (preferences.hasDogTrailer) 10.0 else 12.0
                    BikeType.TANDEM -> 13.0
                    BikeType.MOUNTAIN -> 12.0
                    BikeType.CITY -> 14.0
                    BikeType.E_BIKE -> 20.0
                    BikeType.RECUMBENT -> 18.0
                    BikeType.STANDARD -> if (preferences.hasDogTrailer) 11.0 else 15.0
                }

                // Additional hill climbing penalty if dog trailer or cargo bike
                val hillPenaltyFactor = if (preferences.hasDogTrailer) 1.25 else 1.0
                val durationMins = ((distanceKm / avgSpeedKmH) * 60.0 * hillPenaltyFactor).toInt().coerceAtLeast(1)

                val summaryParts = mutableListOf<String>()
                summaryParts.add("Profile: ${preferences.bikeType.displayName}")
                summaryParts.add("Speed: ${avgSpeedKmH.toInt()} km/h")

                if (viaWaypoints.isNotEmpty()) {
                    summaryParts.add("${viaWaypoints.size} Via Stop${if (viaWaypoints.size > 1) "s" else ""} Included")
                }
                if (preferences.preferResidentialRoads == "N1") summaryParts.add("Prefers quiet residential roads")
                if (preferences.useFerries) summaryParts.add("Uses ferries")
                if (preferences.preferGreenRoutes) summaryParts.add("Prefers green/scenic routes")
                if (preferences.avoidTrafficLights) summaryParts.add("Avoids traffic lights")
                if (preferences.effectiveAvoidHighways()) summaryParts.add("Avoids highways")
                if (preferences.effectiveAvoidSteepHills()) summaryParts.add("Avoids steep hills")
                if (preferences.hasDogTrailer) summaryParts.add("Dog trailer weight penalty 1.5x")

                val points = routeData?.geometry?.coordinates?.mapNotNull { coord ->
                    if (coord.size >= 2) Pair(coord[1], coord[0]) else null
                } ?: (listOf(Pair(startLat, startLon)) + viaWaypoints + listOf(Pair(endLat, endLon)))

                val routeName = "${preferences.bikeType.displayName} Route${if (viaWaypoints.isNotEmpty()) " (${viaWaypoints.size} Via Stop${if (viaWaypoints.size > 1) "s" else ""})" else ""}"

                Result.success(
                    Route(
                        id = UUID.randomUUID().toString(),
                        name = routeName,
                        distanceKm = distanceKm,
                        durationMinutes = durationMins,
                        waypointsCount = points.size,
                        polylinePoints = points,
                        rawGpx = result.rawGpx,
                        safetySummary = summaryParts.joinToString(" • ")
                    )
                )
            }
            is BikeRouteRepository.RouteResult.Failure -> {
                Result.failure(Exception(result.error))
            }
            is BikeRouteRepository.RouteResult.OfflineFallback -> {
                Result.failure(Exception(result.message))
            }
        }
    }
}