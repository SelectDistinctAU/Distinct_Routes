package com.distinctroutes.app.data.repository

import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

class BbbikeApiIntegrationTest {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun testBbbikeCityRoute(city: String, startLonLat: String, zielLonLat: String) {
        val url = "https://api.bbbike.org/api/0.2/$city/?appid=guest&startc_wgs84=$startLonLat&zielc_wgs84=$zielLonLat&pref_cat=N1&pref_speed=20&pref_seen=1&output_as=gpx-route"

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "DistinctRoutes/0.1.0 (distinctroutes@pm.me; Android)")
            .build()

        val response = client.newCall(request).execute()
        val rawText = response.body?.string() ?: ""

        assertEquals("Expected HTTP 200 from BBBike API for $city", 200, response.code)

        val ptRegex = Regex("""(?:<rtept|<trkpt)\s+lat=["']([^"']+)["']\s+lon=["']([^"']+)["']""")
        val matches = ptRegex.findAll(rawText).toList()

        println("City $city: Extracted ${matches.size} waypoints from BBBike GPX")
        assertTrue("Extracted waypoints for $city should be > 5, got ${matches.size}", matches.size > 5)
    }

    @Test
    fun `test live BBBike API for Perth Australia`() {
        testBbbikeCityRoute("Perth", "115.90258,-32.00242", "115.87861,-31.96202")
    }

    @Test
    fun `test live BBBike API for Berlin Germany`() {
        testBbbikeCityRoute("bbbike", "13.377846,52.516265", "13.404294,52.535019")
    }

    @Test
    fun `test live BBBike API for Sydney Australia`() {
        testBbbikeCityRoute("Sydney", "151.2153,-33.8568", "151.2008,-33.8732")
    }
}