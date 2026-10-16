# Approved Implementation Plan — Dual Brand Color Theme System & "Saved to Favourites" Icon Fix

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Implement a runtime Dual Brand Color System (Metallic Blue `#293F6A` vs Emerald Green `#1B5E20`) in Settings, and fix the "Saved to Favourites!" confirmation popup icon to use `map_pin_heart_24px`.

## User Review Required

> [!IMPORTANT]
> **Runtime Color Scheme Swapping:** Switching between Metallic Blue and Emerald Green updates the entire app UI instantly without needing an activity restart.

> [!NOTE]
> **Confirmation Popup Icon:** The "Saved to Favourites!" confirmation toast will display `map_pin_heart_24px` instead of any legacy app launcher icon.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Dual_Color_Theme_System.md` upon approval.

---

### Local Preferences & Theme System

#### [MODIFY] [PreferencesStorage.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/local/PreferencesStorage.kt)
- Add `getColourMode(): String` and `saveColourMode(mode: String)` ("blue" default, "green").

#### [MODIFY] [Color.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/theme/Color.kt)
- Define full Material 3 Light/Dark color schemes for both Metallic Blue and Emerald Green palettes with complete surface container ladders.

#### [MODIFY] [Theme.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/theme/Theme.kt)
- Update `DistinctRoutesTheme` to accept `colourMode: String` and `themeMode: String`, dynamically resolving the active `ColorScheme`.

---

### UI & Settings Layer

#### [MODIFY] [SettingsViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/settings/SettingsViewModel.kt)
- Expose `colourMode: StateFlow<String>` and `updateColourMode(mode: String)`.

#### [MODIFY] [SettingsScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/settings/SettingsScreen.kt)
- Add **"Brand Color Palette"** selector card under **2. Appearance & Safety Defaults** with **Metallic Blue** and **Emerald Green** option chips.

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Update "Saved to Favourites!" confirmation toast/snackbar to render `R.drawable.map_pin_heart_24px`.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/MainActivity.kt)
- Pass `colourMode` from `SettingsViewModel` into `DistinctRoutesTheme`.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify project compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Test toggling between Metallic Blue and Emerald Green on the Settings screen in the emulator.
- Test saving a route to Favourites and verifying the "Saved to Favourites!" popup displays `map_pin_heart_24px`.
