# Approved Implementation Plan — Official BBBike REST API v0.2 Integration & Request Inspector

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Connect Distinct Routes directly to BBBike.org's official REST API v0.2 (`https://api.bbbike.org/api/0.2/{city}/`) with `startc_wgs84`, `zielc_wgs84`, `pref_cat=N1` (Prefer side streets), `pref_seen=1`, and `appid=guest`, delivering 100% route safety parity with BBBike.org across all supported cities, and add Logcat/UI request parameter inspection.

## User Review Required

> [!IMPORTANT]
> **Official BBBike API v0.2 Integration:** Requests are routed directly to `https://api.bbbike.org/api/0.2/{city}/` using BBBike's exact query format (`startc_wgs84=lon,lat`, `zielc_wgs84=lon,lat`, `pref_cat=N1` prefer side streets, `pref_seen=1`), applying BBBike's native safety graph weights across all cities.

> [!NOTE]
> **Request Transparency Inspector:** Every calculation logs the exact outgoing BBBike API URL to Logcat and displays an expandable request inspector on the route result card so you can review the exact parameters sent to BBBike.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Official_BBBike_API_v02_Integration.md` upon approval.

---

### API & Repository Layer

#### [MODIFY] [BBBikeApiService.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/api/BBBikeApiService.kt)
- Define official BBBike API v0.2 method `getBbbikeRoute(...)` targeting `https://api.bbbike.org/api/0.2/{city}/`.

#### [MODIFY] [NetworkModule.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/di/NetworkModule.kt)
- Provide Retrofit instance pointing to `https://api.bbbike.org/`.

#### [MODIFY] [BikeRouteRepository.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/repository/BikeRouteRepository.kt)
- Execute requests against `api.bbbike.org/api/0.2/{city}/`, log the full HTTP GET URL to Logcat (`D/DistinctRoutes: BBBike API Request: ...`), and parse waypoints.

---

### UI Layer

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Add an expandable **"BBBike API Query Inspector"** button/card on the route result card displaying the exact URL parameters sent to BBBike.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify project compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Calculate a Perth route on the emulator.
- Inspect Logcat (`D/DistinctRoutes: BBBike API Request: ...`) to verify `startc_wgs84`, `zielc_wgs84`, `pref_cat=N1`, `pref_seen=1` are sent to `api.bbbike.org`.
- Verify the route on the map uses quiet residential roads and Swan River shared paths, matching `bbbike.org/Perth/`.
