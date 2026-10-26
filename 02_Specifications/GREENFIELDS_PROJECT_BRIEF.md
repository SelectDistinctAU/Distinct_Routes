# GREENFIELDS PROJECT DESIGN BRIEF & ARCHITECTURE SPECIFICATION

**Project Name:** Distinct Routes v2 (Greenfields Clean-Slate Rebuild)  
**Package Name:** `com.distinctroutes.app`  
**License:** GNU General Public License v3.0 (GPLv3) — Free & Open Source  
**Repository:** [https://github.com/SelectDistinctAU/Distinct_Routes](https://github.com/SelectDistinctAU/Distinct_Routes)  
**Maintained by:** Select Distinct  

---

## 1. Executive Summary & Safety Mission

Distinct Routes is an open-source, safety-focused cycling and urban mobility navigator. Its primary goal is to **route cyclists, cargo bikes, wheelchairs, and mobility device operators away from high-speed, multi-lane traffic arterials** and onto **quiet residential backstreets, designated cycleways, flat terrain, and green scenic paths**.

This Greenfields Brief defines a zero-legacy, multi-module architecture built from scratch using every technical lesson, API discovery, and UX breakthrough achieved during v1 development.

---

## 2. Legacy Code Elimination Matrix (What is Kept vs. Discarded)

| Component / Feature | Legacy v1 Implementation | Greenfields v2 Rebuild | Rationale |
| :--- | :--- | :--- | :--- |
| **Routing Backend** | Generic OSRM demo server (`router.project-osrm.org`) | **Official BBBike REST API v0.2 (`https://api.bbbike.org/api/0.2/{city}/`)** | OSRM demo servers ignore BBBike's Perth safety graph. Native BBBike API v0.2 delivers 100% quiet street route parity with `bbbike.org/Perth/`. |
| **Map Viewport** | 280dp embedded card inside scrolling column | **100% Full-Screen Canvas (`MapViewContainer(Modifier.fillMaxSize())`)** | Eliminates parent scroll view gesture fighting. Single-finger panning and pin dragging work 100% smoothly with zero accidental page scrolling. |
| **Search UI Controls** | Always-visible input fields covering 85% of screen | **Auto-Collapsing Top Glassmorphic Summary Banner** | Upon route calculation, inputs collapse into a 1-line summary banner (`"Clinton Ave → Boorloo Bridge"`), expanding map viewport to **85%+ of screen**. |
| **Local Storage** | SharedPreferences Moshi JSON serialization | **Jetpack Room ORM SQLite Database (`AppDatabase`)** | Room ORM enables typed SQL queries (`WHERE distanceKm < 10.0`), structured migrations, and 0ms deserialization overhead. |
| **Geocoding Lookups** | Unconstrained global Nominatim search | **City-Bounded Nominatim Search (`viewbox` + `bounded=1`)** | Typing `"122"` in Perth restricts 100% of results to Perth, WA and eliminates international results (e.g., Oman or Amsterdam). |
| **House Numbers** | Stripped by reverse geocoding | **User House Number Preservation** | User-entered street numbers (e.g., `"122"`) are prepended and preserved across all search selections and pin movements. |

---

## 3. Multi-Module Gradle Architecture

The greenfields codebase uses a decoupled Kotlin multi-module Gradle layout:

```text
DistinctRoutes/
├── :core:model         # Pure Kotlin domain models (Route, BikeType, SafetyPreferences)
├── :core:data          # Jetpack Room ORM database, DAOs, repositories, and preferences
├── :core:network       # BBBike API v0.2 Retrofit client & GeoJSON/GPX parsers
├── :feature:planner    # Full-Screen Map Canvas & Route Planner UI
├── :feature:favorites  # Favourites & Ride Journal UI
├── :feature:settings   # Brand Color Palette & App Defaults UI
└── :app                # Application entry point, Hilt DI graph & Navigation Suite
```

---

## 4. Official BBBike REST API v0.2 Protocol Specification

* **Base URL:** `https://api.bbbike.org/api/0.2/{city}/`
* **Mandatory Parameters:**
  * `appid = "guest"`
  * `startc_wgs84 = "{startLon},{startLat}"` (**Longitude first!**)
  * `zielc_wgs84 = "{endLon},{endLat}"` (**Longitude first!**)
  * `pref_seen = 1` (**Mandatory**)
  * `pref_cat = "N1"` (**Prefer side streets / residential roads**)
  * `pref_speed = 20` (Preferred cruising speed in km/h)
  * `pref_ampel = "yes"` (Avoid traffic lights)
  * `pref_unlit = "NL"` (Avoid unlit streets)
  * `pref_ferry = "use"` (Use ferries)
  * `output_as = "gpx-route"` (or `json`)

---

## 5. Jetpack Room ORM Database Schema

* **Table Name:** `favorite_routes`
* **Entity Class:** `FavoriteRouteEntity`
* **Columns:**
  - `id: String` (PrimaryKey)
  - `title: String`, `startAddress: String`, `endAddress: String`, `viaAddress: String?`
  - `startLat: Double`, `startLon: Double`, `endLat: Double`, `endLon: Double`, `viaLat: Double?`, `viaLon: Double?`
  - `distanceKm: Double`, `durationMinutes: Int`, `bikeType: String`, `safetySummary: String`
  - `isJournalEntry: Boolean`, `timestamp: Long`, `routePolylineJson: String?`

---

## 6. Testing Accountability Protocol Standards

All releases require passing automated unit tests before merge:

1. **Safety Adherence Test (`PerthSafetyRouteTest.kt`):**
   * Verifies 0 intersections with arterial roads (**Berwick Street**, **Jarrah Road**, **Shepperton Road**, **Albany Highway**).
   * Verifies route distance is between **5.0 km and 7.0 km**.
2. **Global City Integration Test (`BbbikeApiIntegrationTest.kt`):**
   * Tests live BBBike API endpoints across Perth (Australia), Berlin (Germany), and Sydney (Australia).
3. **Storage Migration Test (`RoomMigrationTest.kt`):**
   * Tests first-launch migration from legacy SharedPreferences records into Room ORM with 100% data integrity.
