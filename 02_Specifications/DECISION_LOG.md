# DECISION LOG — DISTINCT ROUTES
**Purpose:** Record what was considered, why decisions were made
**Version:** 1.0  
**Last Updated:** 2026-10-11  
**Owner:** Gerard Bendotti (Project Owner)

---

## DECISION RECORDS

| ID | Decision Topic | Options Considered | Final Decision | Rationale | Date | Owner | Status |
|----|---------------|-------------------|----------------|-----------|------|-------|--------|
| D001 | App name (consumer) | Distinct Routes / SD Routes / SafeCycle / BikeNav | Distinct Routes | Shortest, most memorable, trademark-clear, aligns with company name SELECT DISTINCT | 2026-10-11 | Gerard Bendotti | Approved |
| D002 | App name (legal) | Distinct Routes / SELECT DISTINCT Bike Navigator / SD Navigator | SELECT DISTINCT Bike Navigator | Legal entity name required for contracts, liability, T&Cs | 2026-10-11 | Gerard Bendotti | Approved |
| D003 | Package name | com.distinctroutes / com.distinct.routes.app / com.distinctroutes.app | com.distinctroutes.app | Reverse-domain style, unique namespace, clear ownership | 2026-10-11 | Gerard Bendotti | Approved |
| D004 | Navigation engine | Native turn-by-turn / BBBike→OsmAnd / Native + OsmAnd hybrid | BBBike→OsmAnd handoff | Delegates navigation complexity to existing open-source solution, focuses development on routing accuracy only | 2026-10-11 | Gerard Bendotti | Approved |
| D005 | Map rendering library | Google Maps SDK / osmdroid / Mapbox / Leaflet | osmdroid-android 6.1.17 | Open-source, no billing setup required, aligns with open data ethos, offline capable | 2026-10-11 | Gerard Bendotti | Approved |
| D006 | Routing data source | BBBike / OSRM / GraphHopper / Custom | BBBike API | Personal validation: BBBike found safer routes than 25 years local knowledge, established API, Berlin closure data | 2026-10-11 | Gerard Bendotti | Approved |
| D007 | Licence type | MIT / Apache 2.0 / GPL-3.0 / BSD | GPL-3.0 | Ensures derivatives remain open-source (copyleft), matches BBBike's open data ethos | 2026-10-11 | Gerard Bendotti | Approved |
| D008 | Platform scope | Android only / iOS only / Android+iOS / Web + Android | Android only (v1) | Owner uses Android, faster time-to-market, defers iOS to v3+ | 2026-10-11 | Gerard Bendotti | Approved |
| D009 | Minimum SDK version | API 21 (5.0) / API 23 (6.0) / API 24 (7.0) | API 24 (Android 7.0+) | Balances feature support vs. market reach, 95%+ of active devices run 7.0+ | 2026-10-11 | Gerard Bendotti | Approved |
| D010 | Architecture pattern | MVP / MVVM / Clean / MVC | MVVM (Model-View-ViewModel) | Industry standard, lifecycle-aware components, testable without Android dependencies | 2026-10-11 | Gerard Bendotti | Approved |
| D011 | Dependency injection | Manual / Koin / Dagger/Hilt / Kodein | Hilt 2.44 | Compile-time validation, Android Jetpack integration, reduced boilerplate vs manual | 2026-10-11 | Gerard Bendotti | Approved |
| D012 | Async framework | Callbacks / RxJava / Coroutines / Kotlin Flow | Coroutines + LiveData | Simpler learning curve vs RxJava, lifecycle-aware LiveData prevents memory leaks | 2026-10-11 | Gerard Bendotti | Approved |
| D013 | Networking stack | Volley / Retrofit + OkHttp / HttpURLConnection / Apollo (GraphQL) | Retrofit 2.9.0 + OkHttp 4.10.0 | Industry standard, Moshi integration for Kotlin, extensive documentation | 2026-10-11 | Gerard Bendotti | Approved |
| D014 | JSON parser | Gson / Jackson / Moshi / kotlinx.serialization | Moshi 1.14.0 | Kotlin data class support, better performance than Gson, Apache licence | 2026-10-11 | Gerard Bendotti | Approved |
| D015 | Image loading | Picasso / Glide / Coil / Fresco | Glide 4.14.2 | Mature library, excellent caching, smaller footprint than Fresco for this use case | 2026-10-11 | Gerard Bendotti | Approved |
| D016 | User accounts | Required / Optional / Not supported | Not supported | Privacy-first design, no GDPR burden, reduces friction to entry | 2026-10-11 | Gerard Bendotti | Approved |
| D017 | Analytics/telemetry | Firebase Analytics / Amplitude / Zero tracking | Zero tracking | Privacy commitment, avoids GDPR consent dialogs, aligns with ethos | 2026-10-11 | Gerard Bendotti | Approved |
| D018 | Weather integration (v1) | Built-in / OSM Weather API / Open-Meteo / Deferred | Deferred to v2 | Global API complexity exceeds v1 scope, BOM Australia-only, post-launch enhancement | 2026-10-11 | Gerard Bendotti | Deferred |
| D019 | Offline maps | Yes / No | No (v1) | Adds 100+ MB APK size, defers to OsmAnd offline capabilities | 2026-10-11 | Gerard Bendotti | Deferred |
| D020 | Multi-language v1 | English only / EN+DE / All EU / All 9 languages | 9 languages (EN-AU, EN-GB, EN-US, DE, IT, ES, PT, FR, ZH) | Inclusivity priority, owner's use case validates non-English demand | 2026-10-11 | Gerard Bendotti | Approved |
| D021 | Safety language | "Safest routes" / "Safety-focused" / Neutral | "Safety-focused" | Legal requirement — cannot guarantee safety, must not make liability claims | 2026-10-11 | Gerard Bendotti | Mandatory |
| D022 | Dog trailer support | Ignore / Profile setting / Weight/cargo mode | Weight/cargo mode (hill penalty increase) | Owner's personal use case validates demand, 8 hours dev effort | 2026-10-11 | Gerard Bendotti | Approved |
| D023 | GPX format | GPX 1.0 / GPX 1.1 / KML / GeoJSON | GPX 1.1 | Standard exchange format, OsmAnd compatible, widely supported | 2026-10-11 | Gerard Bendotti | Approved |
| D024 | Accessiblity | Basic / WCAG AA / TalkBack from v1 | TalkBack from v1 | Vision-impaired support mandatory, not an afterthought | 2026-10-11 | Gerard Bendotti | Mandatory |
| D025 | Community forum | Reddit / Discord / Google Groups / GitHub Discussions | Google Groups | Simple email-based interface, low barrier to entry, searchable archive | 2026-10-11 | Gerard Bendotti | Approved |
| D026 | Hosting platform | GitHub / GitLab / Bitbucket / Self-hosted | GitHub | Industry standard for Android OSS, Actions CI/CD, 100M+ developers | 2026-10-11 | Gerard Bendotti | Approved |
| D027 | Release channel | Google Play only / F-Droid only / Both | Both (Google Play + F-Droid) | Maximises reach, F-Droid appeals to privacy-conscious users | 2026-10-11 | Gerard Bendotti | Approved |
| D028 | Crash reporting | Firebase Crashlytics / Sentry / None | None (v1) | Privacy commitment takes priority, optional opt-in in v2 | 2026-10-11 | Gerard Bendotti | Deferred |
| D029 | Release gates | 3 gates / 5 gates / Continuous | 5 gates (Architecture → Testing → Safety → Compliance → Release) | Rigorous process for safety-critical application, owner retains authority | 2026-10-11 | Gerard Bendotti | Approved |
| D030 | BBBike partnership | Cold outreach / Existing relationship / No formal partnership | Formal partnership proposal | Credibility boost, API stability assurance, ethical attribution | 2026-10-11 | Gerard Bendotti | In Progress |

---

## DEFERRED DECISIONS (Post-v1 Review)

| ID | Decision Topic | Deferral Reason | Target Review Date | Owner |
|----|---------------|-----------------|-------------------|-------|
| D031 | iOS port feasibility | Scope exceeds v1 timeline | 2027-06-01 | Gerard Bendotti |
| D032 | Professional translation budget | Requires funding assessment | Post-v1 revenue review | Gerard Bendotti |
| D033 | Weather API provider (v2) | Depends on API cost/quality comparison | 2027-03-01 | ASAI + Owner |
| D034 | Hazard reporting moderation | Requires community governance model | 2027-01-01 | Gerard Bendotti |
| D035 | E-bike range calculator | Depends on battery API availability | 2027-01-01 | ASAI |

---

## REJECTED DECISIONS (Never Proceeded)

| ID | Rejected Option | Why Rejected | Date | Owner |
|----|-----------------|--------------|------|-------|
| R001 | Native turn-by-turn engine | 40+ hours development, OsmAnd already handles navigation | 2026-10-11 | Gerard Bendotti |
| R002 | Google Maps SDK | Billing required, closed-source, violates open-data ethos | 2026-10-11 | Gerard Bendotti |
| R003 | Realm database | Unnecessary overhead for simple key-value preferences | 2026-10-11 | Gerard Bendotti |
| R004 | RxJava | Coroutines simpler for this scale, lower learning curve | 2026-10-11 | Gerard Bendotti |
| R005 | FlatLaf UI framework | Desktop/Swing only, irrelevant for Android native | 2026-10-11 | Gerard Bendotti |
| R006 | User accounts with login | Privacy-first design, GDPR burden, unnecessary friction | 2026-10-11 | Gerard Bendotti |
| R007 | Subscription monetisation | Free forever commitment, excludes users financially | 2026-10-11 | Gerard Bendotti |

---

## ALTERNATIVES CONSIDERED (Rejected with Rationale)

### Database Options

| Option | Why Considered | Why Rejected |
|--------|----------------|--------------|
| Room SQLite | Standard Android persistence | Overkill for key-value pairs |
| Realm | Fast NoSQL | Licensing concerns, larger binary size |
| SharedPreferences | Native Android key-value storage | Selected — sufficient for v1 |

### Routing Libraries

| Option | Why Considered | Why Rejected |
|--------|----------------|--------------|
| OSRM | Open-source routing engine | Less safety-aware profiles than BBBike |
| GraphHopper | Flexible routing API | Requires self-hosting for full control |
| BBBike | Established API, Berlin closure data | Selected — validated by owner's experience |

### Testing Frameworks

| Option | Why Considered | Why Rejected |
|--------|----------------|--------------|
| Mockito-Kotlin | Popular Java/Kotlin mocking | MockK preferred for Kotlin property mocking |
| MockK | Kotlin-native mocking | Selected — cleaner syntax for Kotlin |
| Robolectric | Android unit testing without emulator | Espresso better for integration tests |

---

## OWNERSHIP & APPROVAL

**All major decisions require Project Owner approval.** Exceptions:
- ASAI may make minor technical implementation choices (e.g., variable naming, comment style)
- ASAI must flag any decision affecting scope, timeline, or safety for Owner review
- All P0 changes logged in CHANGE_CONTROL_LOG.md with Owner signature required

**Decision Authority Matrix:**

| Decision Type | Who Decides | Escalation Path |
|---------------|-------------|-----------------|
| Technical implementation | ASAI | Owner if P0 scope impact |
| Feature additions/removals | Gerard Bendotti | None (final authority) |
| Timeline adjustments | Gerard Bendotti | None (final authority) |
| Safety language changes | Gerard Bendotti | Mandatory Owner review |
| Third-party partnerships | Gerard Bendotti | Legal review if binding |

---

## APPENDIX: DECISION ID REFERENCE

| ID Range | Category | Count |
|----------|----------|-------|
| D001–D015 | Technical Architecture | 15 decisions |
| D016–D024 | Product Features | 9 decisions |
| D025–D030 | Governance & Distribution | 6 decisions |
| D031–D035 | Deferred (v2+) | 5 decisions |
| R001–R007 | Rejected options | 7 decisions |

**Total decisions recorded:** 42 (30 approved, 5 deferred, 7 rejected)

---

*Decision Log — Version 1.0*  
*Next Review: 2026-11-01 (Pre-beta)*

---

*END OF DECISION LOG*