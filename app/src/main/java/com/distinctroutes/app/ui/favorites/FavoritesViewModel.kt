package com.distinctroutes.app.ui.favorites

import androidx.lifecycle.ViewModel
import com.distinctroutes.app.data.local.FavoritesStorage
import com.distinctroutes.app.data.local.JournalStorage
import com.distinctroutes.app.domain.model.FavoriteRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        _favorites.value = favoritesStorage.getFavorites()
        _journalHistory.value = journalStorage.getJournalHistory()
    }

    fun logRideHistory(route: FavoriteRoute) {
        journalStorage.logRide(route)
        _journalHistory.value = journalStorage.getJournalHistory()
    }

    fun saveFavorite(route: FavoriteRoute) {
        favoritesStorage.saveFavorite(route)
        _favorites.value = favoritesStorage.getFavorites()
    }

    fun removeFavorite(routeId: String) {
        favoritesStorage.removeFavorite(routeId)
        _favorites.value = favoritesStorage.getFavorites()
    }

    fun clearJournalHistory() {
        journalStorage.clearJournal()
        _journalHistory.value = emptyList()
    }
}