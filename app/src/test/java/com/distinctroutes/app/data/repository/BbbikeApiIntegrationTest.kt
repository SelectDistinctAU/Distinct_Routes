package com.distinctroutes.app.data.repository

import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

class BbbikeApiIntegrationTest {

    @Test
    fun `test live BBBike API GPX route parsing for Perth`() {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val url = "https://api.bbbike.org/api/0.2/Perth/?appid=guest&startc_wgs84=115.90258,-32.00242&zielc_wgs84=115.87861,-31.96202&pref_cat=N1&pref_speed=20&pref_seen=1&output_as=gpx-route"

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "DistinctRoutes/0.1.0 (distinctroutes@pm.me; Android)")
            .build()

        val response = client.newCall(request).execute()
        val rawText = response.body?.string() ?: ""

        assertEquals("Expected HTTP 200 from BBBike API", 200, response.code)

        val ptRegex = Regex("""(?:<rtept|<trkpt)\s+lat=["']([^"']+)["']\s+lon=["']([^"']+)["']""")
        val matches = ptRegex.findAll(rawText).toList()

        println("Extracted Waypoint Count from BBBike GPX: ${matches.size}")
        assertTrue("Extracted waypoints should be > 5, got ${matches.size}", matches.size > 5)
    }
}