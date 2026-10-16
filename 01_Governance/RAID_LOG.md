# RAID LOG — DISTINCT ROUTES
**Purpose:** Track Risks, Assumptions, Issues, Dependencies
**Version:** 1.0  
**Last Updated:** 2026-10-11  
**Owner:** Gerard Bendotti (Project Owner)

---

## RISKS

| ID | Risk Description | Likelihood (1-5) | Impact (1-5) | Score | Mitigation | Owner | Status |
|----|------------------|------------------|--------------|-------|------------|-------|--------|
| R001 | BBBike API rate limits exceeded | 3 | 5 | 15 | Implement request queue system (max 5 requests/minute per user), exponential backoff on 429 | ASAI | Open |
| R002 | Closure data Berlin-only limits v1 appeal | 4 | 3 | 12 | Label feature clearly as "Berlin-only" in UI, no false expectations | Gerard Bendotti | Mitigated |
| R003 | OsmAnd intent fails on some Android devices | 2 | 4 | 8 | Fallback to GPX file manager dialog, provide download link for OsmAnd | ASAI | Open |
| R004 | Route misclassified as safe (safety-critical) | 1 | 5 | 5 | External safety audit required before public release, clear disclaimers | Gerard Bendotti | Critical |
| R005 | Google Play rejection due to navigation claims | 2 | 5 | 10 | Strict disclaimer compliance, legal review of all UI text | Gerard Bendotti | Mitigated |
| R006 | Translation resources insufficient for v1 launch | 3 | 3 | 9 | Use community volunteers, seed EN-AU and DE initially, accept partial translations | Gerard Bendotti | Open |
| R007 | APK size exceeds 15 MB target | 2 | 4 | 8 | ProGuard minification, no offline maps, monitor dependency sizes | ASAI | Open |
| R008 | BBBike API changes break integration | 2 | 5 | 10 | Wrap API calls in repository interface, easy to swap provider if needed | ASAI | Open |
| R009 | GPS accuracy poor on low-end devices | 3 | 3 | 9 | Warning banner for accuracy >10m, manual coordinate entry fallback | ASAI | Open |
| R010 | No crash reporting hinders bug detection | 2 | 3 | 6 | Manual testing focus v1, opt-in analytics in v2 | Gerard Bendotti | Accepted |
| R011 | Community engagement lower than expected | 3 | 3 | 9 | Active outreach via Google Groups, regular progress updates | Gerard Bendotti | Open |
| R012 | Security vulnerability discovered post-release | 1 | 5 | 5 | Regular dependency updates, responsive patching within 72 hours | ASAI | Open |

**Risk Scoring Formula:** Likelihood × Impact = Priority Score  
**Mitigation Threshold:** Score ≥ 12 requires mitigation plan before Gate 2

---

## ASSUMPTIONS

| ID | Assumption | Validated? | Verification Method | Expiry Date | Owner |
|----|------------|------------|---------------------|-------------|-------|
| A001 | BBBike API will remain stable for minimum 12 months | No | Monthly health check via automated test | 2027-01-01 | Gerard Bendotti |
| A002 | OsmAnd will remain free and open-source | No | Quarterly review of OsmAnd licensing terms | 2027-01-01 | Gerard Bendotti |
| A003 | Free weather API (v2) sufficient for basic forecasts | No | Load testing at 10k monthly users, cost analysis | 2027-06-01 | ASAI |
| A004 | Community volunteers will contribute translations | No | Monitor GitHub PRs, track submission velocity | 2027-03-01 | Gerard Bendotti |
| A005 | Google Play will approve app with navigation disclaimer | Partial | Policy review + submission test with dummy app | 2026-12-05 | Gerard Bendotti |
| A006 | osmdroid library will continue receiving security patches | No | Monitor GitHub releases, fork if abandoned | 2027-01-01 | ASAI |
| A007 | Average route calculation completes within 10 seconds | No | Performance testing on 10+ device types | 2026-11-20 | ASAI |
| A008 | 95%+ of target market uses Android 7.0+ | Yes | StatCounter global Android stats (Oct 2026) | N/A (historical) | Gerard Bendotti |
| A009 | No GDPR compliance burden (no user data stored externally) | Yes | Privacy policy legal review completed | N/A (verified) | Gerard Bendotti |
| A010 | Gate 1 sign-off achievable by 2026-10-16 | No | Weekly governance reports, owner availability | 2026-10-16 | Gerard Bendotti |

**Assumption Review:** All assumptions reviewed monthly; expired assumptions require reassessment or retirement.

---

## ISSUES

| ID | Issue Description | Raised By | Raised Date | Severity | Resolution | Status | Target Resolution Date |
|----|-------------------|-----------|-------------|----------|------------|--------|------------------------|
| I001 | No BBBike endorsement received yet | Gerard Bendotti | 2026-10-11 | High | Submit PROJECT_DEFINITION.md, follow up weekly | Open | 2026-10-25 |
| I002 | GitHub repository not created | Gerard Bendotti | 2026-10-11 | Medium | Project Owner to create before code scaffolding | In Progress | 2026-10-15 |
| I003 | German translator not identified | Gerard Bendotti | 2026-10-11 | Low | Budget TBD, recruit via community | Open | 2026-10-20 |
| I004 | Google Groups membership management unclear | ASAI | 2026-10-11 | Low | Set up moderation rules, approval workflow | Open | 2026-10-18 |
| I005 | CHANGE_CONTROL_LOG.md template incomplete | ASAI | 2026-10-11 | Low | Add change intake form example | In Progress | 2026-10-12 |
| I006 | FlatLaf confusion between Fotologue and Distinct Routes | Gerard Bendotti | 2026-10-11 | Low | Clarified — no overlap, documented in DECISION_LOG | Resolved | 2026-10-11 |
| I007 | Test automation coverage gaps (Owner manual tests) | ASAI | 2026-10-11 | Medium | Schedule weekly owner testing sessions | Open | 2026-11-20 |

---

## DEPENDENCIES

| ID | Dependency | Provider | Required By | Impact If Missing | Status | Owner |
|----|------------|----------|-------------|-------------------|--------|-------|
| D001 | BBBike API access (routing data) | BBBike.org | 2026-10-12 (code scaffolding) | Cannot calculate routes | Pending | Gerard Bendotti |
| D002 | Google Play developer account | Google | 2026-11-01 (beta) | Cannot distribute publicly | Ready | Gerard Bendotti |
| D003 | German translation resources | Volunteer/Professional | 2026-10-20 (i18n complete) | DE locale delayed | Open | Gerard Bendotti |
| D004 | GitHub organisation account | GitHub | 2026-10-11 (repo creation) | Cannot host code | Ready | Gerard Bendotti |
| D005 | OsmAnd app installed on test devices | OsmAnd team | 2026-11-05 (integration test) | Cannot validate handoff | Ready | ASAI |
| D006 | Proton Mail subscription | Proton AG | Ongoing (contact email) | Email delivery reliability | Active | Gerard Bendotti |
| D007 | Android Studio for development | JetBrains/Google | 2026-10-18 (scaffolding) | Cannot write/test code | Ready | ASAI |
| D008 | Test devices (various Android versions) | Owner/Community | 2026-11-20 (beta) | Limited compatibility testing | Partial | Gerard Bendotti |
| D009 | Community beta testers | Volunteers | 2026-11-23 (internal beta) | Delayed user feedback | Open | Gerard Bendotti |
| D010 | F-Droid inclusion | F-Droid maintainers | Post-release (distribution) | Reduced privacy-focused reach | Deferred | Gerard Bendotti |

**Dependency Tracking:** All dependencies monitored weekly; blocked dependencies escalate to Project Owner immediately.

---

## RAID SUMMARY METRICS

| Metric | Value |
|--------|-------|
| Total risks tracked | 12 |
| High-score risks (≥12) | 3 (R001, R002, R005) |
| Risks requiring immediate mitigation | 1 (R004 — safety-critical) |
| Total assumptions tracked | 10 |
| Validated assumptions | 3 |
| Unvalidated assumptions | 7 |
| Total issues tracked | 7 |
| Open issues | 6 |
| Resolved issues | 1 |
| Total dependencies tracked | 10 |
| Ready dependencies | 5 |
| Pending dependencies | 5 |

---

## ESCALATION TRIGGERS

| Trigger | Condition | Escalation Path | Timeline |
|---------|-----------|-----------------|----------|
| Safety-critical defect | Any route misclassification risk | Project Owner + Immediate development halt | Immediate |
| BBBike API outage >24 hours | Routing completely unavailable | Project Owner contacts BBBike directly | 24 hours |
| Gate milestone missed | Any gate deadline slipped >7 days | Project Owner reviews timeline | 7 days |
| Community dispute | Multiple conflicting reports on same issue | Project Owner + Google Groups moderator | 72 hours |
| Security vulnerability | CVE or exploit discovered | Project Owner + emergency patch release | 72 hours |

---

## WEEKLY RAID REVIEW

Every Friday, ASAI updates this log for Project Owner review.

**Next Scheduled Review:** 2026-10-17

**Update Actions Required:**
- [ ] Recalculate risk scores (likelihood/impact may have changed)
- [ ] Mark resolved issues as closed
- [ ] Add new risks/issues from development
- [ ] Update dependency status (Ready/Pending/Blocked)
- [ ] Flag any escalated triggers

---

## APPENDIX: RISK MATRIX VISUALIZATION

Impact (5) │ │ R004 R005 │ ● ● │ R001 ● │ ● │ │ │ R002 R003 │ ● ● │ │ R006 R009 R010 R011 │ ● ● ● ● │──────────────────────────────── │ 1 2 3 4 5 │ Likelihood Impact (1)


**Quadrant Analysis:**
- **Top-right (high likelihood, high impact):** None — well mitigated
- **Top-left (low likelihood, high impact):** R004, R005 — require monitoring
- **Bottom-right (high likelihood, low impact):** R006, R009 — acceptable
- **Bottom-left (low-low):** None — trivial risks removed

---

*RAID Log — Version 1.0*  
*Next Review: 2026-10-17 (Weekly)*

---

*END OF RAID LOG*