package com.distinctroutes.app.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object for Room database operations on saved routes and ride journal history.
 */
@Dao
interface FavoriteRouteDao {

    @Query("SELECT * FROM favorite_routes WHERE isJournalEntry = 0 ORDER BY timestamp DESC")
    suspend fun getFavorites(): List<FavoriteRouteEntity>

    @Query("SELECT * FROM favorite_routes WHERE isJournalEntry = 1 ORDER BY timestamp DESC")
    suspend fun getJournalHistory(): List<FavoriteRouteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: FavoriteRouteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(routes: List<FavoriteRouteEntity>)

    @Delete
    suspend fun deleteRoute(route: FavoriteRouteEntity)

    @Query("DELETE FROM favorite_routes WHERE id = :routeId")
    suspend fun deleteRouteById(routeId: String)

    @Query("DELETE FROM favorite_routes WHERE isJournalEntry = 1")
    suspend fun clearJournal()
}