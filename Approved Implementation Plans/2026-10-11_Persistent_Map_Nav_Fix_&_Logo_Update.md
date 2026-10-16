# Approved Implementation Plan — Persistent Map Navigation Fix, Dropdown UX, Green Routes & Logo Update

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Fix Favourites/Route Planner tab freeze bug by preserving map composition state, update `AndroidManifest.xml` to `@drawable/direct_route`, fix double `++` text in Via button, make Default Profile an intuitive full-field dropdown, and add Green Routes, Unlit Streets, and Surface preference toggles.

## User Review Required

> [!IMPORTANT]
> **Permanent Favourites Freeze Fix:** `MainActivity.kt` will preserve `RoutePlannerScreen` composition state across tab switches, eliminating `MapView` thread re-allocation leaks and guaranteeing instant 0ms tab transitions with zero freezes.

> [!NOTE]
> **Logo Reference Update:** `AndroidManifest.xml` will be updated to `@drawable/direct_route` (removing stale `direct_route3` references).

## Proposed Changes

### Navigation & Tab Freeze Fix

#### [MODIFY] [MainActivity.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/MainActivity.kt)
- Preserve `RoutePlannerScreen` composition state using tab visibility, preventing `MapView` reconstruction overhead and thread leaks.

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/AndroidManifest.xml)
- Update launcher icon to `@drawable/direct_route` (`android:icon` and `android:roundIcon`).

#### [MODIFY] [MapViewContainer.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/MapViewContainer.kt)
- Ensure clean non-blocking map pause/resume lifecycle handling.

---

### UI & UX Cleanups

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Remove duplicate `+` prefix from Via button text (fixing `+ + Add Stop`).
- Add Green Routes, Unlit Streets, Cobblestone, and Ferry safety toggles to preferences card.

#### [MODIFY] [SettingsScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/settings/SettingsScreen.kt)
- Make "Default Vehicle / Mobility Device" text field and entire container box clickable (`Modifier.clickable`), with clear `ArrowDropDown` icon.
- Add Green Routes, Unlit Streets, and Surface safety preference default toggles.

---

### Safety Preferences & Routing Engine Layer

#### [MODIFY] [SafetyPreferences.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/model/SafetyPreferences.kt)
- Add boolean flags: `preferGreenRoutes`, `avoidUnlitStreets`, `avoidCobblestones`, `avoidFerries`.

#### [MODIFY] [RoutingRequest.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/model/RoutingRequest.kt) & [CalculateRouteUseCase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/usecase/CalculateRouteUseCase.kt)
- Incorporate green routes, unlit streets, cobblestone surface, and ferry avoidances into routing parameters and safety summary text.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify project compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Test switching repeatedly between Favourites, Settings, and Route Planner to verify instant tab transitions with zero freezes.
- Verify Default Profile dropdown opens by tapping anywhere on the input field in Settings.
- Verify Via button displays a single `+` icon.
- Verify Green Routes and Surface toggles affect route summary and calculations.
