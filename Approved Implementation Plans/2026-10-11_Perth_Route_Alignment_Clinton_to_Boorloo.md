# Approved Implementation Plan — Perth Safety Route Alignment (Clinton Ave → Boorloo Bridge)

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Align and test the Perth cycling route calculation from Clinton Avenue to Boorloo Bridge Shared Path using BBBike's official REST API v0.2 (`https://api.bbbike.org/api/0.2/{city}/`), verifying safety adherence (arterial road avoidance & distance tolerance) under the Testing Accountability Protocol.

## User Review Required & Four Approval Conditions

> [!IMPORTANT]
> **1. Verified BBBike API v0.2 Parameters:**
> * Documented in [docs/bbbike_api_reference.md](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/docs/bbbike_api_reference.md).
> * Endpoint: `https://api.bbbike.org/api/0.2/{city}/`
> * Parameters: `appid=guest`, `startc_wgs84=lon,lat` (longitude first!), `zielc_wgs84=lon,lat` (longitude first!), `pref_seen=1` (mandatory), `pref_cat=N1` (side streets), `pref_speed=20`, `pref_ampel=yes`, `pref_unlit=NL`, `output_as=gpx-route`.

> [!IMPORTANT]
> **2. Revised PASS/FAIL Safety Adherence Criteria:**
> * **FAIL (any of these):**
>   - Route includes: **Berwick Street**, **Jarrah Road**, **Shepperton Road**, or **Albany Highway**.
>   - Total distance > 7.0 km (gross detour beyond safe zone).
>   - App crashes or fails to calculate route.
> * **PASS (all of these):**
>   - Zero arterial road violations detected.
>   - Total distance between **5.0 km and 7.0 km** (allows algorithmic variation).
>   - Visual inspection confirms residential backstreet routing.
>   - GPX export validates waypoint locations.
> * **NO LONGER REQUIRED:** Exact 16-waypoint match or specific street sequence from BBBike.

> [!NOTE]
> **3. Distance Tolerance & Offline Fixture Unit Test:**
> * Unit test [PerthSafetyRouteTest.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/test/java/com/distinctroutes/app/data/repository/PerthSafetyRouteTest.kt) asserts:
>   `assertTrue(route.distanceKm >= 5.0 && route.distanceKm <= 7.0)`
>   `assertFalse(route.containsArterial(ARTERIAL_ROADS))`
> * Uses offline test fixture `src/test/resources/bbbike-perth-route.gpx` for deterministic execution without network flakes.

> [!NOTE]
> **4. File Existence Check & Evidence Deliverables:**
> * Confirmed existence of `BBBikeApiService.kt` and `BikeRouteRepository.kt`.
> * Deliverables: `docs/bbbike_api_reference.md`, `PerthSafetyRouteTest` unit test log, emulator screenshots (`ui_state`), GPX export files, and updated `01_Governance/SKULLINATOR_AUDIT_LOG.md`.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Perth_Route_Alignment_Clinton_to_Boorloo.md` upon approval.

#### [NEW] [docs/bbbike_api_reference.md](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/docs/bbbike_api_reference.md)
- Complete technical reference for BBBike REST API v0.2 endpoints, parameters, and response schemas.

---

### Routing & API Calibration Layer

#### [MODIFY] [BBBikeApiService.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/api/BBBikeApiService.kt)
- Update `calculateBbbikeRoute` to send `appid=guest`, `startc_wgs84=lon,lat`, `zielc_wgs84=lon,lat`, `pref_seen=1`, `pref_cat=N1`, `pref_speed=20`, `output_as=json`.

#### [MODIFY] [BikeRouteRepository.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/repository/BikeRouteRepository.kt)
- Execute requests against `api.bbbike.org/api/0.2/{city}/` with `lon,lat` order, parse waypoints, and assert no segments intersect arterial roads.

---

### Verification & Automated Testing Layer

#### [NEW] [PerthSafetyRouteTest.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/test/java/com/distinctroutes/app/data/repository/PerthSafetyRouteTest.kt)
- Automated unit test evaluating route safety adherence:
  - Distance between 5.0 km and 7.0 km.
  - Zero intersections with Berwick St, Jarrah Rd, Shepperton Rd, or Albany Hwy.

---

## Verification Plan

### Automated Tests
- Run `./gradlew testDebugUnitTest --tests com.distinctroutes.app.data.repository.PerthSafetyRouteTest` to verify 100% pass on safety adherence criteria.
- Run `./gradlew assembleDebug` to verify compilation.

### Manual Emulator Verification
- Deploy app to `emulator-5554`.
- Input Start: `"Clinton Avenue"` and Destination: `"Boorloo Bridge Shared Path"`.
- Calculate route and capture screenshot via `ui_state`.
- Verify the rendered blue polyline uses quiet residential streets (5.0–7.0 km) without touching Shepperton Rd, Berwick St, Jarrah Rd, or Albany Hwy.
