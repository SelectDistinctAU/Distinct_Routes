# API INTEGRATION SPECIFICATION — DISTINCT ROUTES
**BBBike Routing API**

**Version:** 1.0  
**Last Updated:** 2026-10-11  
**Platform:** Android (Kotlin)  
**Contact:** distinctroutes@pm.me

---

## OVERVIEW

This document defines the technical integration requirements for BBBike routing API in the Distinct Routes Android application.

**Primary Purpose:** Retrieve safety-aware cycling route data between two geographic points.

**Integration Model:** HTTP GET requests → GPX parsing → Local rendering → OsmAnd handoff

---

## API ENDPOINTS

### Base URL

https://www.bbbike.org/bbbike/


### Route Calculation Endpoint

GET /bbbike/?from={from}&to={to}&profile={profile}[&language={lang}]


**Full Example:**

https://www.bbbike.org/bbbike/?from=52.5200,13.4050&to=52.5210,13.4100&profile=bike&language=en


---

## QUERY PARAMETERS

### Required Parameters

| Parameter | Type | Format | Description | Example |
|-----------|------|--------|-------------|---------|
| `from` | String | lat,lng OR address | Start location coordinates or address | `52.5200,13.4050` or `Alexanderplatz,Berlin` |
| `to` | String | lat,lng OR address | End location coordinates or address | `52.5210,13.4100` or `Brandenburg Gate,Berlin` |
| `profile` | String | Predefined value | Routing preference/profile | `bike`, `fast`, `shortest`, `quiet` |

### Optional Parameters

| Parameter | Type | Format | Description | Default |
|-----------|------|--------|-------------|---------|
| `language` | String | ISO 639-1 code | Response locale for instructions | `de` (Berlin default) |
| `via` | String | lat,lng | Intermediate waypoint(s) | None |
| `avoid` | String | Comma-separated | Features to avoid (traffic_lights, hills, highways) | None |
| `format` | String | `gpx`, `geojson`, `kml` | Output format | `gpx` |

### Supported Profiles

| Profile ID | Name | Description | Use Case |
|------------|------|-------------|----------|
| `bike` | Standard Bike | Balanced safety/speed | Default for most users |
| `fast` | Fast | Minimise travel time | Time-sensitive commutes |
| `shortest` | Shortest | Minimise distance | Quick trips |
| `quiet` | Quiet | Prioritise low-traffic routes | Safety-focused users |
| `mtb` | Mountain Bike | Off-road capable trails | Trail riding |
| `cargo` | Cargo Bike | Avoids steep gradients | Heavy load/e-bike users |

**Profile Mapping to Safety Preferences:**

| User Preference | BBBike Profile | Additional Flags |
|-----------------|----------------|------------------|
| Standard (balanced) | `bike` | None |
| Avoid traffic lights | `quiet` | `avoid=traffic_lights` |
| Avoid highways | `quiet` | `avoid=highways` |
| Avoid steep hills | `cargo` | Increased gradient penalty |
| Dog trailer mode | `cargo` | Weight penalty multiplier 1.5x |

---

## RESPONSE FORMAT

### Successful Response (HTTP 200)

**Content-Type:** `application/gpx+xml` or `application/geojson`

**Sample GPX Response:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<gpx version="1.1" creator="BBBike">
  <metadata>
    <name>Cycling Route</name>
    <time>2026-10-11T14:30:00Z</time>
  </metadata>
  <trk>
    <name>Berlin Route</name>
    <trkseg>
      <trkpt lat="52.5200" lon="13.4050"><ele>35</ele></trkpt>
      <trkpt lat="52.5205" lon="13.4065"><ele>36</ele></trkpt>
      <trkpt lat="52.5210" lon="13.4100"><ele>37</ele></trkpt>
    </trkseg>
  </trk>
  <extensions>
    <distance>1250</distance>
    <duration>360</duration>
  </extensions>
</gpx>

Metadata Fields:

Field	Type	Description
distance	Integer	Total distance in metres
duration	Integer	Estimated travel time in seconds
gradient_max	Float	Maximum gradient percentage
traffic_light_count	Integer	Number of traffic signals crossed
highway_segments	Integer	Highway/path segments avoided
Error Responses
HTTP Code	Error Type	User Message	Retry Action
200	Success	Route calculated	None
400	Bad Request	Invalid coordinates or address	Do not retry
401	Unauthorized	API key required (if applicable)	Check credentials
403	Forbidden	Access denied	Contact BBBike support
429	Too Many Requests	Rate limit exceeded	Wait 30 seconds + retry
500	Server Error	Internal server error	Exponential backoff
502	Bad Gateway	Upstream server error	Exponential backoff
503	Service Unavailable	Temporarily down	Exponential backoff
RATE LIMITING
Known Limits
Limit Type	Threshold	Window
Documented	Unknown (not publicly stated)	N/A
Implemented	5 requests/minute/user	Sliding window
Throttling Strategy
// Pseudo-code for request queue with rate limiting
class BBBikeApiClient {
    private val requestQueue = Queue<Request>()
    private var lastRequestTime: Instant = Instant.MIN
    
    fun calculateRoute(from: Point, to: Point): Route {
        // Check rate limit
        val timeSinceLastRequest = Instant.now() - lastRequestTime
        if (timeSinceLastRequest < 12.seconds) {
            wait(12.seconds - timeSinceLastRequest)
        }
        
        // Execute request
        val response = makeHttpRequest(from, to)
        lastRequestTime = Instant.now()
        
        return parseResponse(response)
    }
}

Exponential Backoff on 429/5xx:

Attempt	Wait Time	Max Retries
1st failure	5 seconds	3
2nd failure	15 seconds	3
3rd failure	30 seconds	3
4th failure	Show error to user	—
ERROR HANDLING
Client-Side Validation
Before sending request, validate inputs:

fun validateCoordinates(point: Point): Boolean {
    return point.lat in -90.0..90.0 && 
           point.lon in -180.0..180.0 &&
           point.accuracy <= 10.0 // GPS accuracy threshold
}

Network Failure Recovery
Failure Handling Flow:
1. Detect timeout/connection error
2. Check if request was in-flight (partial response)
3. If yes, discard partial data
4. If no, queue for retry
5. Show "Network unavailable" message
6. Offer manual retry button
7. Log incident (local debug only)

Parsing Errors
GPX Parse Failure Recovery:
1. Validate XML structure
2. Check for required trkseg element
3. Verify minimum 2 trackpoints exist
4. If invalid, fall back to GeoJSON parser
5. If both fail, show "Routing service unavailable"

CACHING STRATEGY
Cache Policy
Data Type	Cache Duration	Storage Location	Eviction
City list	7 days	SharedPreferences	Auto-refresh on expiry
Route results	24 hours	DiskLruCache (10MB)	LRU eviction
Error responses	5 minutes	Memory cache	Auto-expiry
Cache Implementation
object CacheManager {
    private const val CACHE_SIZE_MB = 10L
    private const val ROUTE_CACHE_EXPIRY_HOURS = 24L
    
    fun getRouteFromCache(cacheKey: String): Route? {
        val cachedEntry = diskCache.get(cacheKey)
        
        if (cachedEntry == null || 
            cachedEntry.timestamp < System.currentTimeMillis() - 
                                   TimeUnit.HOURS.toMillis(ROUTE_CACHE_CACHE_EXPIRY_HOURS)) {
            return null
        }
        
        return deserialize(cachedEntry.data)
    }
    
    fun cacheRoute(key: String, route: Route) {
        diskCache.put(
            key,
            serialize(route),
            System.currentTimeMillis()
        )
    }
}

Cache Key Format:

SHA256(from_lat,from_lon,to_lat,to_lon,profile,safety_flags)

CITY LIST ENDPOINT
Retrieving Available Cities
Endpoint:

GET https://www.bbbike.org/bbbike/cities

Response Format: JSON array

[
  {
    "name": "Berlin",
    "country": "Germany",
    "bbox": [13.088, 52.338, 13.760, 52.675],
    "center": [13.405, 52.520]
  },
  {
    "name": "London",
    "country": "United Kingdom",
    "bbox": [-0.351, 51.286, 0.267, 51.691],
    "center": [-0.128, 51.507]
  }
]

Usage:

Fetch on app first launch
Store in SharedPreferences (7-day cache)
Populate city selection dropdown
Use bbox for map initial zoom
GEOCODING (ADDRESS → COORDINATES)
Forward Geocoding
Convert address text to coordinates.

Endpoint:

GET https://nominatim.openstreetmap.org/search?format=json&q={address}

Parameters:

Parameter	Required	Description
q	Yes	Search query (address)
format	Yes	Output format (json)
limit	No	Max results (default 10)
countrycodes	No	ISO 2-letter country filter
Response:

[
  {
    "place_id": 123456,
    "display_name": "Alexanderplatz, Berlin, Germany",
    "lat": "52.5200",
    "lon": "13.4050",
    "boundingbox": ["52.5190", "52.5210", "13.4040", "13.4060"]
  }
]

Reverse Geocoding
Convert coordinates to address text.

Endpoint:

GET https://nominatim.openstreetmap.org/reverse?format=json&lat={lat}&lon={lon}

Rate Limit: 1 request/second maximum

TIMEOUT CONFIGURATION
Setting	Value	Reason
Connect Timeout	10 seconds	Initial handshake delay
Read Timeout	10 seconds	Route calculation max time
Write Timeout	5 seconds	GPX export operation
Total Request Timeout	15 seconds	Hard timeout (fail-safe)
Implementation (OkHttp):

val client = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(5, TimeUnit.SECONDS)
    .build()

SECURITY CONSIDERATIONS
HTTPS Enforcement
All API requests must use HTTPS only.

// Block cleartext traffic in AndroidManifest.xml
<application
    android:usesCleartextTraffic="false">

Input Sanitisation
Sanitise user-provided addresses before geocoding:

fun sanitiseAddressInput(input: String): String {
    return input
        .trim()
        .replace("<[^>]*>".toRegex(), "") // Remove HTML tags
        .take(200) // Max length
}

No API Keys Required (Current)
BBBike public API does not require authentication. However:

Monitor for API key introduction (future)
Prepare for rate-limiting escalation
Consider API key registration if BBBike requests it
TESTING SCENARIOS
Unit Test Cases
Test ID	Scenario	Expected Result
API01	Valid coordinates	Returns GPX with ≥2 trackpoints
API02	Invalid coordinates (out of bounds)	Returns HTTP 400
API03	Same start/end point	Returns error (cannot calculate)
API04	Profile: bike	Returns balanced route
API05	Profile: quiet	Returns low-traffic route
API06	Missing language param	Returns German (default)
API07	Language: zh (Chinese)	Returns UTF-8 encoded response
API08	Network timeout	Throws TimeoutException
API09	Rate limit (429)	Waits 30s then retries
API10	Server error (500)	Exponential backoff
Integration Test Cases
Test ID	Scenario	Expected Result
INT01	Full route flow	GPX exports successfully
INT02	GPS coordinates → Route	Accurate polyline displayed
INT03	Address search → Route	Geocoded correctly
INT04	City list load	200+ cities populated
INT05	Cache hit	Returns from local storage (<10ms)
INT06	Cache miss	Fetches from API (<10s)
DEPENDENCY MAP
API Call Chain
User Action
    ↓
RoutePlannerViewModel
    ↓
CalculateRouteUseCase
    ↓
RouteRepositoryInterface
    ↓
RouteRepositoryImpl
    ↓
BBBikeApiService (Retrofit)
    ↓
BBBike API (HTTPS)
    ↓
GPX Parser
    ↓
Route Model

Library Dependencies
// build.gradle.kts
dependencies {
    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    
    // GPX Parsing
    implementation("io.github.mikaelgrenberg:gpxparser:0.2.0")
    
    // JSON (for city list/geocoding)
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
}

MONITORING & METRICS
Request Logging (Debug Only)
// Log request details for debugging (disabled in production)
interface ApiLogger {
    fun logRequest(
        endpoint: String,
        method: String,
        parameters: Map<String, String>,
        startTime: Long
    )
    
    fun logResponse(
        statusCode: Int,
        durationMs: Long,
        error: String?
    )
}

Success Metrics
Metric	Target	Measurement
Route calculation success rate	> 95%	HTTP 200 / total requests
Average response time	< 5 seconds	Timestamp diff
Cache hit rate	> 60%	Cached / total requests
Error rate	< 5%	4xx + 5xx / total requests
BACKWARD COMPATIBILITY
API Versioning
BBBike API currently has no explicit versioning.

Mitigation Strategy:

Wrap all API calls in repository interface
Monitor BBBike changelog for breaking changes
Maintain fallback to v1 if v2 introduced
Update repository adapter without touching ViewModel
Deprecation Handling
If BBBike announces API deprecation:

Phase 1 (Announcement): Continue using current endpoint
Phase 2 (Warning period): Add migration code in parallel
Phase 3 (Shutdown): Switch to new endpoint
Communication: Announce to users via Google Groups
APPENDIX A: PROFILE MAPPING TABLE
User Safety Preference	BBBike Profile	Additional Query Params
Standard	bike	None
Avoid traffic lights	quiet	avoid=traffic_lights
Avoid highways	quiet	avoid=highways
Avoid steep hills	cargo	Gradient penalty 1.5x
Dog trailer mode	cargo	Weight penalty 1.5x
E-bike cargo	cargo	Gradient penalty 1.25x
Time-sensitive	fast	None
Minimal walking	bike	Avoid footpaths
APPENDIX B: ERROR CODE REFERENCE
Code	Meaning	User-Facing Message	Owner Action
400	Bad Request	"Invalid location entered. Please check coordinates."	None
401	Unauthorized	"API authentication required. Contact support."	Escalate to owner
403	Forbidden	"Access to routing service denied."	Escalate to owner
429	Rate Limited	"Too many requests. Try again in 30 seconds."	None (wait)
500	Server Error	"Routing service temporarily unavailable."	Retry with backoff
502	Bad Gateway	"Gateway error. Please retry."	Retry with backoff
503	Unavailable	"Service down. Try again later."	Retry with backoff
Timeout	Connection timed out	"Connection took too long. Retry?"	Retry once
APPENDIX C: SAMPLE REQUESTS
Berlin Commute Route
GET https://www.bbbike.org/bbbike/?from=52.5200,13.4050&to=52.5210,13.4100&profile=bike&language=en

London to Westminster (Address)
GET https://www.bbbike.org/bbbike/?from=London,Euston+Station&to=London,Westminster+Abbey&profile=quiet&language=en

Paris with Safety Preferences
GET https://www.bbbike.org/bbbike/?from=48.8566,2.3522&to=48.8606,2.3376&profile=cargo&language=fr&avoid=highways,hills

APPENDIX D: CHANGELIST LOG
Version	Date	Changes	Author
1.0	2026-10-11	Initial specification	ASAI
TBD	TBD	Pending BBBike API confirmation	TBD
API Integration Specification — Version 1.0
Next Review: 2026-11-01 (Pre-beta)

END OF API INTEGRATION SPECIFICATION