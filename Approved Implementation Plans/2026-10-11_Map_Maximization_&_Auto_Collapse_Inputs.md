# Approved Implementation Plan — Auto-Collapsing Search Inputs & Map Maximization

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Automatically collapse search inputs into a 1-line top summary banner upon route calculation success, expanding the interactive map canvas to over 80% of the screen height, and calibrate Perth Principal Shared Path (PSP) route weighting.

## User Review Required

> [!IMPORTANT]
> **Auto-Collapsing Map Maximization:** Upon calculating a route, search inputs automatically collapse into a compact 1-line top banner (`"Hill View Terrace → Boorloo Bridge"`), expanding the interactive map canvas to **over 80% of the screen**.

> [!NOTE]
> **1-Tap Expand / Edit:** Tapping the top summary banner or edit button (`✎`) instantly expands the full search inputs, vehicle selector, and preferences again.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Map_Maximization_&_Auto_Collapse_Inputs.md` upon approval.

---

### UI & Layout Layer

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Add `var isTopCardCollapsed by remember { mutableStateOf(false) }`.
- Automatically trigger `isTopCardCollapsed = true` when `uiState` becomes `RoutePlannerUiState.Success`.
- When collapsed, render a compact 1-line top glassmorphic summary bar:
  `[Logo] "Hill View Terrace → Boorloo Bridge" [Edit ✎]`
- Streamline bottom result card into a compact action pill row (`Save` • `Share` • `Navigate`).

---

### Safety Routing Calibration Layer

#### [MODIFY] [CalculateRouteUseCase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/com/distinctroutes/app/domain/usecase/CalculateRouteUseCase.kt)
- Enforce Perth Principal Shared Path (PSP) and river path weighting when `preferGreenRoutes` or `avoidHighways` is enabled.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify project compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Calculate a route on the emulator and verify that top search inputs collapse into a 1-line banner, expanding the map to 80%+ of the screen.
- Tap "Edit ✎" on the top banner and verify full input controls expand smoothly.
- Verify Perth route calculations favor shared paths along the Swan River.
