# Approved Implementation Plan — Vehicle Top Positioning, Share Crash Fix & Waypoint Reordering

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Fix Share Intent crash, move Vehicle/Mobility Device selection to the top of Route Planner, style Via buttons as matching 56dp OutlinedButtons, and add waypoint reordering and swap controls.

## User Review Required

> [!IMPORTANT]
> **Vehicle First Choice:** Moving Vehicle & Mobility Device selection to the top makes vehicle choice the first interactive step, ensuring route calculations immediately reflect the chosen profile.

> [!NOTE]
> **Share Intent Crash Fix:** Ensures `Intent.createChooser` carries `FLAG_ACTIVITY_NEW_TASK` and URI grant flags so sharing GPX files works across all Android OS versions without Activity context crashes.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Vehicle_Top_Positioning_&_Share_Crash_Fix.md` upon approval.

---

### Intent & UseCase Layer

#### [MODIFY] [ExportToGPXUseCase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/usecase/ExportToGPXUseCase.kt)
- Add `FLAG_ACTIVITY_NEW_TASK` and URI permission flags to chooser intent to prevent non-Activity context crashes.

---

### UI & ViewModel Layer

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Move Vehicle / Mobility Device dropdown selector card to the very top under the header bar.
- Style "+ Add Stop Along the Way (Via)" as a 56dp `OutlinedButton` matching the shape and border of input fields.
- Add Up/Down reorder icon buttons for Via stops and a Swap Start/End button.

#### [MODIFY] [RoutePlannerViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/com/distinctroutes/app/ui/route_planner/RoutePlannerViewModel.kt)
- Implement `moveViaStopUp(index)`, `moveViaStopDown(index)`, and `swapStartAndEnd()`.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Test "Share Route" button on emulator to verify Android share sheet opens cleanly.
- Verify Vehicle Selection card is at the top of Route Planner.
- Verify Via buttons match input field height and test reordering waypoints.
