# Approved Implementation Plan — Multi-Stop Trails, Dynamic City List, Icon Updates & Tab Crash Fix

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Fix tab switching crash, update to new `direct_route` logo, replace Favourites icon with clean heart icon, support multiple intermediate Via stops (for loops/trails), add Perth & dynamic BBBike city fetching, and remove Magnific Flaticon attribution.

## User Review Required

> [!IMPORTANT]
> **Dynamic City List:** The app will fetch the live list of supported cities from `https://www.bbbike.org/` at runtime, falling back to an expanded built-in list (including Perth, WA) when offline.

> [!NOTE]
> **Multi-Stop Trail Creation:** Users can now add multiple Via stops ("Via 1", "Via 2", "Via 3") to create custom trail loops and multi-stop journeys.

## Proposed Changes

### Bug Fix & Navigation Layer

#### [MODIFY] [MapViewContainer.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/MapViewContainer.kt)
- Fix tab navigation crash by managing `MapView` instance lifecycle without blocking on main thread calls.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/com/distinctroutes/app/MainActivity.kt)
- Update `AppDestinations` to use `R.drawable.direct_route` for Route Planner and clean Material `Icons.Default.Favorite` for Favourites.

---

### Icons & Branding Layer

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt) & [SettingsScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/settings/SettingsScreen.kt)
- Use new `direct_route` logo with stronger colors and cleaner background.
- Update "Save to Favourites" button on result card to display clean Heart icon (`Icons.Default.Favorite` / `FavoriteBorder`).
- Remove Magnific Flaticon attribution from Settings screen and `README.md`.

---

### Multi-Stop Via Waypoints (Loops & Trails)

#### [MODIFY] [RoutingRequest.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/model/RoutingRequest.kt) & [CalculateRouteUseCase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/usecase/CalculateRouteUseCase.kt)
- Support a list of Via stop coordinates (`viaPoints: List<Pair<Double, Double>>`) formatted into OSRM coordinate strings:
  `startLon,startLat;via1Lon,via1Lat;via2Lon,via2Lat;...;endLon,endLat`

#### [MODIFY] [RoutePlannerViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/com/distinctroutes/app/ui/route_planner/RoutePlannerViewModel.kt) & [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Support adding/removing multiple Via stops dynamically (e.g. "+ Add Another Stop").

---

### Dynamic BBBike City List (including Perth, WA)

#### [MODIFY] [BBBikeApiService.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/api/BBBikeApiService.kt) & [BikeRouteRepository.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/repository/BikeRouteRepository.kt)
- Fetch live list of supported cities from BBBike server or fallback to expanded city list including Perth, Sydney, Melbourne, Brisbane, Adelaide, Canberra, Berlin, London, Paris, etc.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Test switching between Route Planner, Favourites, and Settings tabs to verify zero crashes.
- Test adding multiple Via stops and calculating a loop/trail route.
- Verify Perth, WA appears in the city selector.
