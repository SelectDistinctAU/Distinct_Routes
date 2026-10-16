# TECHNICAL ARCHITECTURE — DISTINCT ROUTES
**Version:** 1.0  
**Last Updated:** 2026-10-11  
**Platform:** Android (Kotlin)  
**Package:** com.distinctroutes.app

---

## ARCHITECTURE OVERVIEW

### Design Pattern: MVVM (Model-View-ViewModel)

**Rationale:**
- Separation of UI concerns (Views/Fragments) from business logic (ViewModels)
- Lifecycle-aware components prevent memory leaks
- LiveData/StateFlow enables reactive data flow
- Testable view models without Android dependencies
- Industry standard for modern Android development

**Architecture Diagram:**

+-------------------------+ | UI LAYER | | +--------------------+ | | | MainActivity | | | | RoutePlannerFragment| | | | SettingsActivity | | | | GPXExportDialog | | | +----------+---------+ | | | | | +----------v---------+ | | | VIEW MODELS | | | | +----------------+ | | | | |MainViewModel | | | | | |RoutePlannerVm | | | | | +--------+-------+ | | +------------+---------+--+ | +------------v------------------+ | DOMAIN LAYER | | +---------------------------+ | | | Use Cases | | | | +------------------+ | | | | |CalculateRouteUC | | | | | |ExportToGPXUseCase| | | | | +--------+---------+ | | +------------+----------------+ | +------------v------------------+ | DATA LAYER | | +---------------------------+ | | | Repositories | | | | +------------------+ | | | | |RouteRepository | | | | | +--------+---------+ | | +------------+----------------+ | +------------v------------------+ | DATA SOURCES | | +------------------+ | | |BBBikeAPIClient | | | |PreferencesStorage| | | +------------------+ | +-------------------------------+


---

## CORE LIBRARIES & DEPENDENCIES

### Routing & Mapping

| Library | Version | Purpose | Licence |
|---------|---------|---------|---------|
| osmdroid-android | 6.1.17 | OpenStreetMap rendering | Apache 2.0 |
| osmbonuspack | 6.9.0 | OSM POI, routing helpers | Apache 2.0 |
| GeoJSON-Kotlin | 1.1.0 | GPX/GeoJSON parsing | MIT |

**Rationale:** osmdroid is the standard open-source map library for Android. Google Maps requires billing setup and violates open-source ethos.

### Networking

| Library | Version | Purpose | Licence |
|---------|---------|---------|---------|
| Retrofit | 2.9.0 | REST API client | Apache 2.0 |
| OkHttp | 4.10.0 | HTTP client | Apache 2.0 |
| Moshi | 1.14.0 | JSON serialization | Apache 2.0 |

**Rationale:** Industry-standard networking stack. Moshi preferred over Gson for Kotlin data class support.

### Coroutines & Reactive

| Library | Version | Purpose | Licence |
|---------|---------|---------|---------|
| kotlinx-coroutines-android | 1.6.4 | Async operations | Apache 2.0 |
| androidx.lifecycle-livedata | 2.5.1 | Lifecycle-aware data | Apache 2.0 |
| androidx.lifecycle-viewmodel-ktx | 2.5.1 | ViewModel lifecycle | Apache 2.0 |

**Rationale:** Coroutines simplify async code vs. callbacks. LiveData lifecycle-awareness prevents crashes on configuration changes.

### Dependency Injection

| Library | Version | Purpose | Licence |
|---------|---------|---------|---------|
| Hilt | 2.44 | DI for Android | Apache 2.0 |

**Rationale:** Reduces boilerplate vs. manual DI. Compile-time validation catches errors early.

### Testing

| Library | Version | Purpose | Licence |
|---------|---------|---------|---------|
| JUnit4 | 4.13.2 | Unit tests | Eclipse Public License |
| Espresso | 3.5.0 | UI tests | Apache 2.0 |
| MockK | 1.13.3 | Kotlin mocking | Apache 2.0 |

**Rationale:** Standard Android testing stack. MockK preferred over Mockito for Kotlin property mocking.

### Other

| Library | Version | Purpose | Licence |
|---------|---------|---------|---------|
| Glide | 4.14.2 | Image loading | BSD/MIT/Apache |
| Material Components | 1.7.0 | UI components | Apache 2.0 |

---

## PACKAGE STRUCTURE

com.distinctroutes.app/ ├── ui/ # UI layer │ ├── MainActivity.kt │ ├── route_planner/ │ │ ├── RoutePlannerFragment.kt │ │ └── RoutePlannerViewModel.kt │ ├── settings/ │ │ ├── SettingsFragment.kt │ │ └── SettingsViewModel.kt │ └── gpx_export/ │ └── GPXExportDialog.kt │ ├── domain/ # Business logic layer │ ├── model/ │ │ ├── Route.kt │ │ ├── City.kt │ │ └── SafetyPreferences.kt │ ├── usecase/ │ │ ├── CalculateRouteUseCase.kt │ │ ├── ExportToGPXUseCase.kt │ │ └── LoadCitiesUseCase.kt │ └── repository/ │ └── RouteRepository.kt │ ├── data/ # Data layer │ ├── api/ │ │ ├── BBBikeApiService.kt │ │ ├── BBBikeApiClient.kt │ │ └── dto/ # Data Transfer Objects │ │ ├── RouteResponse.kt │ │ └── CityListResponse.kt │ ├── local/ │ │ ├── PreferencesStorage.kt │ │ └── GPXWriter.kt │ └── repository/ │ └── RouteRepositoryImpl.kt │ ├── di/ # Dependency injection │ └── AppModule.kt │ ├── util/ # Utilities │ ├── ExtensionFunctions.kt │ └── Constants.kt │ └── App.kt # Application class


---

## BBBIKE API INTEGRATION

### Endpoint Structure

GET https://www.bbbike.org/bbbike/?from={from}&to={to}&profile={profile}


**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| from | String | Yes | Start location (lat,lng or address) |
| to | String | Yes | End location (lat,lng or address) |
| profile | String | Yes | Route preference (bike, fast, shortest, quiet) |
| language | String | No | Response locale (en, de, etc.) |

**Response Format:** GPX or GeoJSON (we parse GPX)

**Rate Limit:** Unknown — implement exponential backoff + request queue (max 5/min)

### Error Handling

| HTTP Code | Meaning | User Message | Retry Action |
|-----------|---------|--------------|--------------|
| 200 | Success | Route calculated successfully | None |
| 400 | Bad request | Invalid coordinates/addresses | Do not retry |
| 429 | Too many requests | Slow down — try again in 30 seconds | Wait 30s + retry |
| 500 | Server error | BBBike temporarily unavailable | Exponential backoff |
| 503 | Service unavailable | Try again later | Exponential backoff |

### API Timeout Configuration

| Setting | Value | Reason |
|---------|-------|--------|
| Connect timeout | 10 seconds | Network latency tolerance |
| Read timeout | 10 seconds | Route calculation max time |
| Write timeout | 5 seconds | GPX export operation |

---

## PERMISSIONS

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />

Runtime Permissions:

Permission	When Requested	Graceful Degradation
ACCESS_FINE_LOCATION	On first route calculation	Manual start/end point entry
INTERNET	Auto-granted (normal permission)	Offline mode (cached cities only)
STORAGE
Data Type	Storage Method	Retention	Encryption
User preferences	Android SharedPreferences	Until cleared	None (not sensitive)
Saved routes	Internal storage (/GPX/)	Until deleted	None (user-controlled)
API cache	DiskLruCache (10MB max)	24 hours	None
Logs (debug only)	No logging in production	N/A	N/A
BUILD CONFIGURATION
Gradle Dependencies (build.gradle.kts)
android {
    namespace = "com.distinctroutes.app"
    compileSdk = 33
    
    defaultConfig {
        applicationId = "com.distinctroutes.app"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "0.1.0-alpha"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    
    // DI
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-compiler:2.44")
    
    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    
    // Maps
    implementation("org.osmdroid:osmdroid-android:6.1.17")
    implementation("com.github.MKergall:osmbonuspack:6.9.0")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    testImplementation("io.mockk:mockk:1.13.3")
}

QUALITY GATES
Metric	Target	Tool	Measurement Frequency
APK size	< 15 MB	Build analyzer	Every build
Crash rate	< 1%	Firebase Crashlytics	Daily
Test coverage	> 80%	JaCoCo	Per PR
Static analysis	0 critical issues	Detekt + ktlint	Pre-commit
Route calc timeout	< 10 seconds	Instrumentation test	Per build
RISK MITIGATION
Risk	Mitigation	Owner	Status
BBBike API changes	Wrap in repository interface; easy to swap provider	ASAI	Open
OsmAnd intent fails	Fallback to GPX file manager	ASAI	Open
Large APK size	ProGuard minification + no offline maps	ASAI	Mitigated
Low-end device lag	Lazy map loading + simplified route polylines	ASAI	Open
GPS accuracy <10m	Warning banner before route calculation	ASAI	Open
ERROR HANDLING STRATEGY
Network Errors
Retry Logic:
1. First failure → Immediate retry (network hiccup?)
2. Second failure → Wait 5 seconds, retry
3. Third failure → Wait 30 seconds, retry
4. Fourth failure → Show error to user, offer manual retry

GPS Errors
GPS Failure Response:
1. Check if permission granted → If not, request permission
2. Check if GPS enabled → If not, prompt user to enable
3. Check accuracy > 10m → If not, show warning banner
4. Still failing after 10 seconds → Fall back to manual entry

TESTING STRATEGY
Unit Tests (Domain Layer)
Test Target	Coverage Goal	Framework
Use Cases	100% branch coverage	JUnit + MockK
ViewModels	80% line coverage	JUnit + MockK
Repository Interfaces	100% contract tests	JUnit + MockK
Integration Tests (Data Layer)
Test Target	Coverage Goal	Framework
BBBike API Client	All HTTP scenarios	JUnit + MockWebServer
GPX Parser	All XML edge cases	JUnit
Preference Storage	Read/write cycles	Instrumentation test
UI Tests (Presentation Layer)
Test Target	Coverage Goal	Framework
Main Activity launch	Happy path + error states	Espresso
Route calculation flow	End-to-end scenario	Espresso
GPX export dialog	All button actions	Espresso
Settings persistence	Preference changes survive restart	Espresso
ACCESSIBILITY REQUIREMENTS
Requirement	Implementation	Verification
TalkBack compatibility	All buttons have contentDescription	Manual screen reader test
Font scaling	Support up to 200% system font size	Manual resize test
Color contrast	Minimum 4.5:1 ratio (WCAG AA)	Automated scan (Lint)
Touch target size	Minimum 48dp x 48dp	Layout inspector
Focus order	Logical tab navigation	Manual keyboard test
MULTI-LANGUAGE IMPLEMENTATION
Resource Structure
res/
├── values/              # Default (EN-AU)
│   └── strings.xml
├── values-en-rGB/      # English (UK)
│   └── strings.xml
├── values-en-rUS/      # English (US)
│   └── strings.xml
├── values-de/          # German
│   └── strings.xml
├── values-it/          # Italian
│   └── strings.xml
├── values-es/          # Spanish
│   └── strings.xml
├── values-pt/          # Portuguese
│   └── strings.xml
├── values-fr/          # French
│   └── strings.xml
└── values-zh/          # Chinese (Mandarin)
    └── strings.xml

Translation Management
Stage	Responsibility	Deadline
Seed EN-AU strings	Project Owner	Gate 1 sign-off
Seed DE strings	Project Owner	Gate 1 sign-off
Community translations	Volunteers	Beta release
Professional review	Budget TBD	Public release
APPENDIX A: DATA MODELS
Route Data Class
data class Route(
    val id: String,                    // UUID
    val startLocation: LatLng,         // Coordinates
    val endLocation: LatLng,           // Coordinates
    val distanceMeters: Double,        // Total distance
    val durationSeconds: Long,         // Estimated time
    val polyline: List<LatLng>,        // Path coordinates
    val safetyFlags: SafetyFlags,      // Safety metadata
    val timestamp: Instant             // Creation time
)

data class SafetyFlags(
    val avoidsTrafficLights: Boolean,
    val avoidsHighways: Boolean,
    val avoidsSteepHills: Boolean,
    val trafficLightCount: Int,
    val highwaySegments: Int
)

City Data Class
data class City(
    val name: String,                  // Display name
    val countryCode: String,           // ISO code (e.g., AU, DE)
    val boundingBox: BoundingBox,      // Map bounds
    val hasClosureData: Boolean        // Berlin-specific
)

data class BoundingBox(
    val southWest: LatLng,
    val northEast: LatLng
)

APPENDIX B: CONSTANTS
object Constants {
    // API
    const val BBBIKE_BASE_URL = "https://www.bbbike.org/"
    const val BBBIKE_ROUTE_PATH = "bbbike/"
    const val API_TIMEOUT_SECONDS = 10L
    const val MAX_REQUESTS_PER_MINUTE = 5
    
    // Storage
    const val CACHE_EXPIRY_HOURS = 24L
    const val MAX_CACHE_SIZE_MB = 10L
    const val GPX_DIRECTORY = "GPX/"
    
    // Preferences
    const val PREFS_NAME = "DistinctRoutesPrefs"
    const val KEY_SELECTED_CITY = "selected_city"
    const val KEY_SAFETY_PREFERENCES = "safety_prefs"
    const val KEY_LANGUAGE = "language"
    
    // UI
    const val MIN_GPS_ACCURACY_METERS = 10f
    const val ROUTE_CALCULATION_TIMEOUT_MS = 10000L
    
    // Notifications
    const val NOTIFICATION_CHANNEL_ID = "route_calculation"
    const val NOTIFICATION_ID_EXPORT = 1001
    const val NOTIFICATION_ID_ERROR = 1002
}

APPENDIX C: ALTERNATIVES CONSIDERED
Alternative	Why Rejected
Google Maps SDK	Billing required, closed-source, violates ethos
Native turn-by-turn engine	40+ hours dev time, OsmAnd already solves this
Realm Database	Unnecessary for v1 (no user accounts)
RxJava	Coroutines simpler for this scale
Kotlin Flow only	LiveData better for Android lifecycle awareness
Room Database	Overkill for simple key-value preferences
Technical Architecture — Version 1.0
Next Review: 2026-11-01 (Pre-beta)

END OF TECHNICAL ARCHITECTURE