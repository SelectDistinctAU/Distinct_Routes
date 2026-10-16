# Feature Capabilities & System Inventory — Distinct Routes

**Version:** 0.1.0-alpha  
**Platform:** Android (API 26+)  
**Architecture:** MVVM Clean Architecture, Jetpack Compose, Hilt DI, Retrofit/Moshi, osmdroid  
**License:** GNU General Public License v3.0 (GPLv3)  

---

## Complete Feature Matrix

### 1. Routing Engine & OSRM Integration
* **API Integration:** Connects to BBBike OSRM REST service (`router.project-osrm.org`) with custom `User-Agent` headers.
* **3-Point & Multi-Stop Waypoints:** Supports `{startLon},{startLat};{via1Lon},{via1Lat};...;{endLon},{endLat}` routing.
* **Polyline Geometry Extraction:** Aggregates GeoJSON coordinate lists from OSRM response legs and turn steps for street-following polyline rendering.
* **Smart Disk Cache:** Caches route calculations locally with hash-key invalidation incorporating start, vias, destination, vehicle profile, and safety toggles.

### 2. Vehicle & Mobility Device Profiles
* **Standard Bicycle:** 15 km/h base speed profile.
* **City / Utility Bike:** 14 km/h comfortable urban speed profile.
* **E-Bike / Pedelec:** 20 km/h assisted speed profile.
* **Mountain Bike (MTB):** 12 km/h unpaved path profile.
* **Cargo Bike & Tandem:** 10–13 km/h with climbing weight factors.
* **Dog Trailer Mode:** 1.5x weight penalty for heavy towing on steep inclines.
* **Wheelchair & Handbike:** 6 km/h max speed, mandatory steep hill and highway avoidance rules.
* **Mobility Scooter:** 8 km/h speed, mandatory steep hill and highway avoidance rules.

### 3. Safety & Riding Preferences
* **Prefer Green & Scenic Routes:** Prioritizes parks, gardens, and riverfront greenways.
* **Avoid Unlit Streets (Night Safety):** Bypasses unlit streets for night riding safety.
* **Avoid Cobblestones / Unpaved Surfaces:** Smooth surface routing for mobility devices and road bikes.
* **Avoid Ferries:** River crossing preference toggle.
* **Avoid Traffic Lights:** Bypasses signalized intersections.
* **Avoid Highways & Avoid Steep Hills:** Enforced automatically for mobility devices.

### 4. Interactive Map & Pin Dropping
* **osmdroid OpenStreetMap Integration:** Offline tile cache with Mapnik vector tiles.
* **BBBike Map Markers:**
  * **Start Marker:** Forest Green (`#2E7D32`) teardrop pin with Green Play Arrow `▶`.
  * **Via Marker:** Amber Yellow (`#F57F17`) teardrop pin with Yellow Dot `●`.
  * **Destination Marker:** Crimson Red (`#C62828`) teardrop pin with Red Stop Square `■`.
* **Draggable Pins:** Touch drag-and-drop pin placement on the map.
* **Auto Bounding Box:** Automatically zooms and frames calculated routes with `map.zoomToBoundingBox(...)`.

### 5. UI/UX & Screen Space Optimizations
* **Vehicle Choice First:** Vehicle & Mobility Device dropdown positioned at the top.
* **Inline Current GPS Target Button:** Embedded `MyLocation` icon button inside the Start Address field.
* **Compact Via Field Layout:** Single-row `OutlinedTextField` with floating label and trailing delete icon.
* **Waypoint Reordering & Swap:** Up/Down reorder arrows (`↑` / `↓`) for Via stops and Swap Start/Destination button (`⇅`).
* **Auto-Fading Safety Disclaimer:** Disclaimer banner automatically fades out after 4 seconds on launch.

### 6. Favourites & On-Device Ride Journal
* **Save to Favourites:** 1-tap saving with `R.drawable.map_pin_heart_24px` confirmation toast.
* **Dual-Tab Screen (`[ Favourites ]` / `[ Ride Journal ]`):**
  * **Favourites:** Permanent saved routes with 1-tap reload into planner and delete actions.
  * **Ride Journal:** Chronological history of all calculated rides logged locally on device with a Clear Journal option.

### 7. Export & Navigation Handoff
* **Export GPX / Navigate:** Direct 1-tap handoff to OsmAnd or Organic Maps.
* **Share Route:** Android system share sheet chooser to send GPX files or route summaries via SMS, WhatsApp, or Email.

### 8. App Appearance & Material 3 Pine Theme
* **Pine Green Primary Palette (`#1B5E20`):** Vibrant, nature-inspired palette with Sage containers and Warm Amber accents.
* **App Theme Mode Selector:** **Light Pine**, **Dark Night**, or **System Default** in Settings.
* **M3 Surface Container Ladder:** `surfaceContainerLow`, `surfaceContainer`, `surfaceContainerHigh` depth elevation on cards.
* **Expressive Pill Shapes:** `PillShape` (`CircleShape`) for prominent CTAs.

### 9. Multi-Language & Commonwealth Locales
* **Native Commonwealth English:** `values-en-rGB`, `values-en-rAU`, `values-en-rNZ` with correct "Favourite" spellings.
* **8 Global Languages:** English, German (`de`), French (`fr`), Italian (`it`), Spanish (`es`), Portuguese (`pt`), Mandarin Chinese (`zh`).

### 10. Privacy & Open Source
* **Zero Telemetry:** No user registration, no ads, no location analytics.
* **100% On-Device Storage:** SharedPreferences / Moshi JSON private local storage.
* **GPL-3.0 Open Source:** Fully transparent open-source codebase.
