package com.distinctroutes.app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.distinctroutes.app.domain.model.FavoriteRoute
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local storage helper managing saved favorite routes using Moshi JSON serialization.
 */
@Singleton
class FavoritesStorage @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "DistinctRoutesFavorites",
        Context.MODE_PRIVATE
    )

    private val type = Types.newParameterizedType(List::class.java, FavoriteRoute::class.java)
    private val adapter by lazy { moshi.adapter<List<FavoriteRoute>>(type) }

    fun getFavorites(): List<FavoriteRoute> {
        return try {
            val json = prefs.getString("key_favorites_json", null) ?: return emptyList()
            adapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveFavorite(route: FavoriteRoute) {
        val current = getFavorites().toMutableList()
        current.removeAll { it.id == route.id }
        current.add(0, route)
        persist(current)
    }

    fun removeFavorite(routeId: String) {
        val current = getFavorites().toMutableList()
        current.removeAll { it.id == routeId }
        persist(current)
    }

    fun isFavorite(startAddress: String, endAddress: String): Boolean {
        return getFavorites().any { it.startAddress == startAddress && it.endAddress == endAddress }
    }

    private fun persist(list: List<FavoriteRoute>) {
        try {
            val json = adapter.toJson(list)
            prefs.edit().putString("key_favorites_json", json).apply()
        } catch (e: Exception) {
            // Silently ignore write errors
        }
    }
}