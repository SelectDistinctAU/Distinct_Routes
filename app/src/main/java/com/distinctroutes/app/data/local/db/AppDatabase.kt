package com.distinctroutes.app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Main Room Database for Distinct Routes.
 */
@Database(
    entities = [FavoriteRouteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(BikeTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteRouteDao(): FavoriteRouteDao
}