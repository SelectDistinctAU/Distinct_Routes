package com.distinctroutes.app.data.local

import android.content.Context
import com.distinctroutes.app.data.local.db.FavoriteRouteDao
import com.distinctroutes.app.data.local.db.FavoriteRouteEntity
import com.distinctroutes.app.domain.model.FavoriteRoute
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Migration helper to migrate legacy SharedPreferences JSON records to Room Database on first launch.
 */
@Singleton
class StorageMigrationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val favoriteRouteDao: FavoriteRouteDao,
    private val moshi: Moshi
) {
    private val prefs = context.getSharedPreferences("distinct_routes_prefs", Context.MODE_PRIVATE)

    suspend fun performMigrationIfNeeded() {
        if (prefs.getBoolean("key_room_migrated", false)) {
            return
        }

        try {
            val listType = Types.newParameterizedType(List::class.java, FavoriteRoute::class.java)
            val adapter = moshi.adapter<List<FavoriteRoute>>(listType)

            // 1. Migrate Favourites
            val favJson = prefs.getString("key_favorites_list", null)
            if (!favJson.isNullOrBlank()) {
                val favList = adapter.fromJson(favJson) ?: emptyList()
                favList.forEach { fav ->
                    favoriteRouteDao.insertRoute(
                        FavoriteRouteEntity.fromDomainModel(fav, isJournalEntry = false)
                    )
                }
            }

            // 2. Migrate Ride Journal
            val journalJson = prefs.getString("key_journal_list", null)
            if (!journalJson.isNullOrBlank()) {
                val journalList = adapter.fromJson(journalJson) ?: emptyList()
                journalList.forEach { journal ->
                    favoriteRouteDao.insertRoute(
                        FavoriteRouteEntity.fromDomainModel(journal, isJournalEntry = true)
                    )
                }
            }

            // Mark migration complete
            prefs.edit().putBoolean("key_room_migrated", true).apply()
        } catch (e: Exception) {
            // Non-blocking fallback
            prefs.edit().putBoolean("key_room_migrated", true).apply()
        }
    }
}