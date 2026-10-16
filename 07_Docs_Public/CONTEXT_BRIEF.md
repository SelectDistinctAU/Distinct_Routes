# CONTEXT BRIEF — DISTINCT ROUTES
## For AI Assistants (ASAI, Lumo, etc.)

**Last Updated:** 2026-10-11  
**Reading Time:** 5 minutes  
**Version:** 1.0

---

## MISSION STATEMENT

Build a free, open-source Android application that delivers BBBike's safety-aware routing data to mobile cyclists. The app must work reliably. **Safety outcomes are influenced by routing accuracy and clarity.**

---

## COMPANY & CONTACT

| Detail | Value |
|--------|-------|
| **Company** | SELECT DISTINCT |
| **Project Name (Consumer)** | DISTINCT ROUTES |
| **Project Name (Legal)** | SELECT DISTINCT Bike Navigator |
| **Contact Email** | distinctroutes@pm.me |
| **Google Groups** | distinct-routes@googlegroups.com |
| **GitHub** | github.com/select-distinct/distinct-routes |
| **Licence** | GPL-3.0 Open Source |
| **Platform** | Android 7.0+ (API 24+) |

---

## BACKGROUND

- **Started:** October 2026
- **Motivating Incident:** Close-calls caused multi-year cycling hiatus; BBBike routing found safer routes than 25 years local knowledge could identify. User returned to cycling.
- **Inspiration:** BBBike's original language: "We'll help you find a nice, safe and short bicycle route in and around your city."
- **Ethos:** Privacy-first, open-source, no telemetry, no user accounts required

---

## CURRENT STATUS

| Phase | Date | Description |
|-------|------|-------------|
| **Governance & Definition** | Oct 2026 | No code written. All documentation complete. |
| **Development** | Nov 2026 (target) | ASAI scaffolding and implementation |
| **Beta Testing** | Nov 2026 | Internal testers |
| **Public Release** | Dec 2026 (pending BBBike endorsement) | Google Play + F-Droid |

**Gate to Code:** ALL governance documents signed off before first line of code.

---

## KEY DECISIONS (ALREADY MADE)

| Decision | Status | Rationale |
|----------|--------|-----------|
| App name: Distinct Routes (consumer) | Approved | Shortest, most memorable |
| Tech: BBBike API → GPX → OsmAnd handoff | Approved | Delegates navigation complexity |
| Scope v1: Route planner + GPX export only | Approved | Focus on routing accuracy |
| Licence: GPL-3.0 | Approved | Ensures derivatives remain open-source |
| Languages v1: EN-AU, EN-GB, EN-US, DE, IT, ES, PT, FR, ZH | Approved | Inclusivity priority |
| Accessibility: TalkBack enabled from v1 | Approved | Vision-impaired support mandatory |
| Weather integration: Deferred to v2 | Deferred | Global API complexity exceeds v1 scope |
| Dog trailer support: Weight/cargo mode | Approved | Owner's personal use case validates demand |
| Safety language: No guarantees claimed | Approved | Legal requirement |

---

## SAFETY DIRECTIVE (CRITICAL — READ FIRST)

**DO NOT state or claim this app IS the safety navigator.**

**Correct language (USE THESE):**
- Routing data sourced from BBBike
- Safety-focused routing preferences available
- Designed for cyclists who prioritise route safety
- Assists with safer route planning

**Incorrect language (NEVER USE THESE):**
- The safest route guaranteed
- Collision-proof navigation
- Accident prevention tool
- Safe rides guaranteed
- Protects riders from danger

**This distinction is legally and ethically required.** We provide data, not guarantees. Riders remain responsible for their own safety.

---

## AUDIENCE SEGMENTS

| Segment | v1 Support | Notes |
|---------|------------|-------|
| Commuters | Core | Primary use case |
| Tourists | i18n from v1 (9 languages) | Non-English speakers supported |
| Vision-impaired | TalkBack enabled | Screen reader compatibility |
| Tandem riders | Elevation filter adjustable | Hill penalty configurable |
| E-bike trailers with dogs | Weight/cargo mode | Owner's personal use case |
| Cargo bike users | Hill penalty increased | v2 enhancement |

---

## ARCHITECTURE OVERVIEW

+------------------+ +--------------+ +------------+ | Android App |---> | BBBike API |---> | GPX File | | (Kotlin) | | Routing | | Export | +------------------+ +--------------+ +------+-----+ | v +-----------------+ | OsmAnd | | Navigation | +-----------------+


**Key Constraints:**
- No native turn-by-turn engine (use OsmAnd via intent)
- No offline tile cache (reduce scope and failure points)
- GPX format ensures interoperability
- Single-threaded routing requests (avoid race conditions)
- APK size < 15 MB
- Route calc timeout: 10 seconds

---

## LANGUAGE SUPPORT (V1 LAUNCH)

| Locale | Language | Priority |
|--------|----------|----------|
| EN-AU | English (Australian) | Primary (owner's location) |
| EN-GB | English (British) | High |
| EN-US | English (American) | High |
| DE | German | High (BBBike origin language) |
| IT | Italian | Medium |
| ES | Spanish | Medium |
| PT | Portuguese | Medium |
| FR | French | Medium |
| ZH | Mandarin Chinese | Medium |

**Translation Responsibility:** Community contributions welcome. Owner will seed initial translations for EN-AU and DE.

---

## WHAT WE ARE BUILDING (V1 FEATURES)

| ID | Feature | Acceptance Criteria |
|----|---------|---------------------|
| F01 | City selection | List loads from BBBike city list (200+ cities) |
| F02 | Start/End point entry | GPS detect + manual override |
| F03 | Safety preferences | Avoid traffic lights / highways / hills toggle |
| F04 | Route calculation | Returns valid route within 10 seconds |
| F05 | Route visualisation | Draw polyline on map (osmdroid) |
| F06 | GPX export | Valid GPX file, importable into OsmAnd |
| F07 | OsmAnd handoff | Intent opens OsmAnd with route loaded |
| F08 | TalkBack support | All buttons labelled with contentDescription |
| F09 | Multi-language UI | All 9 locales available in settings |
| F10 | Dog trailer weight mode | Cargo profile increases hill penalty |

---

## WHAT WE ARE NOT BUILDING (V1 EXCLUSIONS)

| Feature | Reason | Target Release |
|---------|--------|----------------|
| Turn-by-turn voice | Defer to OsmAnd | Never (in-app) |
| Offline maps | Too complex for v1 | v2+ |
| User accounts | Privacy-first design | Never |
| Social sharing | Post-v1 | v2+ |
| Subscription model | Free only | Never |
| Weather integration | Global API complexity | v2+ |
| Hazard reporting | Moderation burden | v2+ |
| iOS port | Scope creep | v3+ |

---

## PRIVACY, SECURITY & DATA SAFETY

| Commitment | Technical Implementation |
|------------|-------------------------|
| No user accounts | Anonymous usage only |
| No analytics or tracking | Zero telemetry collection |
| No personal data stored | All preferences stored locally on device |
| No third-party data sharing | Your route data never leaves your device |
| Open-source code | Audit trail available at GitHub |
| GDPR compliant | No EU citizen data processed externally |
| End-to-end encryption | Not applicable (no data transmission) |

**Full details:** USER_PRIVACY_POLICY.md (in GitHub repository)

---

## LINKED DOCUMENTS

| Document | Location | Purpose |
|----------|----------|---------|
| PROJECT_DEFINITION.md | /docs/ | BBBike partnership proposal |
| GOVERNANCE_FRAMEWORK.md | /docs/ | Rules of engagement |
| TEST_STRATEGY.md | /docs/ | Testing approach |
| DECISION_LOG.md | /docs/ | What was considered, why deferred |
| RAID_LOG.md | /docs/ | Risks, assumptions, issues, dependencies |
| FEATURE_REQUEST_LOG.md | /docs/ | Out-of-scope requests with ROI analysis |
| USER_PRIVACY_POLICY.md | /docs/ | Legal privacy text |
| ROADMAP_PUBLIC.md | /docs/Public/ | Community-facing timeline |

---

## OPEN QUESTIONS REQUIRING OWNER DECISION

| ID | Question | Options | Pending Since |
|----|----------|---------|---------------|
| OQ001 | GitHub org name | select-distinct vs distinct-routes | 2026-10-11 |
| OQ002 | Weather API for v2 | Open-Meteo vs WeatherAPI | 2026-10-11 |
| OQ003 | Dog trailer mode | Weight preference vs routing profile | 2026-10-11 |
| OQ004 | German translator | Volunteer vs paid | 2026-10-11 |

---

## HOW TO USE THIS BRIEF

1. **Read before engaging on any task** — This is your primary context source
2. **Reference linked documents for details** — Don't assume beyond this text
3. **If uncertain, flag as requires owner decision** — Never guess on safety-critical items
4. **Never assume beyond this text** — If it's not here, ask the Project Owner
5. **Link this brief in every new conversation with AI assistants** — Maintain continuity

**Remember:** This project exists because a cyclist returned to riding after BBBike's algorithm assisted them where their own decades of experience failed. Every decision should respect that motivation.

---

*Document Version: 1.0 — Last Updated: 2026-10-11*
*Next Review: 2026-11-01 (Pre-beta)*
*Review Frequency: Monthly until public release*

---

*END OF CONTEXT BRIEF*