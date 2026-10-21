package com.distinctroutes.app.di

import android.content.Context
import androidx.room.Room
import com.distinctroutes.app.data.local.db.AppDatabase
import com.distinctroutes.app.data.local.db.FavoriteRouteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Dependency Injection module for Room Database and DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "distinct_routes_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideFavoriteRouteDao(database: AppDatabase): FavoriteRouteDao {
        return database.favoriteRouteDao()
    }
}