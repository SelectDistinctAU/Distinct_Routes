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
 * Local storage helper managing on-device calculated ride history log (Ride Journal).
 */
@Singleton
class JournalStorage @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "DistinctRoutesJournal",
        Context.MODE_PRIVATE
    )

    private val type = Types.newParameterizedType(List::class.java, FavoriteRoute::class.java)
    private val adapter by lazy { moshi.adapter<List<FavoriteRoute>>(type) }

    fun getJournalHistory(): List<FavoriteRoute> {
        return try {
            val json = prefs.getString("key_journal_json", null) ?: return emptyList()
            adapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun logRide(route: FavoriteRoute) {
        val current = getJournalHistory().toMutableList()
        current.removeAll { it.startAddress == route.startAddress && it.endAddress == route.endAddress }
        current.add(0, route)
        // Keep maximum 50 recent calculated rides
        val trimmed = current.take(50)
        persist(trimmed)
    }

    fun clearJournal() {
        prefs.edit().remove("key_journal_json").apply()
    }

    private fun persist(list: List<FavoriteRoute>) {
        try {
            val json = adapter.toJson(list)
            prefs.edit().putString("key_journal_json", json).apply()
        } catch (e: Exception) {
            // Silently ignore write errors
        }
    }
}