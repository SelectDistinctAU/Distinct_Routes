# Approved Implementation Plan — Screen Space Optimization & Compact Via Stop Layout

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Streamline screen space on the Route Planner screen by removing redundant section headers, integrating Current GPS location detection directly into the Start input field, and compacting Via stop inputs with inline delete icons.

## User Review Required

> [!IMPORTANT]
> **Total Screen Space Savings:** Eliminating redundant From/To section headers, the full-width GPS button block, and Via stop title rows saves over **180dp of vertical height**, maximizing map view visibility!

> [!NOTE]
> **Compact Via Stop Design:** Each Via stop is rendered as a single compact `OutlinedTextField` with a floating label (*"Via Stop #1 Address"*) and a trailing delete icon (`IconButton(Icons.Default.Close)`), removing all separate header rows.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Screen_Space_Optimization_&_GPS_Placement.md` upon approval.

---

### UI Layer

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Remove `Text("From (Origin)")` and `Text("To (Destination)")` section headers.
- Remove standalone "Current GPS" button block under Start input field.
- Add GPS target icon button (`Icons.Default.MyLocation`) directly inside the `OutlinedTextField` trailing icon for the Start input field.
- Remove separate `Via Stop #1` header rows and `Remove Stop` text buttons.
- Render Via stops with inline `trailingIcon = { IconButton(onClick = { removeViaStop(id) }) { Icon(Icons.Default.Close) } }`.

#### [MODIFY] [RoutePlannerViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/com/distinctroutes/app/ui/route_planner/RoutePlannerViewModel.kt)
- Ensure `useCurrentGpsLocation()` updates start coordinates, reverse geocodes the address, and re-centers the map view on live GPS coordinates.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Inspect `RoutePlannerScreen` on the emulator to verify significantly increased map height and clean, compact Via stop fields.
- Test tapping the GPS target icon inside the Start Address field and delete icons next to Via stops.
