package com.distinctroutes.app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.distinctroutes.app.domain.model.BikeType
import com.distinctroutes.app.domain.model.SafetyPreferences
import com.distinctroutes.app.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local SharedPreferences storage for persistent user settings and preferences.
 */
@Singleton
class PreferencesStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        Constants.PREFS_NAME,
        Context.MODE_PRIVATE
    )

    fun getDefaultCity(): String {
        return prefs.getString(Constants.KEY_SELECTED_CITY, "Berlin") ?: "Berlin"
    }

    fun saveDefaultCity(city: String) {
        prefs.edit().putString(Constants.KEY_SELECTED_CITY, city).apply()
    }

    fun getThemeMode(): String {
        return prefs.getString("key_theme_mode", "light") ?: "light"
    }

    fun saveThemeMode(mode: String) {
        prefs.edit().putString("key_theme_mode", mode).apply()
    }

    fun getColourMode(): String {
        return prefs.getString("pref_colour_mode", "blue") ?: "blue"
    }

    fun saveColourMode(mode: String) {
        prefs.edit().putString("pref_colour_mode", mode).apply()
    }

    fun getSafetyPreferences(): SafetyPreferences {
        val bikeTypeName = prefs.getString("key_bike_type", BikeType.STANDARD.name) ?: BikeType.STANDARD.name
        val bikeType = try {
            BikeType.valueOf(bikeTypeName)
        } catch (e: Exception) {
            BikeType.STANDARD
        }

        return SafetyPreferences(
            preferResidentialRoads = prefs.getString("key_prefer_residential_roads", "N1") ?: "N1",
            useFerries = prefs.getBoolean("key_use_ferries", false),
            preferredSpeedKmH = prefs.getInt("key_preferred_speed", 20),
            roadSurfaceQuality = prefs.getString("key_road_surface", "") ?: "",
            avoidTrafficLights = prefs.getBoolean("key_avoid_traffic_lights", false),
            avoidHighways = prefs.getBoolean("key_avoid_highways", false),
            avoidSteepHills = prefs.getBoolean("key_avoid_steep_hills", false),
            hasDogTrailer = prefs.getBoolean("key_has_dog_trailer", false),
            preferGreenRoutes = prefs.getBoolean("key_prefer_green_routes", false),
            avoidUnlitStreets = prefs.getBoolean("key_avoid_unlit_streets", false),
            avoidCobblestones = prefs.getBoolean("key_avoid_cobblestones", false),
            bikeType = bikeType
        )
    }

    fun saveSafetyPreferences(preferences: SafetyPreferences) {
        prefs.edit()
            .putString("key_prefer_residential_roads", preferences.preferResidentialRoads)
            .putBoolean("key_use_ferries", preferences.useFerries)
            .putInt("key_preferred_speed", preferences.preferredSpeedKmH)
            .putString("key_road_surface", preferences.roadSurfaceQuality)
            .putBoolean("key_avoid_traffic_lights", preferences.avoidTrafficLights)
            .putBoolean("key_avoid_highways", preferences.avoidHighways)
            .putBoolean("key_avoid_steep_hills", preferences.avoidSteepHills)
            .putBoolean("key_has_dog_trailer", preferences.hasDogTrailer)
            .putBoolean("key_prefer_green_routes", preferences.preferGreenRoutes)
            .putBoolean("key_avoid_unlit_streets", preferences.avoidUnlitStreets)
            .putBoolean("key_avoid_cobblestones", preferences.avoidCobblestones)
            .putString("key_bike_type", preferences.bikeType.name)
            .apply()
    }
}