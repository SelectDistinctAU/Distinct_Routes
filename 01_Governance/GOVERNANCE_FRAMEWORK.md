# GOVERNANCE FRAMEWORK — DISTINCT ROUTES
**Version:** 1.0  
**Last Updated:** 2026-10-11  
**Company:** SELECT DISTINCT  
**Authority:** Project Owner (Gerard Bendotti)

---

## PURPOSE

This document establishes decision-making authority, release gates, and accountability for the DISTINCT ROUTES project. No code will be written until all gates pass. Lives are at stake — governance priority over velocity.

---

## ROLES & RESPONSIBILITIES

| Role | Person | Authority | Limitation |
|------|--------|-----------|------------|
| Project Owner | Gerard Bendotti | Final sign-off on scope, safety claims, BBBike partnership | Cannot bypass Gates without written waiver |
| ASAI | Development AI | Technical implementation, test automation, documentation drafts | No independent releases |
| BBBike Liaison | You (external) | API stability confirmation, closure data access | Can veto API usage |
| Beta Testers | Community volunteers | Feedback on usability, routing quality | No release authority |

**Decision Flow:**

Feature Request → Project Owner (Scope Approval) → ASAI (Implementation) → QA Gate → Project Owner (Final Sign-off) → Release

**Every release requires written approval from Project Owner. ASAI cannot self-authorise releases.**

---

## RELEASE GATES (MANDATORY)

Before any release (alpha, beta, or production), these gates must pass. Each requires signed approval from Project Owner.

### Gate 1: Architecture Review

- [ ] Technical architecture doc approved (TECHNICAL_ARCHITECTURE.md)
- [ ] BBBike API confirmed working (API_INTEGRATION_SPEC.md)
- [ ] GPX export validated with 5 sample routes
- [ ] Privacy policy reviewed by owner
- [ ] GPL-3 licence file committed

### Gate 2: Functional Testing Complete

- [ ] All 7 core features tested (F01–F07 per FEATURE_SPECIFICATION.md)
- [ ] 100% critical test cases passing (TEST_CASES.csv)
- [ ] APK size < 15MB verified
- [ ] Crash rate < 1% in alpha test (Firebase Crashlytics)
- [ ] Route calc timeout < 10 seconds

### Gate 3: Safety Validation

- [ ] 3 real-world commutes tested on device (owner or tester)
- [ ] At least 2 routes preferred BBBike over alternatives
- [ ] No false positives (routes through known danger zones)
- [ ] Disclaimer text displayed before first route calculation
- [ ] No safety guarantees claimed in UI or documentation

### Gate 4: Legal/Compliance

- [ ] Disclaimer text in app (Route planning only, not a navigation replacement)
- [ ] GDPR compliance verified (no user tracking, no analytics)
- [ ] GPL-3 licence files included
- [ ] BBBike endorsement received (if requested)
- [ ] Google Play policy review (no safety guarantee claims)

### Gate 5: Release Sign-off

- [ ] All previous gates passed
- [ ] APK signed and stored in release folder
- [ ] Google Play listing drafted
- [ ] Changelog written
- [ ] Google Groups announcement prepared

**Gate Approval Template:**

Gate: [X] Date: [YYYY-MM-DD] Approver: [Name] Signature: _______________________ Comments: [Any conditions or notes]


---

## CHANGE CONTROL PROCESS

Any change to scope, features, timelines, or budgets must be logged. ASAI cannot self-authorise P0 changes.

### Change Control Log

| Change ID | Description | Requestor | Owner Approval | Date | Status |
|-----------|-------------|-----------|----------------|------|--------|
| CC001 | Add elevation profile chart | ASAI | [Pending] | TBD | Pending |

**Approval Rule:** Any change marked P0 (priority zero) requires Project Owner's written sign-off. ASAI cannot self-authorise.

### Change Intake Form

REQUEST: [Describe change] REASON: [Why needed?] IMPACT: [Time, cost, risk] OWNER DECISION: [Approve/Reject/Defer] DATE: [YYYY-MM-DD]

---

## RISK MANAGEMENT

Live document updated weekly. Any risk >= 12 (likelihood x impact) requires mitigation plan before release.

### Risk Register

| Risk ID | Risk Description | Likelihood | Impact | Score | Mitigation | Owner | Status |
|---------|------------------|------------|--------|-------|------------|-------|--------|
| R001 | BBBike API rate limits exceeded | 3 | 5 | 15 | Implement request queuing | ASAI | Open |
| R002 | Closure data Berlin-only | 4 | 3 | 12 | Label feature as Berlin-only | Owner | Mitigated |
| R003 | OsmAnd intent fails on some devices | 2 | 4 | 8 | Fallback to file manager | ASAI | Open |
| R004 | Route misclassified as safe | 1 | 5 | 5 | External safety audit required | Owner | Critical |
| R005 | Google Play rejection | 2 | 5 | 10 | Strict disclaimer compliance | Owner | Mitigated |

**Risk Scoring:** Likelihood x Impact = Priority (>= 12 requires immediate mitigation)

---

## WEEKLY GOVERNANCE REPORT

Every Friday, ASAI generates this report for Project Owner review.

### Weekly Status Report

WEEKLY STATUS REPORT
Week Of: [YYYY-MM-DD] Prepared By: ASAI

PROGRESS
Sprint progress vs. plan: [% complete]
Features completed: [List]
Features in progress: [List]
Blockers: [List, if any]
QUALITY METRICS
Bug count by severity: Critical [X] / High [X] / Medium [X] / Low [X]
Test coverage: [%]
APK size: [MB]
Crash rate: [%]
UPCOMING MILESTONES
Next week's goals: [List]
Gate checkpoints approaching: [Date, Gate Name]
BLOCKING DECISIONS REQUIRED
[Question requiring owner input]
RISK REGISTER UPDATES
[Any changes since last week]
OWNERSHIP ACTIONS REQUESTED
 Sign-off on completed work
 Decision on blocked items
 Approval to proceed to next milestone
Owner Response: [ ] Approved to proceed [ ] Not approved — revisions required [ ] Deferred pending further review


**You respond with:**
- [ ] Sign-off on completed work
- [ ] Decisions on blocked items
- [ ] Approval to proceed to next milestone

---

## ESCALATION PATH

| Issue Type | Who Handles | Timeline |
|------------|-------------|----------|
| Technical blockers | ASAI + Project Owner | Same day |
| BBBike API changes | Project Owner (liaison) | 24 hours |
| Legal/compliance questions | Project Owner (final authority) | 48 hours |
| Safety-critical defects | Project Owner + Immediate halt | Immediate |
| Community disputes | Project Owner + Google Groups mod | 72 hours |

---

## VERSION CONTROL RULES

| Rule | Requirement |
|------|-------------|
| Branch naming | feature/[name], bugfix/[name], release/[version] |
| Commit messages | Conventional commits (feat:, fix:, docs:) |
| Pull requests | Required for all non-trivial changes |
| Code review | Project Owner approves all PRs |
| Release tags | Semantic versioning (v0.1.0, v1.0.0) |
| Changelog | Every release updated CHANGELOG.md |

---

## COMMUNICATION PROTOCOL

| Channel | Purpose | Frequency |
|---------|---------|-----------|
| Google Groups | Community announcements | As needed |
| GitHub Issues | Bug reports, feature requests | Continuous |
| Weekly Report | Progress to Project Owner | Fridays |
| Emergency | Safety-critical defect | Immediate email to distinctroutes@pm.me |

---

## NON-NEGOTIABLES

These principles cannot be overridden without written waiver from Project Owner:

1. No code before Gate 1 — Governance first, execution second
2. No safety guarantee claims — Legal requirement enforced
3. No analytics or tracking — Privacy-first design enforced
4. No closed-source derivatives — GPL-3 licence enforced
5. No releases without owner sign-off — Accountability enforced

---

## SIGN-OFF SECTION

   **Approved By:** GJB (Project Owner)
   
   **Date:** 10-Nov-2026

---

*END OF GOVERNANCE FRAMEWORK*