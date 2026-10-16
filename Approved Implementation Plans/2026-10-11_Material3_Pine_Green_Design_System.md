# Approved Implementation Plan — Material 3 Emerald/Pine Design System & Build SDK Alignment

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Refactor the Jetpack Compose Material 3 design system for Distinct Routes using a deep Pine/Emerald Green seed palette, M3 surface container elevation ladders, expressive typography, pill-shaped CTAs, and dynamic theme fallback, while aligning Gradle/AGP SDK tools.

## User Review Required

> [!IMPORTANT]
> **Deep Emerald/Pine Safety Palette:** Replaces flat default template greys with a vibrant, WCAG AA compliant Pine Green (`#1E5B3A` / `#004D28`) primary palette, Warm Ochre/Amber (`#7D5260` / `#C88B00`) tertiary accent palette, and full M3 surface container elevation ladder.

> [!NOTE]
> **SDK Build Alignment:** Sets `compileSdk = 35` and AGP `8.7.3` / `8.5.2` to match Android SDK v4 XML build-tools cleanly and suppress SDK version warnings.

## Proposed Changes

### Build System & SDK Tooling

#### [MODIFY] [build.gradle.kts](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/build.gradle.kts)
- Set AGP `8.7.3` or `8.5.2` matching Android SDK build tools.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/build.gradle.kts)
- Set `compileSdk = 35`.

---

### Theme & Design Tokens Layer

#### [MODIFY] [Color.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/theme/Color.kt)
- Define full Material 3 Light & Dark color role tokens derived from Pine Green (`#1E5B3A`):
  - Primary: Emerald Pine Green
  - Secondary: Sage / Forest Slate
  - Tertiary: Warm Ochre / Safety Amber
  - Full Surface Container Ladder (`Lowest`, `Low`, `Container`, `High`, `Highest`)

#### [NEW] [Shape.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/theme/Shape.kt)
- Define M3 Expressive shapes: Pill shape (`CircleShape`) for CTAs and rounded containers for cards (`16.dp`, `20.dp`, `28.dp`).

#### [MODIFY] [Typography.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/theme/Typography.kt)
- Define M3 Typography hierarchy with emphasized font weights (`Bold` / `SemiBold`) for CTAs, page titles, and card headers.

#### [MODIFY] [Theme.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/theme/Theme.kt)
- Implement `DistinctRoutesTheme` with `lightColorScheme`, `darkColorScheme`, dynamic color support (`dynamicLightColorScheme` / `dynamicDarkColorScheme`), and M3 shapes/typography.

---

### UI Component & Screen Refactor Layer

#### [MODIFY] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Apply M3 surface container ladder to cards and inputs.
- Style "Calculate Safety-Focused Route" CTA with prominent pill shape (`Shape.kt` / `CircleShape`) and `primary` container filled style.
- Style safety summary and vehicle badges with `secondaryContainer` / `tertiaryContainer` tones.

#### [MODIFY] [FavoritesScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/favorites/FavoritesScreen.kt)
- Apply `surfaceContainerLow` and `surfaceContainer` depth to saved route and journal cards.
- Style filter chips and action buttons with semantic M3 colors.

#### [MODIFY] [SettingsScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/com/distinctroutes/app/ui/settings/SettingsScreen.kt)
- Apply surface container depth ladder across Settings cards, "Why Distinct Routes?" mission card, and Privacy Statement modal.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/com/distinctroutes/app/MainActivity.kt)
- Ensure `NavigationSuiteScaffold` bottom navigation bar applies M3 semantic color roles (`primary`, `onSurfaceVariant`, `surfaceContainer`).

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify project compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Inspect all screens on emulator in both Light Theme and Dark Theme.
- Verify WCAG contrast ratios and depth elevation ladders.
