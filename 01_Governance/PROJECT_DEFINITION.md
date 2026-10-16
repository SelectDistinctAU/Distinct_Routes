# PROJECT DEFINITION — DISTINCT ROUTES
## Partnership Proposal with BBBike

**Last Updated:** 2026-10-11  
**Version:** 1.0  
**Company:** SELECT DISTINCT  
**Contact:** distinctroutes@pm.me

---

## EXECUTIVE SUMMARY

Distinct Routes is a free, open-source Android application that surfaces BBBike's safety-aware routing data to mobile cyclists. We seek BBBike's technical endorsement and API stability assurance before public launch.

**Our Ask:**
- Written confirmation that BBBike API access remains stable for minimum 12 months
- Confirmation of any usage limits (rate limiting, request quotas)
- Permission to reference BBBike in app documentation as data source
- Optional: Formal partnership endorsement for marketing credibility

**What We Offer:**
- Attribution and backlink to bbbike.org in app documentation
- Community feedback on routing quality from global users
- Bug reports if API changes or errors detected
- Open-source transparency — all code auditable

---

## PROJECT BACKGROUND

| Detail | Value |
|--------|-------|
| **Developer** | SELECT DISTINCT (independent developer) |
| **App Name** | Distinct Routes (consumer name) |
| **Platform** | Android 7.0+ (API 24+) |
| **Licence** | GPL-3.0 (open-source) |
| **Target Launch** | December 2026 |
| **Projected Users** | 100+ active monthly (v1 target) |
| **Monetisation** | None — free forever |

---

## TECHNICAL INTEGRATION

### How We Use BBBike Data

+------------------+ +--------------+ +------------+ | Android App |---> | BBBike API |---> | GPX File | | (Kotlin) | | Routing | | Export | +------------------+ +--------------+ +------+-----+ | v +-----------------+ | OsmAnd | | Navigation | +-----------------+


**Integration Details:**
- HTTP GET requests to `https://www.bbbike.org/bbbike/`
- Query parameters: `from`, `to`, `profile`, `language`
- Response format: GPX (parsed locally)
- No caching beyond 24 hours (performance optimisation only)
- No stored route history sent back to BBBike
- No user accounts — anonymous requests only

**Request Volume Estimate:**
- V1 beta: < 1000 requests/month
- Public release (conservative): < 5000 requests/month
- Peak usage window: Morning/evening commute hours (local time)

---

## SAFETY DIRECTIVE (CRITICAL)

**This app does NOT claim to guarantee safety.** We provide routing data sourced from BBBike. Cyclists remain responsible for assessing local conditions before riding.

**Correct language we will use:**
- "Routing data sourced from BBBike"
- "Safety-focused routing preferences available"
- "Assists with safer route planning"

**Language we will NOT use:**
- "The safest route guaranteed"
- "Collision-proof navigation"
- "Accident prevention tool"

**This distinction protects both parties.** We are not making liability claims — we surface routing preferences, not safety certifications.

---

## BENEFITS FOR BBBIKE

| Benefit | Description |
|---------|-------------|
| **Extended reach** | Mobile Android users gain access to BBBike routing without browser dependency |
| **Community feedback** | Global user base identifies edge cases, bugs, routing anomalies |
| **Attribution** | Clear credit in app documentation linking to bbbike.org |
| **Zero cost** | We bear all development, hosting, maintenance expenses |
| **Open-source alignment** | GPL-3.0 licence matches BBBike's open data ethos |
| **Long-term stewardship** | Developer has personal stake (cycling after close-calls) — sustainable commitment |

---

## RISK MITIGATION

| Concern | Our Mitigation |
|---------|----------------|
| API rate limits exceeded | Request queue system (max 5 requests/minute per user) |
| BBBike server overload | Local caching (24-hour expiry) reduces duplicate requests |
| Incorrect routing data flagged | GitHub Issues for bug reports — transparent tracking |
| Brand reputation risk | Clear disclaimer: "Not safety guaranteed — data only" |
| Legal/liability concerns | No safety claims made; GDPR-compliant data handling |
| Abrupt API changes | Wrap in repository interface — easy to swap provider if needed |

---

## PARTNERSHIP ASKS — TIERED APPROACH

### Tier 1: Minimum Required (Non-negotiable)

| Item | Reason |
|------|--------|
| Confirmation: API access stable for 12+ months | V1 launch depends on routing reliability |
| Confirmation: Any rate limit thresholds | Prevent accidental violations during testing |
| Permission: Reference BBBike as data source | Legal attribution requirement |

### Tier 2: Recommended (Enhances Credibility)

| Item | Reason |
|------|--------|
| Written endorsement email (for marketing) | Builds trust with early adopters |
| Link on bbbike.org to our GitHub repo | Reciprocal community support |
| Direct contact channel (email/forum) | Faster resolution if API issues arise |

### Tier 3: Optional (Nice to Have)

| Item | Reason |
|------|--------|
| Inclusion in BBBike third-party integrations list | Long-term discoverability |
| Co-marketing announcement (blog/social media) | Community awareness |
| Access to closure/incident data feed | Future enhancement for Berlin area |

---

## LEGAL & LICENSING ALIGNMENT

| Aspect | BBBike Position | Our Position | Alignment |
|--------|-----------------|--------------|-----------|
| Map data licence | OpenStreetMap ODbL | GPL-3.0 app code | Compatible (both open) |
| Attribution required | Yes (ODbL clause) | Will attribute in app | Compliant |
| Commercial use | Permitted | Non-commercial only | Compliant |
| Derivative works | Share-alike (ODbL) | GPL copyleft | Compatible |
| Data privacy | BBBike processes requests | No data stored by us | Compliant |

**We have reviewed BBBike's terms of service and believe this integration complies with their existing licensing framework.**

---

## TIMELINE EXPECTATIONS

| Milestone | Target Date | Dependency |
|-----------|-------------|------------|
| BBBike response received | 2026-10-25 | Your team availability |
| Gate 1 sign-off | 2026-10-16 | Internal governance (can proceed without BBBike) |
| Code scaffolding | 2026-10-18 | Gate 1 passed |
| Alpha build | 2026-11-20 | API integration complete |
| Beta testing | 2026-11-23 | Alpha stable |
| Public release | 2026-12-07 | Endorsement received |

**Note:** We can proceed with development regardless of BBBike response timing. Endorsement affects release timing, not technical implementation.

---

## CONTACT INFORMATION

| Role | Method |
|------|--------|
| Project Owner | distinctroutes@pm.me (encrypted email) |
| Community Discussion | distinct-routes@googlegroups.com |
| Bug Reports | GitHub Issues (public repository) |
| Formal Enquiries | wolfram@bbbike.org (BBBike lead contact) |

**Response expectation:** Within 14 business days for formal enquiries.

---

## APPENDIX A: APP FEATURES (V1)

| Feature | Description |
|---------|-------------|
| City selection | BBBike city list (200+ cities) loaded dynamically |
| Start/End entry | GPS detect + manual address entry |
| Safety preferences | Avoid traffic lights / hills / highways toggles |
| Route calculation | BBBike API → GPX parsing → polyline display |
| GPX export | Valid GPX file, OsmAnd-compatible |
| Navigation handoff | Intent to open OsmAnd with loaded GPX |
| Multi-language | EN-AU, EN-GB, EN-US, DE, IT, ES, PT, FR, ZH |
| Accessibility | TalkBack screen reader support |

---

## APPENDIX B: PRIVACY COMMITMENTS

| Commitment | Technical Implementation |
|------------|-------------------------|
| No user accounts | Anonymous usage only |
| No analytics or tracking | Zero telemetry collection |
| No personal data stored | All data local on device |
| No third-party data sharing | Route data never leaves device |
| GDPR compliant | No EU citizen data processed externally |

**Full details:** USER_PRIVACY_POLICY.md (GitHub repository)

---

## APPENDIX C: SUCCESS METRICS

| Metric | v1 Target | Measurement |
|--------|-----------|-------------|
| Active monthly users | 100+ | Google Play Console |
| Route calc success rate | > 95% | Internal logging |
| APK size | < 15 MB | Build artifact |
| Crash rate | < 1% | Firebase Crashlytics (opt-in) |
| BBBike API uptime | > 99% | Monitored via requests |

---

## DECLARATION

We affirm that:
1. This project is developed in good faith for community benefit
2. We will attribute BBBike clearly in all documentation
3. We will comply with BBBike's API terms of service
4. We will report bugs transparently via GitHub Issues
5. We will not make safety guarantees in app or marketing materials

---

**Submitted by:** Gerard Bendotti, Project Owner  
**Date:** 2026-10-11  
**Company:** SELECT DISTINCT  
**Contact:** distinctroutes@pm.me

---

*END OF PROJECT DEFINITION*