package com.distinctroutes.app.data.local

import com.distinctroutes.app.data.local.db.FavoriteRouteDao
import com.distinctroutes.app.data.local.db.FavoriteRouteEntity
import com.distinctroutes.app.domain.model.FavoriteRoute
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local storage manager delegating on-device ride journal history persistence to Room Database DAO.
 */
@Singleton
class JournalStorage @Inject constructor(
    private val favoriteRouteDao: FavoriteRouteDao,
    private val migrationHelper: StorageMigrationHelper
) {

    suspend fun getJournalHistory(): List<FavoriteRoute> {
        migrationHelper.performMigrationIfNeeded()
        return favoriteRouteDao.getJournalHistory().map { it.toDomainModel() }
    }

    suspend fun logRide(route: FavoriteRoute) {
        migrationHelper.performMigrationIfNeeded()
        val entity = FavoriteRouteEntity.fromDomainModel(route, isJournalEntry = true)
        favoriteRouteDao.insertRoute(entity)
    }

    suspend fun clearJournal() {
        migrationHelper.performMigrationIfNeeded()
        favoriteRouteDao.clearJournal()
    }
}