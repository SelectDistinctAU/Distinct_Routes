# Approved Implementation Plan — Settings Screen & Bottom Navigation Tab Icon Refinement

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Build out the Settings Screen (Default City, Default Vehicle/Mobility Device, Default Safety Toggles, Attributions & Safety Disclaimer) with local persistence, and refine the bottom navigation bar tab icons.

## User Review Required

> [!IMPORTANT]
> **Persistent User Preferences:** Default city, default vehicle/mobility device, and default safety preferences selected in Settings will persist across app restarts via `SharedPreferences`.

> [!NOTE]
> **Attributions:** The Settings screen includes full credit attributions for BBBike routing engine, OpenStreetMap data, and Magnific Flaticon pin artwork.

## Open Questions

None.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Settings_Screen_&_Tab_Icon_Refinement.md` upon approval.

---

### Data & Local Storage Layer

#### [NEW] [PreferencesStorage.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/local/PreferencesStorage.kt)
- Create local `SharedPreferences` helper managing user default city, vehicle type, and safety preference toggles.

---

### UI Layer

#### [NEW] [SettingsViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/settings/SettingsViewModel.kt)
- Create Hilt `@HiltViewModel` managing settings state flow and persisting preference updates.

#### [NEW] [SettingsScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/settings/SettingsScreen.kt)
- Build Compose UI with:
  - Top Branding Bar (logo & title)
  - Default BBBike City Selector Card
  - Default Vehicle / Mobility Device Selector Card
  - Default Safety & Riding Preferences Toggles Card
  - About & Attributions Card (Version `0.1.0-alpha`, GPLv3 license, BBBike attribution, Flaticon attribution)
  - Official Safety Disclaimer Card

#### [MODIFY] [MainActivity.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/com/distinctroutes/app/MainActivity.kt)
- Connect `SettingsScreen` to `AppDestinations.PROFILE` ("Settings") tab.
- Refine navigation tab icon sizing to prevent bitmap clipping.

#### [MODIFY] [RoutePlannerViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerViewModel.kt)
- Load initial default city, vehicle type, and safety toggles from `PreferencesStorage`.

#### [MODIFY] [strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values/strings.xml)
- Add localized strings for Settings section titles, options, and attributions.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Test modifying settings in Settings screen, restarting app, and verifying persistence.
- Inspect bottom navigation bar tab icons on emulator.
