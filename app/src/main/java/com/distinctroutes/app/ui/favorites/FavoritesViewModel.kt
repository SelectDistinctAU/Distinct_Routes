package com.distinctroutes.app.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.distinctroutes.app.data.local.FavoritesStorage
import com.distinctroutes.app.data.local.JournalStorage
import com.distinctroutes.app.domain.model.FavoriteRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing saved favorite routes and on-device ride journal history.
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesStorage: FavoritesStorage,
    private val journalStorage: JournalStorage
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<FavoriteRoute>>(emptyList())
    val favorites: StateFlow<List<FavoriteRoute>> = _favorites.asStateFlow()

    private val _journalHistory = MutableStateFlow<List<FavoriteRoute>>(emptyList())
    val journalHistory: StateFlow<List<FavoriteRoute>> = _journalHistory.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _favorites.value = favoritesStorage.getFavorites()
            _journalHistory.value = journalStorage.getJournalHistory()
        }
    }

    fun logRideHistory(route: FavoriteRoute) {
        viewModelScope.launch {
            journalStorage.logRide(route)
            _journalHistory.value = journalStorage.getJournalHistory()
        }
    }

    fun saveFavorite(route: FavoriteRoute) {
        viewModelScope.launch {
            favoritesStorage.saveFavorite(route)
            _favorites.value = favoritesStorage.getFavorites()
        }
    }

    fun removeFavorite(routeId: String) {
        viewModelScope.launch {
            favoritesStorage.removeFavorite(routeId)
            _favorites.value = favoritesStorage.getFavorites()
        }
    }

    fun clearJournalHistory() {
        viewModelScope.launch {
            journalStorage.clearJournal()
            _journalHistory.value = emptyList()
        }
    }
}