# Approved Implementation Plan — Via Cache Fix, Button Overflow Fix & Navigation Handoff Clarification

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Fix the Via route cache key bug so 3-point routes pass through the Via stop, fix the "Save to Favourites" button text overflow on result cards, and clarify turn-by-turn navigation export/handoff with a standard Android share sheet.

## User Review Required

> [!IMPORTANT]
> **Via Cache Bug Fix:** `generateCacheKey` now includes `request.viaParam()`, preventing cached 2-point direct routes from overriding 3-point Via routes.

> [!NOTE]
> **Navigation Handoff Clarification:** Clarified "OsmAnd" button to "Export GPX / Navigate" with Android system share sheet, allowing users to export GPX to OsmAnd, Organic Maps, Garmin, or local storage.

## Proposed Changes

### Data & Repository Layer

#### [MODIFY] [BikeRouteRepository.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/repository/BikeRouteRepository.kt)
- Update `generateCacheKey` to include `${request.viaParam()}` and `${request.hasViaPoint}`, ensuring 3-point Via routes generate distinct cache keys.

---

### UI & Result Card Layer

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Fix button row layout in route result card:
  - Use `maxLines = 1` and `TextOverflow.Ellipsis` for "Save to Favourites" button to prevent 2-line vertical text clipping.
  - Relabel "OsmAnd" button to "Export GPX / Navigate" and launch Android Intent chooser (allowing OsmAnd handoff or GPX file sharing).

#### [MODIFY] [FavoritesScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/favorites/FavoritesScreen.kt)
- Ensure saved favourite routes with Via stops display full Via address details and load correctly into Route Planner.

#### [MODIFY] [strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values/strings.xml)
- Add "Export GPX / Navigate" button label string across all localized string files.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Test calculating a route with a Via stop and verify the polyline explicitly bends to pass through the Via pin.
- Verify "Save to Favourites" button fits cleanly without text overflow or clipping.
- Verify "Export GPX / Navigate" button opens the Android share sheet for GPX export/handoff.
