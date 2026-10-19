# Approved Implementation Plan — Full-Screen Map Canvas UI, Short Place Name Formatting & Perth Safety Calibration

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Transform Route Planner into a 100% full-screen map canvas with floating overlay cards, format reverse-geocoded addresses into short human-readable place names, and calibrate routing parameters to match BBBike Perth's quiet street weighting.

## User Review Required

> [!IMPORTANT]
> **Full-Screen Map Architecture:** The interactive map now occupies 100% of the screen canvas background (`fillMaxSize()`). Single-finger panning, zooming, and pin dragging work 100% smoothly with zero gesture conflicts or page scrolling.

> [!NOTE]
> **Short Place Names:** Address reverse-geocoding strips long postal boilerplate strings and displays clean, short place names (e.g. *"Royal Perth Hospital"* instead of *"Axel-Springer Parkhaus, Zimmerstraße, Friedrichstadt, Mitte, Berlin, 10117, Deutschland"*).

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_FullScreen_Map_UI_&_Perth_Safety_Calib.md` upon approval.

---

### UI & Layout Layer

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Refactor layout to a full-screen `Box(Modifier.fillMaxSize())`:
  - **Background Layer:** 100% full-screen `MapViewContainer(Modifier.fillMaxSize())`.
  - **Top Floating Card:** Floating card anchored at `Alignment.TopCenter` containing Header Logo, Vehicle Selector, Start/End inputs, and collapsible Safety Preferences.
  - **Bottom Floating Card:** Floating card anchored at `Alignment.BottomCenter` containing "Calculate Safety-Focused Route" CTA button and Route Results.

#### [MODIFY] [MapViewContainer.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/MapViewContainer.kt)
- Support full-screen height and format pin titles/snippets with short place names.

---

### Geocoding & Address Formatting Layer

#### [MODIFY] [GeocodingUseCase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/usecase/GeocodingUseCase.kt)
- Add short address formatting helper `formatShortAddress(displayName, addressMap)` to extract concise place names (e.g. amenity/building name + road + suburb).

---

### Routing Calibration & Safety Layer

#### [MODIFY] [RoutingRequest.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/model/RoutingRequest.kt) & [BikeRouteRepository.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/repository/BikeRouteRepository.kt)
- Calibrate BBBike safety parameters (`pref_speed`, `pref_cat`, `pref_quality`, `pref_special`), prioritizing Perth cycleways, river paths, and quiet backstreets over busy arterials.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify project compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Test panning and zooming the full-screen map on the emulator with single-finger gestures.
- Verify reverse-geocoded addresses display concise place names.
- Verify Perth route calculations favor quiet backstreets and cycleways.
