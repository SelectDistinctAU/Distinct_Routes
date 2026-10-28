# Approved Implementation Plan — Address Search, Map Pin Placement & Place Display Overhaul

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Implement explicit Magnifying Glass search buttons, interactive "Drop Pin on Map" placement mode, explicit "Calculate Route Now" buttons, and clean human-readable place name formatting (eliminating raw lat/lon coordinate strings).

## User Review Required

> [!IMPORTANT]
> **Explicit Search & Interactive Pin Placement:** Address inputs now feature an explicit **Magnifying Glass Search Button (`🔍`)** to trigger lookups instantly, plus a **"Drop Pin on Map" Button (`📍`)** that collapses inputs to let riders drag and place pins directly on the full-screen map canvas.

> [!NOTE]
> **Clean Place Names (Zero Coordinates):** Address fields and route summary banners will always display clean street and suburb names (e.g. *"Clinton Avenue, St James"* or *"Boorloo Bridge Shared Path"*), completely eliminating raw coordinate strings like `"Location near (-32.0474, 115.8536)"`.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Address_Search_&_Pin_Placement_Overhaul.md` upon approval.

---

### UI & Address Selection Layer

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Add **Magnifying Glass Search Button (`Icons.Default.Search`)** to Start, Via, and Destination input rows.
- Add **"Drop Pin on Map" Button (`Icons.Default.PinDrop`)** allowing riders to collapse inputs and drag pins directly on the map.
- Add an explicit **"Calculate Route Now"** button on the address card as well as the floating CTA bar.

---

### Geocoding & Place Name Formatting Layer

#### [MODIFY] [GeocodingUseCase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/usecase/GeocodingUseCase.kt)
- Enhance `reverseGeocode` to resolve street names and suburbs from OpenStreetMap POIs, replacing raw `"Location near (-32.0474, 115.8536)"` strings with clean, human-readable place names.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify project compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Test tapping the Magnifying Glass search button on the Start and Destination fields on the emulator.
- Test tapping "Drop Pin on Map" and dragging the green/red pins on the full-screen map canvas.
- Verify address input fields display clean street names without raw coordinates.
