# Approved Implementation Plan — BBBike Perth CGI Safety Parameters Calibration

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Calibrate Distinct Routes' safety preferences and routing engine request parameters to match BBBike.org's native Perth CGI parameters (`pref_cat=N1` prefer residential roads, `pref_speed`, `pref_quality`, `pref_ferry=use`), delivering 100% route safety parity with BBBike.org/Perth/.

## User Review Required

> [!IMPORTANT]
> **Prefer Residential Roads (`pref_cat = "N1"`):** Setting `pref_cat="N1"` as default forces the routing engine to bypass high-speed Perth traffic arterials (e.g. Wellington St / Riverside Dr) and route cyclists through quiet residential backstreets and Principal Shared Paths (PSP).

> [!NOTE]
> **Use Ferries & Speed Calibration:** Replaces "Avoid Ferries" with "Use Ferries" (`pref_ferry=use`) and adds a Preferred Cruising Speed field (`pref_speed=20`) matching BBBike's native form controls.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_BBBike_Perth_CGI_Safety_Calibration.md` upon approval.

---

### Domain & Data Model Layer

#### [MODIFY] [SafetyPreferences.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/model/SafetyPreferences.kt)
- Add `preferResidentialRoads: String = "N1"` (`"N1"` prefer residential, `"N2"` only residential, `""` no preference).
- Change `avoidFerries: Boolean` to `useFerries: Boolean = false`.
- Add `preferredSpeedKmH: Int = 20`.
- Add `roadSurfaceQuality: String = ""` (`""` no preference, `"Q2"` avoid cobblestones/bad surfaces, `"Q0"` excellent surfaces only).

#### [MODIFY] [RoutingRequest.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/model/RoutingRequest.kt)
- Map `pref_cat`, `pref_speed`, `pref_quality`, `pref_specialvehicle`, `pref_ferry`, `pref_unlit` query parameters into BBBike OSRM/CGI requests.

---

### UI Layer

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Update Safety Preferences accordion to include **Prefer Residential Roads**, **Preferred Speed (20 km/h)**, **Road Surface Quality**, and **Use Ferries**.

#### [MODIFY] [SettingsScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/settings/SettingsScreen.kt)
- Update default safety preferences to reflect `pref_cat="N1"` and `useFerries`.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify project compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Calculate Perth route (*Hill View Terrace* to *Boorloo Bridge / Hay St*) on the emulator with `pref_cat="N1"` active.
- Verify the calculated route bypasses Wellington St and uses quiet residential streets and Swan River shared paths, matching `bbbike.org/Perth/`.
