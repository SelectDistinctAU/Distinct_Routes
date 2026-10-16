# CHANGELOG — DISTINCT ROUTES

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [0.1.0-alpha] - 2026-10-11

### Added
- **Safety-First Routing Engine:** BBBike OSRM integration with GeoJSON polyline geometry rendering on osmdroid OpenStreetMap view.
- **Specialized Vehicle & Mobility Device Profiles:** Standard Bicycles, E-Bikes, Cargo Bikes (with Dog Trailer weight penalties), Tandems, Wheelchairs / Handbikes (6 km/h max, slope rules), and Mobility Scooters (8 km/h max).
- **Safety Preferences Suite:** Toggles for Prefer Green & Scenic Routes, Avoid Unlit Streets (Night Safety), Avoid Cobblestones / Unpaved Surfaces, Avoid Ferries, Avoid Traffic Lights, Avoid Highways, and Avoid Steep Hills.
- **Multi-Stop Waypoints (Trails & Loops):** Unlimited Via stops with Up/Down reordering and Start/Destination swap buttons.
- **BBBike Custom Map Markers:** Green Play Arrow ▶ (Start), Yellow Dot ● (Via), and Red Stop Square ■ (Destination).
- **On-Device Ride Journal & Favourites:** Favourites saving + automatic local ride history logging with 1-tap reloading into Route Planner.
- **Navigation Handoff & Share:** 1-tap GPX handoff to OsmAnd / Organic Maps and Android system share sheet chooser for sending GPX files to contacts.
- **Material 3 Theme System:** Dual brand color palettes (Metallic Blue vs Emerald Pine) in Light and Dark Night modes.
- **Multi-Language & Commonwealth Locales:** Native UK (`en-GB`), AU (`en-AU`), and NZ (`en-NZ`) "Favourite" spellings + German, French, Italian, Spanish, Portuguese, and Mandarin Chinese.
- **Privacy Policy & Public Docs:** Complete Web/In-App Privacy Policy statement, Google Play Store Listing (`APP_STORE_LISTING.md`), User Guide (`USER_GUIDE.md`), Onboarding Guide (`ONBOARDING_GUIDE.md`), Beta Tester Recruitment Material (`TESTER_RECRUITMENT.md`), and Feature Capabilities inventory (`FEATURE_CAPABILITIES.md`).

### Fixed
- Fixed `osmdroid` `MapView` lifecycle detached state bug causing tab navigation freeze/ANR on tab switching.
- Fixed `FileProvider` meta-data attribute (`android:resource="@xml/file_paths"`) resolving Share Intent crashes.
- Fixed Via route cache key invalidation bug in `BikeRouteRepository.kt` to ensure 3-point routes recalculate accurately.
- Fixed button text line-wrapping on result cards and theme selector chips in Settings.

---

## [0.1.0-beta] - TBD

_In progress_

---

## [1.0.0] - TBD

_Planned_

---

[0.1.0-alpha]: https://github.com/SELECT-DISTINCT/Distinct_Routes/releases/tag/v0.1.0-alpha
[0.1.0-beta]: https://github.com/SELECT-DISTINCT/Distinct_Routes/releases/tag/v0.1.0-beta
[1.0.0]: https://github.com/SELECT-DISTINCT/Distinct_Routes/releases/tag/v1.0.0