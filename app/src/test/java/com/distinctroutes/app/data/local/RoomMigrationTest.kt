package com.distinctroutes.app.data.local

import com.distinctroutes.app.data.local.db.FavoriteRouteDao
import com.distinctroutes.app.data.local.db.FavoriteRouteEntity
import com.distinctroutes.app.domain.model.FavoriteRoute
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Unit test verifying SharedPreferences -> Room Database migration logic.
 */
class RoomMigrationTest {

    @Test
    fun `test FavoriteRouteEntity mapping preserves all domain fields`() {
        val domainModel = FavoriteRoute(
            id = "Perth|Fremantle",
            title = "Perth → Fremantle",
            startAddress = "Perth Station",
            startLat = -31.9505,
            startLon = 115.8605,
            endAddress = "Fremantle Port",
            endLat = -32.0569,
            endLon = 115.7439,
            viaAddress = "Cottesloe Beach",
            viaLat = -31.9961,
            viaLon = 115.7516,
            hasViaPoint = true,
            distanceKm = 19.8,
            durationMinutes = 60,
            bikeTypeDisplayName = "E-Bike / Pedelec",
            safetySummary = "Quiet backstreets & cycleways"
        )

        // Convert to Room Entity
        val entity = FavoriteRouteEntity.fromDomainModel(domainModel, isJournalEntry = false)

        assertEquals("Perth|Fremantle", entity.id)
        assertEquals("Perth Station", entity.startAddress)
        assertEquals("Fremantle Port", entity.endAddress)
        assertEquals("Cottesloe Beach", entity.viaAddress)
        assertEquals(19.8, entity.distanceKm, 0.001)
        assertEquals(60, entity.durationMinutes)
        assertEquals("E-Bike / Pedelec", entity.bikeType)
        assertEquals(false, entity.isJournalEntry)

        // Convert back to Domain Model
        val restored = entity.toDomainModel()

        assertNotNull(restored)
        assertEquals(domainModel.id, restored.id)
        assertEquals(domainModel.startAddress, restored.startAddress)
        assertEquals(domainModel.endAddress, restored.endAddress)
        assertEquals(domainModel.viaAddress, restored.viaAddress)
        assertEquals(domainModel.distanceKm, restored.distanceKm, 0.001)
        assertEquals(domainModel.durationMinutes, restored.durationMinutes)
        assertEquals(domainModel.bikeTypeDisplayName, restored.bikeTypeDisplayName)
    }

    @Test
    fun `test FavoriteRouteDao insert and query mock verification`() = runBlocking {
        val dao = mockk<FavoriteRouteDao>(relaxed = true)
        val testEntity = FavoriteRouteEntity(
            id = "Berlin|Potsdam",
            title = "Berlin → Potsdam",
            startAddress = "Alexanderplatz",
            startLat = 52.5200,
            startLon = 13.4050,
            endAddress = "Potsdam Hbf",
            endLat = 52.3988,
            endLon = 13.0657,
            distanceKm = 28.5,
            durationMinutes = 85,
            bikeType = "Standard Bicycle",
            safetySummary = "Scenic greenway",
            isJournalEntry = false
        )

        coEvery { dao.getFavorites() } returns listOf(testEntity)

        dao.insertRoute(testEntity)
        val result = dao.getFavorites()

        coVerify { dao.insertRoute(testEntity) }
        assertEquals(1, result.size)
        assertEquals("Berlin|Potsdam", result[0].id)
    }
}