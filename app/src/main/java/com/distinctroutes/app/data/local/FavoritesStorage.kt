package com.distinctroutes.app.data.local

import com.distinctroutes.app.data.local.db.FavoriteRouteDao
import com.distinctroutes.app.data.local.db.FavoriteRouteEntity
import com.distinctroutes.app.domain.model.FavoriteRoute
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local storage manager delegating saved favorite routes persistence to Room Database DAO.
 */
@Singleton
class FavoritesStorage @Inject constructor(
    private val favoriteRouteDao: FavoriteRouteDao,
    private val migrationHelper: StorageMigrationHelper
) {

    suspend fun getFavorites(): List<FavoriteRoute> {
        migrationHelper.performMigrationIfNeeded()
        return favoriteRouteDao.getFavorites().map { it.toDomainModel() }
    }

    suspend fun saveFavorite(route: FavoriteRoute) {
        migrationHelper.performMigrationIfNeeded()
        val entity = FavoriteRouteEntity.fromDomainModel(route, isJournalEntry = false)
        favoriteRouteDao.insertRoute(entity)
    }

    suspend fun removeFavorite(routeId: String) {
        migrationHelper.performMigrationIfNeeded()
        favoriteRouteDao.deleteRouteById(routeId)
    }

    suspend fun isFavorite(startAddress: String, endAddress: String): Boolean {
        migrationHelper.performMigrationIfNeeded()
        return getFavorites().any { it.startAddress == startAddress && it.endAddress == endAddress }
    }
}