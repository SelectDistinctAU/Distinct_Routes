# Approved Implementation Plan — Greenfields Clean-Slate Rebuild & Legacy Jettison

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Refactor Distinct Routes on the main codebase into the clean-slate multi-module Greenfields architecture specified in GREENFIELDS_PROJECT_BRIEF.md, permanently deleting all legacy code, fallback OSRM endpoints, and unused assets.

## User Review Required

> [!IMPORTANT]
> **Clean-Slate Rebuild on Main Codebase:** Refactors the main project into a decoupled multi-module Gradle structure (`:core:model`, `:core:data`, `:core:network`, `:feature:planner`, `:feature:favorites`, `:feature:settings`, `:app`) and jettisons all legacy fallback code so testers only interact with the production greenfields app.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Greenfields_Clean_Slate_Rebuild.md` upon approval.

---

### Multi-Module Gradle Configuration

#### [MODIFY] [settings.gradle.kts](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/settings.gradle.kts)
- Register multi-module layout:
  ```kotlin
  include(":app")
  include(":core:model")
  include(":core:data")
  include(":core:network")
  include(":feature:planner")
  include(":feature:favorites")
  include(":feature:settings")
  ```

---

### Module Implementation & Legacy Jettison

#### [NEW] `:core:model`
- Pure Kotlin domain models (`Route`, `BikeType`, `FavoriteRoute`, `SafetyPreferences`).

#### [NEW] `:core:data`
- Jetpack Room ORM SQLite Database (`AppDatabase`, `FavoriteRouteDao`, `FavoriteRouteEntity`, `StorageMigrationHelper`).

#### [NEW] `:core:network`
- Native BBBike REST API v0.2 Retrofit client (`https://api.bbbike.org/api/0.2/{city}/`) and GPX/GeoJSON parsers.
- Permanently deletes legacy OSRM fallback methods.

#### [NEW] `:feature:planner`
- 100% Full-Screen Floating Canvas Map UI (`MapViewContainer`, `RoutePlannerScreen`, `RoutePlannerViewModel`).

#### [NEW] `:feature:favorites`
- Favourites & Ride Journal UI (`FavoritesScreen`, `FavoritesViewModel`).

#### [NEW] `:feature:settings`
- Settings UI & Theme Selector (`SettingsScreen`, `SettingsViewModel`).

#### [MODIFY] `:app`
- Main Application entry point, Hilt DI graph, and Bottom Navigation Scaffold.

---

## Verification Plan

### Automated Tests
- Run `./gradlew test` across all modules.
- Run `./gradlew bundleRelease` to verify production release AAB build.

### Manual Verification
- Deploy app to emulator and verify route planning, database storage, and map canvas rendering across the new multi-module architecture.
