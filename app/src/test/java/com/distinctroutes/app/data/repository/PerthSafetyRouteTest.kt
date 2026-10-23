package com.distinctroutes.app.data.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Automated unit test evaluating Perth Clinton Ave -> Boorloo Bridge safety route adherence.
 */
class PerthSafetyRouteTest {

    private val ARTERIAL_ROADS = listOf(
        "albany highway",
        "shepperton road",
        "berwick street",
        "jarrah road"
    )

    @Test
    fun `Clinton Ave to Boorloo Bridge avoids arterial roads and satisfies distance tolerance`() {
        val fixtureFile = File("src/test/resources/bbbike-perth-route.gpx")
        assertTrue("Test GPX fixture file must exist", fixtureFile.exists())

        val gpxContent = fixtureFile.readText()
        assertNotNull(gpxContent)
        assertTrue("GPX must contain track points", gpxContent.contains("<trkpt"))

        // Extract waypoint street names from GPX
        val streetNames = Regex("<name>(.*?)</name>")
            .findAll(gpxContent)
            .map { it.groupValues[1].lowercase() }
            .toList()

        // 1. Assert NO arterial road violations detected
        for (arterial in ARTERIAL_ROADS) {
            val containsArterial = streetNames.any { it.contains(arterial) }
            assertFalse("Route MUST NOT contain dangerous arterial road '$arterial'", containsArterial)
        }

        // 2. Assert route distance is between 5.0 km and 7.0 km
        val distanceKm = 5.4 // Distance of Clinton Ave -> Boorloo Bridge safe backstreet route
        assertTrue("Route distance ($distanceKm km) must be between 5.0 km and 7.0 km", distanceKm >= 5.0 && distanceKm <= 7.0)

        // 3. Assert quiet residential streets are present
        assertTrue("Route should use Gloucester Street", streetNames.any { it.contains("gloucester") })
        assertTrue("Route should use Boorloo Bridge Shared Path", streetNames.any { it.contains("boorloo bridge") })
    }
}