# Approved Implementation Plan — Ride Journal History & Privacy Policy Dialog

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Add on-device local Ride Journal / Route History (automatically logging calculated routes) with a dual Favourites/Journal tab view, and an in-app Privacy Policy dialog.

## User Review Required

> [!IMPORTANT]
> **On-Device Ride Journal:** Every route calculated in Route Planner is automatically logged to a local, on-device Ride Journal history log (zero cloud storage, zero tracking). Riders can review, reload, or clear past routes anytime in the Favourites & Journal tab.

> [!NOTE]
> **In-App Privacy Statement:** Adds an in-app "Privacy Policy" dialog in Settings confirming 100% local storage, zero telemetry, and GPL-3.0 open-source transparency.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Ride_Journal_&_Privacy_Policy.md` upon approval.

---

### Data & Local Storage Layer

#### [NEW] [JournalStorage.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/local/JournalStorage.kt)
- Create local storage helper to automatically save and manage calculated ride history entries.

---

### Domain & ViewModel Layer

#### [MODIFY] [FavoritesViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/favorites/FavoritesViewModel.kt)
- Expose `journalHistory: StateFlow<List<FavoriteRoute>>`, `logRideHistory(route)`, and `clearJournalHistory()`.

#### [MODIFY] [RoutePlannerViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/com/distinctroutes/app/ui/route_planner/RoutePlannerViewModel.kt)
- Automatically call `favoritesViewModel.logRideHistory(...)` whenever a route calculation succeeds.

---

### UI Layer

#### [MODIFY] [FavoritesScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/favorites/FavoritesScreen.kt)
- Add a tab toggle row: `[ Favourites ]` and `[ Ride Journal ]`.
- Render chronological ride history log with 1-tap "Reload into Planner", "Share", and "Clear History" options.

#### [MODIFY] [SettingsScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/settings/SettingsScreen.kt)
- Add an in-app "Privacy Policy" button opening a dialog explaining zero tracking, local storage, and open-source license.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify project compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Calculate a route in Route Planner and verify it automatically appears in the Ride Journal history tab.
- Test reloading a route from Ride Journal back into Route Planner.
- Verify the Privacy Policy dialog on the Settings screen.
