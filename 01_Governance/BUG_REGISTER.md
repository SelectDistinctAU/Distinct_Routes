# BUG REGISTER & ISSUE LOG — DISTINCT ROUTES

**Project:** Distinct Routes (`com.distinctroutes.app`)  
**Repository:** [https://github.com/SelectDistinctAU/Distinct_Routes](https://github.com/SelectDistinctAU/Distinct_Routes)  
**GitHub Issues Integration:** Syncs with GitHub Issue Tracker, Pull Requests, and GitHub Projects.

---

## GitHub Issues Integration Strategy

Distinct Routes integrates directly with GitHub Issues for open-source bug tracking, community triage, and automated commit resolution:

1. **GitHub Issue Tracker:** Public issues at `https://github.com/SelectDistinctAU/Distinct_Routes/issues`.
2. **Commit Keyword Linking:**
   * Include issue numbers in Git commit messages (e.g. `fix #1` or `resolves #2`).
   * When merged into `main`, GitHub automatically closes the corresponding issue and updates the project board.
3. **GitHub Projects / Kanban:** Track issues across columns: `Backlog`, `In Progress`, `In Review`, `Done`.

---

## Active & Resolved Bug Log

| Bug ID | Title & Summary | Severity | Affected Component | Status | Resolution |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **BUG-001** | **Map Jitter & Focus Snatching on Recomposition** <br> Map view animates and snatches pan/zoom focus whenever user types or interacts with fields. | **High** | [MapViewContainer.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/MapViewContainer.kt) | **Resolved** | Encapsulated `zoomToBoundingBox` in `LaunchedEffect(routePoints)` so zoom animation triggers strictly once when a new route is calculated. |
| **BUG-002** | **Address Search Rate-Limiting (Missing Debounce)** <br> Rapid typing sent 10+ HTTP requests/sec to Nominatim, triggering 429 Too Many Requests. | **High** | [RoutePlannerViewModel.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/ui/route_planner/RoutePlannerViewModel.kt) & [NominatimApiService.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/data/api/NominatimApiService.kt) | **Resolved** | Implemented 350ms coroutine debouncing (`searchJob?.cancel()`) and added compliant User-Agent headers (`distinctroutes@pm.me`). |
| **BUG-003** | **Tab Navigation Freeze / Deadlock** <br> Unmounting MapView without cancelling tile downloaders caused thread lock in osmdroid. | **Critical** | [MainActivity.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/MainActivity.kt) | **Resolved** | Preserved `RoutePlannerScreen` composition state across tab switches using `graphicsLayer { alpha = ... }`. |
| **BUG-004** | **Share Intent Chooser Non-Activity Context Crash** <br> Tapping Share Route crashed on devices missing Activity context flags. | **High** | [ExportToGPXUseCase.kt](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/java/com/distinctroutes/app/domain/usecase/ExportToGPXUseCase.kt) & [AndroidManifest.xml](file:///C:/Users/geewy/AndroidStudioProjects/DistinctRoutes/app/src/main/AndroidManifest.xml) | **Resolved** | Corrected FileProvider meta-data attribute (`android:resource`) and added `FLAG_ACTIVITY_NEW_TASK` to chooser intent. |

---

## How to Report a New Bug
1. Open [GitHub Issues](https://github.com/SelectDistinctAU/Distinct_Routes/issues/new).
2. Use the Bug Report template providing:
   - Device model & Android OS version.
   - Steps to reproduce.
   - Expected vs actual behavior.
   - Logcat stack trace snippet (if applicable).
