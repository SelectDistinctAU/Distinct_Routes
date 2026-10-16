# Approved Implementation Plan — Theme Mode Selector, Settings Grouping & Duplication Fix

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Activate custom Pine Green brand palette by default, add an in-app Theme Selector (Light / Dark Night / System), eliminate vehicle title duplication on Route Planner, and re-organize Settings into 3 logical categories.

## User Review Required

> [!IMPORTANT]
> **Pine Green Theme Activation:** Disabling wallpaper dynamic color by default activates the custom Emerald/Pine Green palette (`#1B5E20`) across buttons, CTAs, cards, and navigation bars.

> [!NOTE]
> **In-App Theme Control:** Users can select between Light (Emerald Pine), Dark (Pine Night), or System Default theme in Settings.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Theme_Selector_&_Settings_Grouping.md` upon approval.

---

### Theme & Preferences Layer

#### [MODIFY] [PreferencesStorage.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/local/PreferencesStorage.kt)
- Add `getThemeMode(): String` and `saveThemeMode(mode: String)` ("system", "light", "dark").

#### [MODIFY] [Theme.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/theme/Theme.kt)
- Set `dynamicColor = false` by default so the Pine Green brand palette is active, and support explicit dark/light overrides.

---

### UI Layer

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Remove duplicate vehicle title from `Safety & Riding Preferences` accordion subtitle, displaying active safety flags instead (*"Avoids Highways • Prefer Green Routes"*).

#### [MODIFY] [SettingsScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/settings/SettingsScreen.kt) & [SettingsViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/settings/SettingsViewModel.kt)
- Add Theme Mode selector (System / Light Pine / Dark Night).
- Group Settings into 3 logical categories:
  1. **Route & Vehicle Defaults** (Default City, Default Vehicle Profile)
  2. **Appearance & Safety Defaults** (Theme Mode, Safety Toggles)
  3. **About & Privacy** (Mission Statement, Privacy Statement, Attributions, License)

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Verify the Pine Green primary palette renders clearly across cards, CTAs, and tab bars in the emulator.
- Test switching Theme Mode (Light / Dark Night / System) in Settings.
- Verify vehicle subtitle duplication is removed from Route Planner.
