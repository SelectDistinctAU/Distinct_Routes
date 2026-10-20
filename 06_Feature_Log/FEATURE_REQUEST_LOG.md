# FEATURE REQUEST LOG — DISTINCT ROUTES
**Purpose:** Track feature requests, capabilities, and ROI justifications
**Version:** 0.2.0-alpha  
**Last Updated:** 2026-10-11  
**Owner:** Gerard Bendotti (Project Owner)

---

## REQUEST LOG & IMPLEMENTATION STATUS

| ID | Feature | Requestor | Description | ROI Justification | Investment Cost | Score | Decision | Target Release | Status |
|----|---------|-----------|-------------|-------------------|-----------------|-------|----------|----------------| :--- |
| FR001 | Offline maps | Community | "Users need maps in tunnels/subways without internet" | Medium utility for underground areas | 40 hours dev + 100 MB APK increase | 0.66 | Deferred | v2+ | Pending |
| FR002 | Multi-language v1 launch | Gerard Bendotti | "Non-English commuters need safety routing" | High inclusivity priority, owner validates demand | 80 hours i18n + translation effort | 1.20 | Approved | v1 | **Implemented (v0.1.0)** |
| FR003 | Voice navigation | ASAI | "Hands-free riding essential for safety" | Low value — OsmAnd already provides this via handoff | Defer to OsmAnd (handled externally) | 0.10 | Rejected | Never (in-app) | Closed |
| FR004 | iOS port | Community | "iPhone cyclists excluded from safety tool" | Medium demand but high dev effort | 80 hours Swift dev + separate codebase | 0.40 | Rejected | v3+ | Closed |
| FR005 | Weather alerts | Gerard Bendotti | "Wind affects cargo bike safety significantly" | Medium-high safety relevance for e-bike users | 30 hours + API complexity + battery drain | 0.90 | Deferred | v2+ | Pending |
| FR006 | Hazard reporting | Community | "Crowd-sourced safety data improves routing" | Medium community value but moderation burden | 60 hours + ongoing moderation + legal liability | 0.70 | Deferred | v2+ | Pending |
| FR007 | Dog trailer weight mode | Gerard Bendotti | "Cargo dynamics affect hill climbing safety" | High personal use case validates demand | 8 hours routing config adjustment | 1.50 | Approved | v1 | **Implemented (v0.1.0)** |
| FR008 | Elevation profile chart | ASAI | "Climbers need gradient awareness before committing" | Medium utility for tandem/hill riders | 12 hours chart library integration | 0.80 | Deferred | v2+ | Pending |
| FR009 | Social route sharing | Community | "Share routes with friends easily" | Low utility — GPX export already handles this | 20 hours social integration + privacy concerns | 0.30 | Rejected | Never | Closed |
| FR010 | Subscription model | Community | "Revenue sustains development long-term" | Contradicts free-for-ever ethos | 40 hours billing setup + excludes financially disadvantaged | 0.20 | Rejected | Never | Closed |
| FR011 | Trip history tracking | ASAI | "Track cycling habits over time" | Low privacy-aligned value, violates no-tracking principle | 15 hours + GDPR burden + contradicts ethos | 0.15 | Rejected | Never | Closed |
| FR012 | Berlin closure data | BBBike | "Real-time road closures improve routing" | High Berlin-specific value, limited elsewhere | Requires BBBike API partnership + ongoing maintenance | 1.10 | Deferred | v2+ | Pending |
| FR013 | Fitness stats integration | Community | "Calories burned, heart rate metrics" | Low utility for commuter segment, adds complexity | 25 hours + sensor permissions + health data privacy | 0.25 | Rejected | v3+ | Closed |
| FR014 | Dark mode | ASAI | "Night cyclists need OLED-friendly UI" | Medium accessibility value, easy to implement | 4 hours Material Components theming | 1.80 | Approved | v1 | **Implemented (v0.1.0)** |
| FR015 | Battery optimization mode | ASAI | "Long commutes drain phone battery" | Medium utility for power users | 10 hours background service optimization | 0.85 | Deferred | v2+ | Pending |
| FR016 | Ride scheduling | Community | "Plan commute departure times in advance" | Medium planner value, low daily utility | 18 hours scheduler + alarm integration | 0.45 | Deferred | v2+ | Pending |
| FR017 | Emergency contact alert | ASAI | "Auto-alert contacts if accident detected" | High safety value but complex implementation | 50 hours + accelerometer sensors + false positive risk | 0.75 | Rejected | v3+ | Closed |
| FR018 | Multilingual voice hints | Community | "Navigation prompts in user's language" | High utility for tourists | Defer to OsmAnd (handles this natively) | 0.10 | Rejected | Never | Closed |
| FR019 | Bicycle type selection | Gerard Bendotti | "Road bike vs mountain bike affects route choice" | Medium routing relevance, minimal code effort | 6 hours routing profile adjustment | 1.30 | Approved | v1 | **Implemented (v0.1.0)** |
| FR020 | Crash reporting (opt-in) | ASAI | "Identify bugs faster with analytics" | Medium dev efficiency value, privacy trade-off | 8 hours Firebase setup + GDPR consent flow | 0.55 | Deferred | v2+ | Pending |
| **FR021** | **Dual Brand Color Theme System** | Gerard Bendotti | "Switch between Metallic Blue & Emerald Green palettes in Settings" | High personalization & brand aesthetic appeal | 6 hours Compose M3 color scheme mapping | 1.65 | Approved | v0.2.0 | **Implemented (v0.2.0)** |
| **FR022** | **Wheelchair & Mobility Scooter Profiles** | Gerard Bendotti | "Custom speed (6-8 km/h) & slope avoidance rules for mobility devices" | Critical accessibility & inclusivity priority | 10 hours routing profile & safety logic | 1.90 | Approved | v0.1.0 | **Implemented (v0.1.0)** |
| **FR023** | **On-Device Ride Journal** | Gerard Bendotti | "Automatic local ride history logging with 100% privacy" | High rider utility without cloud tracking | 8 hours SharedPreferences storage helper | 1.70 | Approved | v0.1.0 | **Implemented (v0.1.0)** |
| **FR024** | **Multi-Stop Waypoint Reordering & Swap** | Gerard Bendotti | "Up/Down reorder arrows & Start/End swap for loop trails" | High route planning utility for trail loops | 6 hours Compose state & list reordering | 1.75 | Approved | v0.2.0 | **Implemented (v0.2.0)** |
| **FR025** | **100% Full-Screen Map Canvas UI** | Gerard Bendotti | "Full-screen map background with floating overlay cards" | Major UI/UX improvement, buttery-smooth single finger map gesture control | 12 hours Compose Box overlay refactor | 1.95 | Approved | v0.2.0 | **Implemented (v0.2.0)** |
| **FR026** | **City-Scoped Address Search Lookups** | Gerard Bendotti | "Strictly bound Nominatim search lookups to active city" | Critical usability fix, eliminates international search noise | 4 hours Nominatim viewbox & query framing | 1.85 | Approved | v0.2.0 | **Implemented (v0.2.0)** |

---

## DECISION SUMMARY

| Decision | Count | Percentage |
|----------|-------|------------|
| Approved & Implemented | 10 | 38% |
| Deferred (v2+) | 8 | 31% |
| Rejected permanently | 8 | 31% |
| **Total Logged** | **26** | **100%** |

---

*Feature Request Log — Version 0.2.0-alpha*
*GitHub Repository Sync: https://github.com/SelectDistinctAU/Distinct_Routes*