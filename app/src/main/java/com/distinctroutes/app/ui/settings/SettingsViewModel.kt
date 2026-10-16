package com.distinctroutes.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.distinctroutes.app.data.local.PreferencesStorage
import com.distinctroutes.app.data.repository.BikeRouteRepository
import com.distinctroutes.app.domain.model.BikeType
import com.distinctroutes.app.domain.model.SafetyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel managing application settings, default city, theme mode, brand color, and vehicle safety defaults.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesStorage: PreferencesStorage,
    private val repository: BikeRouteRepository
) : ViewModel() {

    private val _selectedCity = MutableStateFlow(preferencesStorage.getDefaultCity())
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    private val _themeMode = MutableStateFlow(preferencesStorage.getThemeMode())
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _colourMode = MutableStateFlow(preferencesStorage.getColourMode())
    val colourMode: StateFlow<String> = _colourMode.asStateFlow()

    private val _availableCities = MutableStateFlow<List<String>>(
        listOf("Perth", "Berlin", "Canberra", "Sydney", "Melbourne", "Brisbane", "Adelaide", "London", "Paris", "Rome", "Madrid")
    )
    val availableCities: StateFlow<List<String>> = _availableCities.asStateFlow()

    private val _preferences = MutableStateFlow(preferencesStorage.getSafetyPreferences())
    val preferences: StateFlow<SafetyPreferences> = _preferences.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val list = repository.getSupportedCities()
                if (list.isNotEmpty()) {
                    _availableCities.value = list
                }
            } catch (e: Exception) {
                // Fallback to default built-in list
            }
        }
    }

    fun updateDefaultCity(city: String) {
        _selectedCity.value = city
        preferencesStorage.saveDefaultCity(city)
    }

    fun updateThemeMode(mode: String) {
        _themeMode.value = mode
        preferencesStorage.saveThemeMode(mode)
    }

    fun updateColourMode(mode: String) {
        _colourMode.value = mode
        preferencesStorage.saveColourMode(mode)
    }

    fun updateSafetyPreferences(
        avoidTrafficLights: Boolean = _preferences.value.avoidTrafficLights,
        avoidHighways: Boolean = _preferences.value.avoidHighways,
        avoidSteepHills: Boolean = _preferences.value.avoidSteepHills,
        hasDogTrailer: Boolean = _preferences.value.hasDogTrailer,
        preferGreenRoutes: Boolean = _preferences.value.preferGreenRoutes,
        avoidUnlitStreets: Boolean = _preferences.value.avoidUnlitStreets,
        avoidCobblestones: Boolean = _preferences.value.avoidCobblestones,
        avoidFerries: Boolean = _preferences.value.avoidFerries,
        bikeType: BikeType = _preferences.value.bikeType
    ) {
        val updated = _preferences.value.copy(
            avoidTrafficLights = avoidTrafficLights,
            avoidHighways = avoidHighways,
            avoidSteepHills = avoidSteepHills,
            hasDogTrailer = hasDogTrailer,
            preferGreenRoutes = preferGreenRoutes,
            avoidUnlitStreets = avoidUnlitStreets,
            avoidCobblestones = avoidCobblestones,
            avoidFerries = avoidFerries,
            bikeType = bikeType
        )
        _preferences.update { updated }
        preferencesStorage.saveSafetyPreferences(updated)
    }
}