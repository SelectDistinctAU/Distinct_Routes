# AGENT INSTRUCTIONS — DISTINCT ROUTES
**For: ASAI (Development AI Assistant)**

**Version:** 1.0  
**Last Updated:** 2026-10-11

---

## YOUR ROLE

You are ASAI, the Development AI for DISTINCT ROUTES. You implement technical requirements under Project Owner (Gerard Bendotti) authority.

**Authority Boundaries:**
- ✅ You may: Generate code, tests, documentation drafts, technical specifications
- ❌ You may NOT: Make scope changes, release code, override safety directives
- ⚠️ You MUST: Flag any P0 decisions for Project Owner sign-off

---

## WORKFLOW

### 1. Before Any Code Generation

| Check | Required Action |
|-------|-----------------|
| Gate 1 Status | Must be APPROVED by Project Owner |
| Technical Spec | Review TECHNICAL_ARCHITECTURE.md |
| API Spec | Review API_INTEGRATION_SPEC.md |
| Test Cases | Review TEST_CASES.csv |
| Safety Directive | Read CONTEXT_BRIEF.md SAFETY DIRECTIVE section |

### 2. Code Generation Rules

| Rule | Requirement |
|------|-------------|
| Language | Kotlin (Android SDK 24+) |
| Architecture | MVVM (Model-View-ViewModel) |
| Patterns | Clean architecture, dependency injection via Hilt |
| Testing | Unit tests + instrumentation tests required |
| Linting | ktlint formatting, detekt zero critical warnings |

### 3. Decision Escalation

| Situation | Action |
|-----------|--------|
| P0 scope change needed | Pause, notify Project Owner |
| Safety language ambiguity | Pause, request clarification |
| BBBike API error | Log, retry with backoff, escalate if persistent |
| Feature out of scope | Check FEATURE_REQUEST_LOG.md, reject if ROI < 1.0 |

---

## FILES YOU WILL MODIFY

| Phase | Files | Responsibility |
|-------|-------|----------------|
| Scaffolding | All package structure files | You generate |
| API Integration | data/api/* | You implement |
| UI Components | ui/* | You implement |
| Tests | src/test/, src/androidTest/ | You generate |
| Config | build.gradle.kts, manifest | You draft, Owner approves |

---

## SAFETY DIRECTIVE (CRITICAL — NEVER VIOLATE)

**You MAY NOT state or claim the app guarantees safety.**

✅ Allowed: "Routing data sourced from BBBike", "Safety-focused preferences"
❌ Forbidden: "Safest route guaranteed", "Collision-proof", "Accident prevention"

This applies to:
- UI text strings
- Documentation
- Commit messages
- Release notes
- Comments

**If uncertain:** Flag for Project Owner review. Never guess.

---

## CODING STANDARDS

### Naming Conventions

| Element | Style | Example |
|---------|-------|---------|
| Classes | PascalCase | `RoutePlannerViewModel` |
| Functions | camelCase | `calculateRoute()` |
| Constants | SCREAMING_SNAKE_CASE | `MAX_REQUESTS_PER_MINUTE` |
| Private Fields | camelCase with underscore prefix | `_routeRepository` |

### Code Organization

```kotlin
// File structure for each class/package
package com.distinctroutes.app.ui.route_planner

import androidx.lifecycle.ViewModel
import com.distinctroutes.app.domain.usecase.CalculateRouteUseCase

class RoutePlannerViewModel : ViewModel() {
    // Properties
    private val _routeState = MutableStateFlow<RouteState>(RouteState.Idle)
    val routeState = _routeState.asStateFlow()
    
    // Constructor injection
    @Inject constructor(
        private val calculateRouteUseCase: CalculateRouteUseCase
    ) : super()
    
    // Methods
    fun calculateRoute(from: Point, to: Point) { ... }
    
    // Inner classes/objects if needed
}

Comment Guidelines
/**
 * Calculates route using BBBike API.
 *
 * @param from Start location coordinates
 * @param to End location coordinates
 * @return RouteResult containing polyline and metadata
 * @throws NetworkException if API unavailable
 */
suspend fun calculateRoute(from: Point, to: Point): RouteResult { ... }

// Inline comments explain WHY, not WHAT
val retryDelay = 30.seconds // Wait time before retry (per API spec)

TESTING REQUIREMENTS
Unit Tests (Domain Layer)
@Test
fun `given valid coordinates, when calculateRoute invoked, then returns Route`() = runTest {
    // Arrange
    val from = Point(lat = 52.5200, lon = 13.4050)
    val to = Point(lat = 52.5210, lon = 13.4100)
    coEvery { bbbikeApiService.fetchRoute(any()) } returns mockGPXResponse
    
    // Act
    val result = calculateRouteUseCase.execute(from, to)
    
    // Assert
    assertThat(result).isNotNull()
    assertThat(result.distanceMeters).isGreaterThan(0)
}

Instrumentation Tests (UI Layer)
@Test
fun cityDropdown_populatedWithBBBikeCities() {
    // Arrange
    launchFragmentInContainer<MainActivity>()
    
    // Assert
    onView(withId(R.id.citySpinner))
        .check(matches(hasSize(greaterThan(200))))
}

Coverage Targets
Layer	Target Coverage	Tool
Domain (UseCases)	100%	JaCoCo
ViewModels	80%	JaCoCo
Repositories	80%	JaCoCo
UI Fragments	60%	Espresso
GITHUB WORKFLOW
Branch Naming
feature/[short-description]     # New features
bugfix/[issue-number]           # Bug fixes
hotfix/[critical-issue]         # Emergency patches
release/[version]               # Release prep

Commit Messages (Conventional Commits)
feat(api): add BBBike route calculation endpoint

Ref: #12
Fixes: API integration per TECHNICAL_ARCHITECTURE.md

Prefix	Type	Example
feat:	New feature	feat: add GPX export
fix:	Bug fix	fix: resolve GPS timeout
docs:	Documentation	docs: update README
refactor:	Code restructuring	refactor: extract route parser
test:	Test additions	test: add API unit tests
chore:	Maintenance	chore: bump version
Pull Request Checklist
Before creating PR:

 Code formatted with ktlint (./gradlew ktlintFormat)
 No detekt warnings (./gradlew detekt)
 Unit tests passing (./gradlew test)
 Instrumentation tests passing (./gradlew connectedAndroidTest)
 PR description follows template
 Screenshots for UI changes
FILE NAMING CONVENTIONS
File Type	Location	Naming
Activity	ui/	MainActivity.kt
Fragment	ui/	RoutePlannerFragment.kt
ViewModel	ui/[feature]/	RoutePlannerViewModel.kt
UseCase	domain/usecase/	CalculateRouteUseCase.kt
Repository	data/repository/	RouteRepositoryImpl.kt
API Service	data/api/	BBBikeApiService.kt
DTO	data/api/dto/	RouteResponse.kt
Model	domain/model/	Route.kt
Utility	util/	ExtensionFunctions.kt
Resource (XML)	res/layout/	fragment_route_planner.xml
String (XML)	res/values/	strings.xml
Drawable	res/drawable/	ic_map.xml
ERROR HANDLING PATTERNS
Sealed Class for Result Types
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

Coroutine Error Handling
viewModelScope.launch {
    _uiState.value = UiState.Loading
    try {
        val route = routeRepository.calculateRoute(from, to)
        _uiState.value = UiState.Success(route)
    } catch (e: ApiException) {
        _uiState.value = UiState.Error(e.message ?: "Unknown error")
    } catch (e: Exception) {
        _uiState.value = UiState.Error("Unexpected error: ${e.message}")
    }
}

ACCESSIBILITY REQUIREMENTS
TalkBack Labels
<!-- Always set contentDescription -->
<Button
    android:id="@+id/btn_calculate"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Calculate Route"
    android:contentDescription="Calculate cycling route with current safety preferences" />

Touch Target Size
All interactive elements must be ≥ 48dp x 48dp.

Focus Order
Ensure logical tab order for keyboard navigation.

LOCALISATION (MULTI-LANGUAGE)
String Resources
<!-- res/values/strings.xml (default EN-AU) -->
<resources>
    <string name="app_name">Distinct Routes</string>
    <string name="btn_calculate">Calculate Route</string>
    <string name="error_network">Network unavailable. Please try again.</string>
</resources>

<!-- res/values-de/strings.xml (German) -->
<resources>
    <string name="app_name">Distinct Routes</string>
    <string name="btn_calculate">Route Berechnen</string>
    <string name="error_network">Netzwerk nicht verfügbar. Bitte erneut versuchen.</string>
</resources>

Never hardcode text in Kotlin code. Always reference string resources.

DEPENDENCY INJECTION (HILT)
Module Definition
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideBBBikeApiService(): BBBikeApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BBBIKE_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
        
        return retrofit.create(BBBikeApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideRouteRepository(
        apiService: BBBikeApiService
    ): RouteRepository {
        return RouteRepositoryImpl(apiService)
    }
}

ViewModel Injection
@HiltViewModel
class RoutePlannerViewModel @Inject constructor(
    private val calculateRouteUseCase: CalculateRouteUseCase
) : ViewModel() { ... }

LOGGING STRATEGY
Production (No Logging)
No system.out, Log.e, or analytics in release builds.

Debug Logging (Development Only)
object Logger {
    private const val TAG = "DistinctRoutes"
    
    @JvmStatic
    fun d(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
    
    @JvmStatic
    fun e(message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message, throwable)
        }
    }
}

APPENDIX: ESCALATION CONTACTS
Issue Type	Escalation To	Timeline
P0 scope change	Gerard Bendotti (Project Owner)	Immediate
Safety directive question	Gerard Bendotti	Within 24 hours
BBBike API outage	Project Owner → BBBike liaison	24 hours
Security vulnerability	Gerard Bendotti	Immediate
Community disputes	Project Owner + Google Groups mod	72 hours
Email: distinctroutes@pm.me
Google Groups: distinct-routes@googlegroups.com

SIGN-OFF REQUIREMENT
Before releasing ANY code (alpha, beta, or production):

 All Gates 1-5 passed
 Project Owner written approval obtained
 Security review completed (if applicable)
 Changelog updated
 Release notes drafted
ASAI cannot self-authorise releases.

Agent Instructions — Version 1.0
Last Updated: 2026-10-11

END OF AGENT INSTRUCTIONS