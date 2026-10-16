# Approved Implementation Plan — UX Redesign, Map Pin Dropping, GPS & Mobility Devices

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Redesign the Route Planner interface to replace raw coordinate inputs with Address Search, GPS Current Location detection, Accessible Map Pin Dropping (Start Flag A / Finish Flag B), Collapsible Preferences, and Wheelchair/Mobility Scooter support.

## User Review Required

> [!IMPORTANT]
> **Accessibility & Color-Vision Safety:** Map pin markers use distinct shape overlays ("Start Flag A" and "Finish Flag B") rather than red/green color differentiation, complying with WCAG AA guidelines and accessible design.

> [!NOTE]
> **Geocoding Service:** OpenStreetMap Nominatim API (`nominatim.openstreetmap.org`) will be used for forward/reverse address lookup with standard user-agent attribution.

## Open Questions

None.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_UX_Redesign_&_Mobility_Devices.md` upon approval.

---

### Domain Layer

#### [MODIFY] [SafetyPreferences.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/model/SafetyPreferences.kt)
- Expand `enum class BikeType`:
  - `STANDARD` ("Standard Bike", "bike")
  - `MOUNTAIN` ("Mountain Bike", "mtb")
  - `CARGO` ("Cargo / E-Bike", "cargo")
  - `TANDEM` ("Tandem Bike", "cargo")
  - `WHEELCHAIR` ("Wheelchair / Handbike", "handbike")
  - `MOBILITY_SCOOTER` ("Mobility Scooter", "cargo")
- Add helper `isMobilityDevice()` enforcing automatic steep hill and highway avoidance.

#### [NEW] [GeocodingUseCase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/usecase/GeocodingUseCase.kt)
- Implement forward address geocoding (search query -> coordinates) and reverse geocoding (coordinates -> address string).

---

### Data Layer

#### [NEW] [NominatimApiService.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/api/NominatimApiService.kt)
- Retrofit API service for Nominatim `/search` and `/reverse` geocoding endpoints.

#### [NEW] [LocationHelper.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/util/LocationHelper.kt)
- Utility helper to request location permissions and fetch current GPS coordinates.

#### [MODIFY] [NetworkModule.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/di/NetworkModule.kt)
- Add `@Provides` for Nominatim Retrofit client.

---

### UI Layer

#### [NEW] [MapViewContainer.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/MapViewContainer.kt)
- Compose `AndroidView` wrapping `osmdroid` `MapView`.
- Draw accessible Start Flag "A" and Finish Flag "B" markers with pin drop/drag capabilities.

#### [MODIFY] [RoutePlannerViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerViewModel.kt)
- Manage Start/End address text, geocoding suggestions, current GPS location status, and collapsible preferences state.

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Redesign layout:
  - Top header summary chip for selected city.
  - "From" address search + "GPS / Current Location" + "Pin on Map" buttons.
  - "To" address search + "Pin on Map" button.
  - Collapsible "Safety & Riding Preferences" card with Wheelchair & Mobility Scooter options.
  - Embedded `osmdroid` interactive map view.
  - TalkBack `contentDescription` on all controls.

#### [MODIFY] [strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values/strings.xml)
- Add localized strings for Wheelchair, Mobility Scooter, GPS permissions, and accessibility descriptions.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Verify address geocoding search and GPS location permission dialog.
- Verify pin dragging on `osmdroid` map.
- Test Wheelchair and Mobility Scooter profile selection and hill avoidance logic.
- Verify TalkBack screen reader labels on controls.
