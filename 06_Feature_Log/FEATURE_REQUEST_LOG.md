# FEATURE REQUEST LOG — DISTINCT ROUTES
**Purpose:** Track out-of-scope feature requests with ROI justification
**Version:** 1.0  
**Last Updated:** 2026-10-11  
**Owner:** Gerard Bendotti (Project Owner)

---

## REQUEST LOG

| ID | Feature | Requestor | Description | ROI Justification | Investment Cost | Score | Decision | Target Release |
|----|---------|-----------|-------------|-------------------|-----------------|-------|----------|----------------|
| FR001 | Offline maps | Community (GitHub Issue #1) | "Users need maps in tunnels/subways without internet" | Medium utility for underground areas | 40 hours dev + 100 MB APK increase | 0.66 | Deferred | v2+ |
| FR002 | Multi-language v1 launch | Gerard Bendotti | "Non-English commuters need safety routing" | High inclusivity priority, owner validates demand | 80 hours i18n + translation effort | 1.20 | Approved | v1 |
| FR003 | Voice navigation | ASAI | "Hands-free riding essential for safety" | Low value — OsmAnd already provides this via handoff | Defer to OsmAnd (handled externally) | 0.10 | Rejected | Never (in-app) |
| FR004 | iOS port | Community (Google Groups) | "iPhone cyclists excluded from safety tool" | Medium demand but high dev effort | 80 hours Swift dev + separate codebase | 0.40 | Rejected | v3+ |
| FR005 | Weather alerts | Gerard Bendotti | "Wind affects cargo bike safety significantly" | Medium-high safety relevance for e-bike users | 30 hours + API complexity + battery drain | 0.90 | Deferred | v2+ |
| FR006 | Hazard reporting | Community (Forum Thread #3) | "Crowd-sourced safety data improves routing" | Medium community value but moderation burden | 60 hours + ongoing moderation + legal liability | 0.70 | Deferred | v2+ |
| FR007 | Dog trailer weight mode | Gerard Bendotti | "Cargo dynamics affect hill climbing safety" | High personal use case validates demand | 8 hours routing config adjustment | 1.50 | Approved | v1 |
| FR008 | Elevation profile chart | ASAI | "Climbers need gradient awareness before committing" | Medium utility for tandem/hill riders | 12 hours chart library integration | 0.80 | Deferred | v2+ |
| FR009 | Social route sharing | Community (Feature Request #5) | "Share routes with friends easily" | Low utility — GPX export already handles this | 20 hours social integration + privacy concerns | 0.30 | Rejected | Never |
| FR010 | Subscription model | Community (Discussion #2) | "Revenue sustains development long-term" | Contradicts free-for-ever ethos | 40 hours billing setup + excludes financially disadvantaged | 0.20 | Rejected | Never |
| FR011 | Trip history tracking | ASAI | "Track cycling habits over time" | Low privacy-aligned value, violates no-tracking principle | 15 hours + GDPR burden + contradicts ethos | 0.15 | Rejected | Never |
| FR012 | Berlin closure data | BBBike (potential) | "Real-time road closures improve routing" | High Berlin-specific value, limited elsewhere | Requires BBBike API partnership + ongoing maintenance | 1.10 | Deferred | v2+ (if endorsed) |
| FR013 | Fitness stats integration | Community (GitHub Issue #7) | "Calories burned, heart rate metrics" | Low utility for commuter segment, adds complexity | 25 hours + sensor permissions + health data privacy | 0.25 | Rejected | v3+ |
| FR014 | Dark mode | ASAI | "Night cyclists need OLED-friendly UI" | Medium accessibility value, easy to implement | 4 hours Material Components theming | 1.80 | Approved | v1 |
| FR015 | Battery optimization mode | ASAI | "Long commutes drain phone battery" | Medium utility for power users | 10 hours background service optimization | 0.85 | Deferred | v2+ |
| FR016 | Ride scheduling | Community (Forum Thread #9) | "Plan commute departure times in advance" | Medium planner value, low daily utility | 18 hours scheduler + alarm integration | 0.45 | Deferred | v2+ |
| FR017 | Emergency contact alert | ASAI | "Auto-alert contacts if accident detected" | High safety value but complex implementation | 50 hours + accelerometer sensors + false positive risk | 0.75 | Rejected | v3+ (requires external review) |
| FR018 | Multilingual voice hints | Community (Translation request) | "Navigation prompts in user's language" | High utility for tourists | Defer to OsmAnd (handles this natively) | 0.10 | Rejected | Never (in-app) |
| FR019 | Bicycle type selection | Gerard Bendotti | "Road bike vs mountain bike affects route choice" | Medium routing relevance, minimal code effort | 6 hours routing profile adjustment | 1.30 | Approved | v1 |
| FR020 | Crash reporting (opt-in) | ASAI | "Identify bugs faster with analytics" | Medium dev efficiency value, privacy trade-off | 8 hours Firebase setup + GDPR consent flow | 0.55 | Deferred | v2+ (opt-in only) |

---

## ROI FORMULA

**Priority Score Calculation:**

Score = (Benefit × Impact × Reach) / (Time × Complexity)

Where:

Benefit: Safety value (1-10), usability value (1-10), or strategic value (1-10)
Impact: User segment size (1-10 scale)
Reach: Geographic/demographic coverage (1-10 scale)
Time: Development hours estimated
Complexity: Technical difficulty multiplier (1-5)

**Threshold for v1 Inclusion:** Score ≥ 1.0  
**Approval Authority:** Project Owner (Gerard Bendotti)

---

## DECISION SUMMARY

| Decision | Count | Percentage |
|----------|-------|------------|
| Approved (v1) | 4 | 20% |
| Deferred (v2+) | 7 | 35% |
| Rejected (never/in-app) | 7 | 35% |
| Pending Owner Review | 2 | 10% |
| **Total** | **20** | **100%** |

**Approved v1 Features:**
- FR002: Multi-language v1 launch
- FR007: Dog trailer weight mode
- FR014: Dark mode
- FR019: Bicycle type selection

---

## APPROVED FEATURES — IMPLEMENTATION NOTES

### FR002: Multi-language (v1 Launch)

| Locale | Status | Responsibility | Deadline |
|--------|--------|----------------|----------|
| EN-AU | Seeded | Project Owner | Gate 1 sign-off |
| EN-GB | Seeded | Project Owner | Gate 1 sign-off |
| EN-US | Seeded | Project Owner | Gate 1 sign-off |
| DE | Seeded | Project Owner | Gate 1 sign-off |
| IT | Community | Volunteers | Beta release |
| ES | Community | Volunteers | Beta release |
| PT | Community | Volunteers | Beta release |
| FR | Community | Volunteers | Beta release |
| ZH | Community | Volunteers | Beta release |

### FR007: Dog Trailer Weight Mode

```kotlin
// Pseudo-code for hill penalty adjustment
fun calculateHillPenalty(basePenalty: Double, hasCargo: Boolean): Double {
    return if (hasCargo) basePenalty * 1.5 else basePenalty
}

Acceptance Criteria:

Cargo mode increases hill penalty by 50%
Toggle visible in safety preferences
Persisted across app restarts
FR014: Dark Mode
Implementation:

Follow Material Components Night theme (DayNight)
Respect system preference by default
Allow manual override in settings
Test with OLED displays for battery savings
Acceptance Criteria:

Dark theme activates at sunset OR manually
All UI elements contrast ≥ 4.5:1 ratio
No layout breakage in dark mode
FR019: Bicycle Type Selection
Profile Options:

Type	Penalty Adjustment	Rationale
Road Bike	Default	Standard routing weights
Mountain Bike	Reduce hill penalty by 10%	Better hill/climb capability
Cargo/E-bike	Increase hill penalty by 50%	Weight affects climbing
Tandem	Increase hill penalty by 75%	Two riders = double weight
Acceptance Criteria:

Dropdown visible in city selection
Profile persists across sessions
Route recalculates when changed
DEFERRED FEATURES — RE-ASSESSMENT CRITERIA
ID	Feature	Re-assessment Trigger	Review Date
FR001	Offline maps	1000+ active users, APK size < 10 MB	2027-06-01
FR005	Weather alerts	BBBike partnership confirmed, API cost < $50/mo	2027-03-01
FR006	Hazard reporting	Community governance model defined	2027-01-01
FR008	Elevation profile chart	Chart library performance validated	2027-01-01
FR012	Berlin closure data	BBBike endorsement received	Post-partnership
FR015	Battery optimization	Power usage analysis shows >15% drain	2027-03-01
FR016	Ride scheduling	100+ users request same feature	2027-01-01
FR020	Crash reporting (opt-in)	Privacy review completes, consent flow designed	2027-01-01
REJECTED FEATURES — JUSTIFICATION
ID	Feature	Primary Reason	Secondary Concern
FR003	Voice navigation	OsmAnd already provides this	Duplication of effort
FR004	iOS port	Scope exceeds v1 timeline	Separate codebase maintenance
FR009	Social route sharing	GPX export already handles this	Privacy/data sharing concerns
FR010	Subscription model	Contradicts free-for-ever ethos	Excludes financially disadvantaged
FR011	Trip history tracking	Violates no-tracking principle	GDPR compliance burden
FR013	Fitness stats integration	Low commuter utility	Health data privacy complexity
FR018	Multilingual voice hints	OsmAnd already provides this	Audio asset storage bloat
FR017	Emergency contact alert	High complexity, false positives	Legal liability exposure
NEW REQUEST INTAKE
New feature requests must be logged here with ROI justification before ASAI implements.

Intake Form Template
---
Feature Request ID: [Auto-assigned: FRxxx]
Feature Name: [Short descriptive title]
Requestor: [Username/Email]
Description: [Detailed description of requested feature]
Benefit Justification: [Safety value? Usability improvement? Strategic need?]
Estimated User Base: [% of users who would benefit]
Competing Alternatives: [How do users solve this now?]
---

ROI Calculation:
Benefit Score (1-10): ____
Impact Score (1-10): ____
Reach Score (1-10): ____
Estimated Hours: ____
Complexity Multiplier (1-5): ____

Proposed Score: (Benefit × Impact × Reach) / (Hours × Complexity) = ____

Owner Decision: [ ] Approved [ ] Deferred [ ] Rejected
Decision Date: ____
Decision Notes: [Rationale]
---

APPENDIX: FEATURE REQUEST STATISTICS
Metric	Value
Total requests logged	20
Approved for v1	4 (20%)
Deferred to v2+	7 (35%)
Rejected permanently	7 (35%)
Pending review	2 (10%)
Average ROI score (approved)	1.20
Average ROI score (deferred)	0.79
Average ROI score (rejected)	0.28
Feature Request Log — Version 1.0
Next Review: 2026-11-01 (Pre-beta)

END OF FEATURE REQUEST LOG