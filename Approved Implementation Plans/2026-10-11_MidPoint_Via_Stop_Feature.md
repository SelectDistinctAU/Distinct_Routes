# Approved Implementation Plan — Mid Point Via / Stop along the way

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Add support for an optional Mid Point Via / Stop along the way between Start (From) and Destination (To) in the route planner, domain model, API request, and interactive map view.

## User Review Required

> [!IMPORTANT]
> **BBBike Authentic Marker Styling:** The Via stop uses BBBike's classic Amber Yellow Pin (`#F57F17`) with a solid Yellow Dot (`●`) inside a white circle, providing visual harmony with Green Play (Start) and Red Stop (Destination) markers.

> [!NOTE]
> **3-Point Routing:** The OSRM biking API will receive 3 coordinate points (`{startLon},{startLat};{viaLon},{viaLat};{endLon},{endLat}`), routing the polyline through the Via stop.

## Open Questions

None.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_MidPoint_Via_Stop_Feature.md` upon approval.

---

### Data Layer

#### [MODIFY] [RoutingRequest.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/model/RoutingRequest.kt)
- Add fields `viaLat: Double? = null`, `viaLon: Double? = null`, `hasViaPoint: Boolean = false`.
- Add `formattedCoordinatesParam()` returning 2-point or 3-point coordinate strings for OSRM API.

#### [MODIFY] [BikeRouteRepository.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/repository/BikeRouteRepository.kt)
- Pass `request.formattedCoordinatesParam()` to API request.

---

### Domain Layer

#### [MODIFY] [CalculateRouteUseCase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/usecase/CalculateRouteUseCase.kt)
- Accept optional `viaLat`, `viaLon`, `hasViaPoint` parameters.
- Incorporate Via stop into route calculation and summary description.

---

### UI Layer

#### [MODIFY] [MapViewContainer.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/MapViewContainer.kt)
- Add parameters `viaLat: Double?`, `viaLon: Double?`, `hasViaPoint: Boolean`, and `onViaPinMoved: (Double, Double) -> Unit`.
- Create `createBbbikeViaPin(context)` drawing BBBike's Yellow Pin (`#F57F17`) with a Yellow Dot (`●`).
- Add draggable `viaMarker` overlay to `osmdroid` MapView when `hasViaPoint` is true.

#### [MODIFY] [RoutePlannerViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerViewModel.kt)
- Manage state for `hasViaPoint`, `viaAddressText`, `viaSuggestions`, `viaLat`, `viaLon`, `toggleViaPoint()`, and `onViaPinMoved(lat, lon)`.

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Add "+ Add Stop along the way" button / expandable Via address field with remove stop option.
- Pass Via parameters and callbacks to `MapViewContainer`.

#### [MODIFY] [strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values/strings.xml)
- Add localized strings for Via stop input, add/remove stop buttons, and Via pin accessibility labels.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Test adding and removing a Via stop in the UI.
- Test address geocoding search for Via stop.
- Verify Yellow Via Pin ● on `osmdroid` map and route polyline passing through the Via stop.
- Verify TalkBack accessibility descriptions for Via stop.
