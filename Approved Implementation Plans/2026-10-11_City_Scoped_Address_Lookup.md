# Approved Implementation Plan — City-Scoped Address Lookup & Top Card Scrollability Fix

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Scope all address search lookups in input fields directly to the user's selected BBBike city (e.g., Perth, Berlin, London), and fix top card scrollability so expanded safety preference toggles never get covered by the bottom CTA button.

## User Review Required

> [!IMPORTANT]
> **City-Bounded Search:** Address queries are automatically framed with the active city context (e.g. searching `"122, Perth, WA"` instead of unconstrained global query `"122"`), guaranteeing all returned address suggestions match the local riding region.

> [!NOTE]
> **Top Overlay Scrollability:** The top floating card gets a clean vertical scroll modifier with max-height clamping, ensuring all 8 safety toggles remain 100% accessible when the preferences card is expanded.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_City_Scoped_Address_Lookup.md` upon approval.

---

### UI & Layout Layer

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Add vertical scrolling / weight bounds to the top floating card column so expanded safety toggles scroll smoothly above the bottom CTA button.

---

### Geocoding & Address Lookup Layer

#### [MODIFY] [GeocodingUseCase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/usecase/GeocodingUseCase.kt)
- Update `searchAddress(query: String, cityContext: String?)`:
  - Append `cityContext` to address lookup queries when not already present in the search string (e.g., `"122"` → `"122, Perth"`).
  - Filter returned location suggestions to ensure they belong to the expected city/region.

#### [MODIFY] [RoutePlannerViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/com/distinctroutes/app/ui/route_planner/RoutePlannerViewModel.kt)
- Pass `selectedCity` (from `PreferencesStorage`) into `geocodingUseCase.searchAddress(query, cityContext)`.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify project compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Type `"122"` or street names into the Start Address field on the emulator with Perth selected as the active city.
- Verify 100% of returned suggestions are in Perth, WA (e.g., *"122 St Georges Terrace, Perth, WA"*).
- Expand Safety Preferences card on the emulator and verify all 8 toggles scroll cleanly without being obscured by the bottom CTA button.
