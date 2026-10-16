# Approved Implementation Plan — Tab Crash Fix, High-Contrast Icons, Multi-Language Translations & Favourites Feature

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Fix the tab navigation freeze/crash, update bottom navigation tab icons for high contrast, implement multi-language localized string resources (en-GB, en-AU, en-NZ, de, fr, it, es, pt, zh) with correct "Favourite" spellings, and implement the Saved Routes / Favourites feature.

## User Review Required

> [!IMPORTANT]
> **Tab Navigation Fix:** Resolves the `osmdroid` `MapView` detached lifecycle state bug where switching tabs away from Route Planner and returning caused a freeze/crash.

> [!NOTE]
> **Locale Specificity:** UK (`en-GB`), Australia (`en-AU`), and New Zealand (`en-NZ`) users will automatically see "Favourite" and "Favourites", while US (`en-US`) users see "Favorite".

## Open Questions

None.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Favourites_Feature_&_Translations.md` upon approval.

---

### Bug Fix & Navigation Layer

#### [MODIFY] [MapViewContainer.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/MapViewContainer.kt)
- Fix `MapView` lifecycle management so disposing when switching tabs re-creates a fresh MapView upon returning to Route Planner.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/MainActivity.kt)
- Replace low-contrast tab icons with high-contrast Material 3 vector icons (`Icons.Default.Favorite` for Favourites, `Icons.Default.Settings` for Settings) with dynamic theme tinting.

---

### Localized Translation Resources (8 Languages & Commonwealth Locales)

#### [NEW] [res/values-en-rGB/strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values-en-rGB/strings.xml)
- British English localization using "Favourite Routes" and "Favourites".

#### [NEW] [res/values-en-rAU/strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values-en-rAU/strings.xml)
- Australian English localization using "Favourite Routes" and "Favourites".

#### [NEW] [res/values-en-rNZ/strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values-en-rNZ/strings.xml)
- New Zealand English localization using "Favourite Routes" and "Favourites".

#### [NEW] [res/values-de/strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values-de/strings.xml)
- German localization ("Favoriten", "Routenplaner", "Einstellungen").

#### [NEW] [res/values-fr/strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values-fr/strings.xml)
- French localization ("Favoris", "Planificateur", "Paramètres").

#### [NEW] [res/values-it/strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values-it/strings.xml)
- Italian localization ("Preferiti", "Pianificatore", "Impostazioni").

#### [NEW] [res/values-es/strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values-es/strings.xml)
- Spanish localization ("Favoritos", "Planificador", "Ajustes").

#### [NEW] [res/values-pt/strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values-pt/strings.xml)
- Portuguese localization ("Favoritos", "Planejador", "Configurações").

#### [NEW] [res/values-zh/strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values-zh/strings.xml)
- Chinese Simplified localization ("收藏路线", "路线规划", "设置").

---

### Saved Routes / Favourites Feature

#### [NEW] [FavoriteRoute.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/model/FavoriteRoute.kt)
- Domain data class representing a saved favourite route (title, start address/coords, end address/coords, via address/coords, distance, duration, profile).

#### [NEW] [FavoritesStorage.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/local/FavoritesStorage.kt)
- Local persistent storage class for saving, retrieving, and removing favourite routes.

#### [NEW] [FavoritesViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/com/distinctroutes/app/ui/favorites/FavoritesViewModel.kt)
- ViewModel for managing saved favourites list and actions.

#### [NEW] [FavoritesScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/favorites/FavoritesScreen.kt)
- Screen UI displaying saved favourite routes card list with:
  - "Load into Route Planner" button
  - "Open in OsmAnd" GPX handoff button
  - Delete button

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Add "Save to Favourites" heart button on route calculation result card.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify compilation across all localizations.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Test switching between Route Planner, Favourites, and Settings tabs repeatedly to verify no crash/freeze.
- Test saving a route to Favourites and reloading it into Route Planner.
- Verify UK/AU/NZ English displays "Favourite" and high-contrast icons render clearly in navigation bar.
