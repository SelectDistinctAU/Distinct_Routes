# Approved Implementation Plan — Core Feature & Fix Rollout

**Approval Status:** Approved by Project Owner  
**Approval Timestamp:** 2026-10-11  

---

Align the codebase with project specifications, fix compilation errors, and complete the core route planning pipeline (Data, Domain, and Compose UI layers).

## User Review Required

> [!IMPORTANT]
> **UI Framework Choice:** [MainActivity.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/MainActivity.kt) was created using Jetpack Compose, but [app/build.gradle.kts](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/build.gradle.kts) was missing Compose dependencies. We will update [build.gradle.kts](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/build.gradle.kts) to enable Compose and add the required Jetpack Compose dependencies.

> [!NOTE]
> **API Protocol Alignment:** [BBBikeApiService.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/api/BBBikeApiService.kt) currently defines a `POST` JSON request, whereas [API_INTEGRATION_SPEC.md](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/02_Specifications/API_INTEGRATION_SPEC.md) specifies an HTTP `GET` request. We will align the Retrofit interface and repository with the HTTP `GET` query specification.

## Open Questions

None.

## Proposed Changes

### Documentation & Plan Archiving

#### [NEW] [Approved Implementation Plans/](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/Approved%20Implementation%20Plans)
- Create the directory `Approved Implementation Plans/` in the project root.
- Archive a copy of this approved plan as `Approved Implementation Plans/2026-10-11_Core_Feature_&_Fix_Rollout.md` containing the title and approval timestamp.

---

### Build Configuration

#### [MODIFY] [build.gradle.kts](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/build.gradle.kts)
- Enable Compose build features (`buildFeatures { compose = true }`).
- Configure Kotlin Compose options.
- Add Compose dependencies: `ui`, `ui-graphics`, `ui-tooling-preview`, `material3`, `adaptive-navigation-suite`, `activity-compose`, `lifecycle-viewmodel-compose`.

---

### Data Layer

#### [MODIFY] [BBBikeApiService.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/api/BBBikeApiService.kt)
- Refactor API method to HTTP `GET` with query parameters (`from`, `to`, `profile`, `language`, `avoid`, `format`).

#### [MODIFY] [RoutingRequest.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/model/RoutingRequest.kt)
- Update data model to hold start/end location coordinates, profile, language, and safety preference parameters.

#### [MODIFY] [RoutingResponse.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/model/RoutingResponse.kt)
- Enhance response data classes to support route geometry, distance, duration, and error codes.

#### [MODIFY] [BikeRouteRepository.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/repository/BikeRouteRepository.kt)
- Correct package declaration to `com.distinctroutes.app.data.repository`.
- Add `@Inject` constructor and inject `Moshi` instance for disk cache serialization.
- Wire network calls to `apiService.calculateRoute(...)`.

---

### Domain Layer

#### [NEW] [Route.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/model/Route.kt)
- Domain model representing a calculated bike route (id, waypoints, distance, duration, safety flags, GPX track).

#### [NEW] [SafetyPreferences.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/model/SafetyPreferences.kt)
- Model for safety toggles: avoid traffic lights, highways, steep hills, dog trailer weight penalty, and bicycle type selection.

#### [NEW] [CalculateRouteUseCase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/usecase/CalculateRouteUseCase.kt)
- Domain use case for route calculation, applying profile and hill penalty logic (e.g. 1.5x penalty for dog trailer mode).

#### [NEW] [ExportToGPXUseCase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/usecase/ExportToGPXUseCase.kt)
- Domain use case to export route GPX to internal storage and create an Intent for OsmAnd navigation handoff (`org.osmand`).

---

### UI Layer

#### [NEW] [RoutePlannerViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerViewModel.kt)
- Hilt `@HiltViewModel` managing UI state (`StateFlow`) for route calculation, city selection, safety preferences, and GPX export.

#### [NEW] [RoutePlannerScreen.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerScreen.kt)
- Compose UI providing origin/destination inputs, city dropdown, safety preference toggles (traffic lights, highways, steep hills, dog trailer mode, bike type), calculate button, route summary card, GPX export button, and OsmAnd handoff trigger.
- Add TalkBack `contentDescription` on all controls.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/MainActivity.kt)
- Connect `@AndroidEntryPoint`, Jetpack Compose theme, and `RoutePlannerScreen`.

#### [MODIFY] [strings.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/res/values/strings.xml)
- Add string resources for UI controls, safety options, disclaimers, and accessibility content descriptions.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify project compilation.
- Verify static analysis with `analyze_file`.

### Manual Verification
- Verify layout and functionality in Compose Preview and device/emulator.
- Validate TalkBack screen reader labels on UI controls.
