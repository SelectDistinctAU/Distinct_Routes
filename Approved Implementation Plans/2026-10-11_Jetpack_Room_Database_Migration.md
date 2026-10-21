# Approved Implementation Plan — Jetpack Room Database Migration & Architectural Decoupling

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Migrate Distinct Routes' Ride Journal and Favourites storage from SharedPreferences JSON serialization to Jetpack Room ORM SQLite database (adopted from Fotologue's database patterns), incorporating first-launch data migration, typed vehicle profile converters, explicit coordinate storage, and migration verification tests.

## User Review Required & Four Approval Conditions

> [!IMPORTANT]
> **Condition 1: First-Launch SharedPreferences → Room Data Migration**
> * On first launch after upgrade, a `MigrationHelper` reads existing JSON routes from `FavoritesStorage` and `JournalStorage` in SharedPreferences, converts them to `FavoriteRouteEntity` objects, inserts them into Room `AppDatabase`, and sets `key_room_migrated = true` to prevent data loss.

> [!NOTE]
> **Condition 2: Vehicle Profile Representation & TypeConverter**
> * Vehicle profile is stored as a `String` column (`bikeType: String`, e.g. `"E_BIKE"`, `"WHEELCHAIR"`). A Room `@TypeConverter` (`BikeTypeConverter`) maps between `String` and the domain `BikeType` enum for type-safe database queries.

> [!NOTE]
> **Condition 3: Coordinate Storage Representation & Rationale**
> * Coordinates are stored as explicit primitive `Double` columns (`startLat`, `startLon`, `endLat`, `endLon`, `viaLat`, `viaLon`) alongside an optional `routePolylineJson: String?` GeoJSON geometry field.
> * **Rationale:** Primitive `Double` columns enable high-performance, zero-deserialization SQL bounding box queries (`WHERE startLat BETWEEN :minLat AND :maxLat`), while `routePolylineJson` preserves exact polyline street geometry for instant 1-tap route re-loading into the planner.

> [!NOTE]
> **Condition 4: Automated Migration Test Case**
> * Includes an automated test ([RoomMigrationTest.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/test/java/com/distinctroutes/app/data/local/RoomMigrationTest.kt)) that populates legacy SharedPreferences JSON records, executes the migration, and asserts 100% data preservation in Room.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Jetpack_Room_Database_Migration.md` upon approval.

---

### Room Database & Migration Layer

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/build.gradle.kts)
- Add Jetpack Room dependencies (`androidx.room:room-runtime`, `androidx.room:room-ktx`, `androidx.room:room-compiler`).

#### [NEW] [FavoriteRouteEntity.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/local/db/FavoriteRouteEntity.kt)
- Define Room `@Entity(tableName = "favorite_routes")` with columns:
  - `id: String` (PrimaryKey)
  - `title: String`, `startAddress: String`, `endAddress: String`, `viaAddress: String?`
  - `startLat: Double`, `startLon: Double`, `endLat: Double`, `endLon: Double`, `viaLat: Double?`, `viaLon: Double?`
  - `distanceKm: Double`, `durationMinutes: Int`, `bikeType: String`, `safetySummary: String`
  - `hasViaPoint: Boolean`, `isJournalEntry: Boolean`, `timestamp: Long`, `routePolylineJson: String?`

#### [NEW] [BikeTypeConverter.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/local/db/BikeTypeConverter.kt)
- Room `@TypeConverter` converting `BikeType` enum to/from `String`.

#### [NEW] [FavoriteRouteDao.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/local/db/FavoriteRouteDao.kt)
- Define Room `@Dao` interface:
  - `@Query("SELECT * FROM favorite_routes WHERE isJournalEntry = 0 ORDER BY timestamp DESC") fun getFavorites()`
  - `@Query("SELECT * FROM favorite_routes WHERE isJournalEntry = 1 ORDER BY timestamp DESC") fun getJournalHistory()`
  - `@Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertRoute(route: FavoriteRouteEntity)`
  - `@Delete suspend fun deleteRoute(route: FavoriteRouteEntity)`
  - `@Query("DELETE FROM favorite_routes WHERE isJournalEntry = 1") suspend fun clearJournal()`

#### [NEW] [AppDatabase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/local/db/AppDatabase.kt) & [DatabaseModule.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/di/DatabaseModule.kt)
- Configure Room `@Database(entities = [FavoriteRouteEntity::class], version = 1)` and Hilt DI module supplying singleton `AppDatabase` and `FavoriteRouteDao`.

#### [NEW] [StorageMigrationHelper.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/local/StorageMigrationHelper.kt)
- Performs first-launch migration: reads legacy SharedPreferences JSON strings from `FavoritesStorage` and `JournalStorage`, converts to `FavoriteRouteEntity`, inserts into Room, and marks `key_room_migrated = true`.

#### [MODIFY] [JournalStorage.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/local/JournalStorage.kt) & [FavoritesStorage.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/local/FavoritesStorage.kt)
- Delegate persistence operations directly to `FavoriteRouteDao`.

---

### Verification Layer

#### [NEW] [RoomMigrationTest.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/test/java/com/distinctroutes/app/data/local/RoomMigrationTest.kt)
- Unit test verifying that pre-existing SharedPreferences JSON records are migrated cleanly to Room DAO with 100% data integrity.

---

## Verification Plan

### Automated Tests
- Run `./gradlew test` to execute `RoomMigrationTest`.
- Run `./gradlew assembleDebug` to verify project compilation.

### Manual Verification
- Launch app on emulator with existing SharedPreferences records and verify all Favourites and Ride Journal entries populate in Room without data loss.
