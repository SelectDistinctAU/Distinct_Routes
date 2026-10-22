# SKULLINATOR PROTOCOL AUDIT LOG — DISTINCT ROUTES

**Version:** 0.2.0-alpha  
**Date:** 2026-10-11  
**Protocol Source:** [Skullinator.md](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Skullinator.md)  
**Scope:** Verification of recent architecture changes (BBBike REST API v0.2, Jetpack Room Migration, Full-Screen Map UI, LocationHelper, Geocoding).

---

## Skullinator Interrogation Analysis

### 1. What happens if this runs twice?
* **Room Database Migration (`StorageMigrationHelper.kt`):**
  * *Analysis:* Executing migration multiple times could cause duplicate route records.
  * *Control:* Checks `prefs.getBoolean("key_room_migrated", false)`. If `true`, returns immediately (0ms no-op). If `false`, migrates records and sets `key_room_migrated = true` atomically.
* **BBBike API Route Calculation (`BikeRouteRepository.kt`):**
  * *Analysis:* Repeating identical queries triggers unnecessary network bandwidth and API load.
  * *Control:* `getRoute()` checks `isFreshCache()`. If cache is <24 hours old, returns cached `RoutingResponse` instantly without hitting `api.bbbike.org` twice.

---

### 2. Where else does this value live? Is it synced?
* **Vehicle Profile & Safety Preferences (`PreferencesStorage` vs `SafetyPreferences`):**
  * *Analysis:* `selectedCity`, `bikeType`, `preferResidentialRoads`, and `useFerries` live in `PreferencesStorage` and ViewModels.
  * *Control:* `PreferencesStorage` is the single source of truth. All ViewModels (`SettingsViewModel`, `RoutePlannerViewModel`) observe updates via `StateFlow`.

---

### 3. What breaks if [specific system assumption] changes?
* **BBBike API Response Schema Assumption:**
  * *Analysis:* What if `api.bbbike.org` changes response format from `Route.Points` to GeoJSON `features` or OSRM `routes`?
  * *Control:* `parseJsonRoutingResponse` in [BikeRouteRepository.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/repository/BikeRouteRepository.kt) evaluates all 3 schemas sequentially (`Route.Points` → `features.geometry.coordinates` → `routes[0].geometry.coordinates`). If all 3 fail, it falls back gracefully to `createEstimatedRoutingResponse` without crashing.

---

### 4. Who else depends on this output format staying exactly as-is?
* **GPX Export & Navigation Handoff (`ExportToGPXUseCase.kt`):**
  * *Analysis:* OsmAnd, Organic Maps, and Garmin Connect depend on standard GPX 1.1 XML structure (`<trk><trkseg><trkpt lat="..." lon="...">`).
  * *Control:* `ExportToGPXUseCase` generates standard GPX 1.1 XML schema with WGS84 coordinates.

---

### 5. What happens on the first run, and on the run where the source is empty?
* **First Run / Empty Input Fields (`RoutePlannerViewModel.kt`):**
  * *Analysis:* Tapping "Calculate Safety-Focused Route" on first launch when inputs are empty.
  * *Control:* `calculateRoute()` validates `startAddressText.value.isBlank() || endAddressText.value.isBlank()`, displaying error banner *"Error: Please enter both a Start and Destination address."* without initiating network requests.

---

### 6. If this fails at 2am with nobody watching, what does anyone actually learn?
* **Logcat & Request Transparency (`BikeRouteRepository.kt` & `RoutePlannerScreen.kt`):**
  * *Analysis:* Diagnosing API failures in production.
  * *Control:* Logcat logs `I/DistinctRoutes: BBBike API Request URL: https://api.bbbike.org/api/0.2/...` and `E/DistinctRoutes: BBBike API exception: [error]`. The result card includes an expandable **"BBBike API Query Inspector"** button showing the exact URL sent to BBBike.

---

## Non-Negotiable Compliance Verification

1. **No new list of values without checking existing ones first:** Reused `BikeType` enum and `Constants.SUPPORTED_LOCALES`.
2. **Every change ships with documentation of why, not just what:** Documented in `CHANGELOG.md`, `BUG_REGISTER.md`, and `FEATURE_REQUEST_LOG.md`.
3. **If an assumption isn't written down, it doesn't exist:** Documented in [TECHNICAL_ARCHITECTURE.md](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/02_Specifications/TECHNICAL_ARCHITECTURE.md) and [SKULLINATOR_AUDIT_LOG.md](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/01_Governance/SKULLINATOR_AUDIT_LOG.md).
