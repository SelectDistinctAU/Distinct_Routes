package com.distinctroutes.app.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.distinctroutes.app.domain.model.FavoriteRoute

/**
 * Room SQLite Entity for storing saved routes and ride journal history entries.
 */
@Entity(tableName = "favorite_routes")
data class FavoriteRouteEntity(
    @PrimaryKey
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
    val bikeType: String,
    val safetySummary: String,
    val isJournalEntry: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val routePolylineJson: String? = null
) {
    fun toDomainModel(): FavoriteRoute {
        return FavoriteRoute(
            id = id,
            title = title,
            startAddress = startAddress,
            startLat = startLat,
            startLon = startLon,
            endAddress = endAddress,
            endLat = endLat,
            endLon = endLon,
            viaAddress = viaAddress,
            viaLat = viaLat,
            viaLon = viaLon,
            hasViaPoint = hasViaPoint,
            distanceKm = distanceKm,
            durationMinutes = durationMinutes,
            bikeTypeDisplayName = bikeType,
            safetySummary = safetySummary
        )
    }

    companion object {
        fun fromDomainModel(model: FavoriteRoute, isJournalEntry: Boolean = false): FavoriteRouteEntity {
            return FavoriteRouteEntity(
                id = model.id,
                title = model.title,
                startAddress = model.startAddress,
                startLat = model.startLat,
                startLon = model.startLon,
                endAddress = model.endAddress,
                endLat = model.endLat,
                endLon = model.endLon,
                viaAddress = model.viaAddress,
                viaLat = model.viaLat,
                viaLon = model.viaLon,
                hasViaPoint = model.hasViaPoint,
                distanceKm = model.distanceKm,
                durationMinutes = model.durationMinutes,
                bikeType = model.bikeTypeDisplayName,
                safetySummary = model.safetySummary,
                isJournalEntry = isJournalEntry,
                timestamp = System.currentTimeMillis()
            )
        }
    }
}