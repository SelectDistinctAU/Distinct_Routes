# CONTRIBUTING GUIDE — DISTINCT ROUTES

**Thank you for considering contributing to Distinct Routes!**

This document outlines how you can help make safety-aware cycling routing accessible to everyone. All contributions are welcomed and acknowledged in release notes.

---

## QUICK START

### Before You Start

1. **Read the documentation first:**
   - [README.md](README.md) — Project overview
   - [USER_PRIVACY_POLICY.md](02_Specifications/USER_PRIVACY_POLICY.md) — Privacy commitments
   - [FEATURE_REQUEST_LOG.md](06_Feature_Log/FEATURE_REQUEST_LOG.md) — What's in/out of scope

2. **Join the community:**
   - Google Groups: [distinct-routes@googlegroups.com](https://groups.google.com/g/distinct-routes)
   - Email: distinctroutes@pm.me

3. **Understand our ethos:**
   - Free forever (no subscriptions, no ads)
   - Open-source (GPL-3.0)
   - Privacy-first (zero tracking, zero telemetry)
   - Safety-focused but **no safety guarantees claimed**

---

## WAYS TO CONTRIBUTE

### 1. Bug Reports (Low Effort)

**Best for:** Non-technical users who find issues

**Steps:**
1. Check existing issues on GitHub (avoid duplicates)
2. Create new issue with template:

Title: [Bug] Brief description Body:

Device: [Android version, model]
App version: [e.g., 0.1.0-alpha]
Steps to reproduce: [Numbered list]
Expected behavior: [What should happen]
Actual behavior: [What actually happens]
Screenshots: [If applicable]
3. Label: `bug`

**Response time:** We aim to acknowledge within 7 days.

---

### 2. Feature Requests (Low Effort)

**Best for:** Suggesting new functionality

**Requirements:**
- Complete [FEATURE_REQUEST_LOG.md](06_Feature_Log/FEATURE_REQUEST_LOG.md) intake form
- Include ROI justification (Benefit × Impact × Reach) / (Time × Complexity)
- Score ≥ 1.0 required for consideration

**Steps:**
1. Review existing feature requests (avoid duplicates)
2. Comment on GitHub Issue or create new one
3. Use label: `enhancement`
4. Project Owner reviews weekly

**Important:** Most new features are deferred to v2+. v1 scope is locked at Gate 1.

---

### 3. Translation Contributions (Medium Effort)

**Best for:** Bilingual speakers who want to expand accessibility

**Current status:**
| Locale | Status | Needed |
|--------|--------|--------|
| EN-AU | Seeded | Complete |
| EN-GB | Seeded | Complete |
| EN-US | Seeded | Complete |
| DE | Seeded | Complete |
| IT | Incomplete | Volunteers needed |
| ES | Incomplete | Volunteers needed |
| PT | Incomplete | Volunteers needed |
| FR | Incomplete | Volunteers needed |
| ZH | Incomplete | Volunteers needed |

**Steps:**
1. Clone the repository
2. Navigate to `res/values-[locale]/strings.xml`
3. Translate all `<string>` entries
4. Test with Android Studio emulator
5. Submit pull request

**Template:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Copy from values/strings.xml, translate values only -->
    <string name="app_name">Distinct Routes</string>
    <string name="route_calculate">Calculate Route</string>
    <!-- ... continue for all strings ... -->
</resources>

Quality check:

All umlauts/accented characters display correctly (ä, é, ñ, etc.)
No placeholder text remains
String length doesn't break UI layout (test in emulator)
Deadline: Beta release (November 2026)

4. Testing (Medium Effort)
Best for: Careful users who want to help validate quality

Beta Programme Requirements:

Android 7.0+ device
willingness to report bugs
~1 hour per week for 2 weeks
How to join:

Subscribe to Google Groups (distinct-routes@googlegroups.com)
Comment: "Interested in beta testing" on beta announcement post
Receive APK download link privately
Test assigned scenarios (checklist provided)
Submit bug reports via GitHub Issues
Beta Test Checklist:

 Install app successfully
 Select city from dropdown
 Enter start/end points (GPS + manual)
 Toggle safety preferences
 Calculate route under 10 seconds
 Export GPX file
 Import GPX into OsmAnd
 Navigate with OsmAnd turn-by-turn
 Change language settings
 Test TalkBack accessibility (if vision-impaired)
 Report all bugs encountered
Note: Beta builds may be unstable. Use only for testing, not daily commuting.

5. Code Contributions (High Effort)
Best for: Android developers who want to write code

Before starting:

Read technical docs first:

TECHNICAL_ARCHITECTURE.md
API_INTEGRATION_SPEC.md (when available)
Check existing issues:

Look for good first issue labels
Avoid working on features already assigned
Understand governance:

All code requires Project Owner approval (Gate system)
No P0 changes without written sign-off
Security-critical changes may require external review
Development Setup:

# Clone repository
git clone https://github.com/select-distinct/distinct-routes.git
cd distinct-routes

# Open in Android Studio
# File → Open → Select distinct-routes folder

# Build debug APK
./gradlew assembleDebug

Branching Strategy:

main (protected)
├── feature/[feature-name]  → New feature development
├── bugfix/[issue-number]   → Bug fixes
└── hotfix/[critical-fix]   → Emergency patches

Pull Request Requirements:

 Code formatted with ktlint
 No detekt warnings/errors
 Unit tests passing (./gradlew test)
 PR description includes:
What was changed
Why the change was made
Testing performed
Related issue number (if any)
Code Review Process:

ASAI performs initial review (style, lint)
Project Owner reviews (functionality, scope alignment)
Merge only after both approvals
PR closed if out of scope (refer to FEATURE_REQUEST_LOG.md)
LICENCE COMPLIANCE
All contributions must comply with GPL-3.0:

You retain copyright to your changes
You grant permission to distribute under GPL-3.0
Your contributions become part of the larger GPL-3.0 project
Derivative works must also be open-source (copyleft)
By submitting a pull request, you agree to:

I certify that my contribution is licensed under GPL-3.0.
I understand this code will be distributed freely as open-source.
I waive moral rights to the extent permitted by law.

Add this statement to your PR description.

CODE OF CONDUCT
Our Standards
Be respectful: Different opinions are welcome; personal attacks are not
Be constructive: Critique ideas, not people
Be inclusive: Welcome newcomers; mentor when possible
Be honest: Admit mistakes; give credit where due
Be safety-aware: Never claim the app "guarantees safety"
Unacceptable Behaviour
Harassment or discrimination (any form)
Aggressive trolling or insults
Spam or self-promotion
Posting unsafe routing advice as fact
Claiming the app is safer than it is
Enforcement
First offence: Private reminder of standards
Second offence: Temporary ban from discussions
Repeated offences: Permanent exclusion from project
Report violations to: distinctroutes@pm.me

DESIGN GUIDELINES
UI/UX Principles
Minimalism: Fewer clicks = better
Accessibility: All buttons must work with TalkBack
Contrast: Minimum 4.5:1 text/background ratio (WCAG AA)
Touch targets: Minimum 48dp x 48dp
Language: Clear, concise, non-technical where possible
Dark Mode
Follow Material Components DayNight theme
Respect system preference by default
Test with OLED displays for battery impact
Language Support
Never hardcode English text in Kotlin code
Always reference string resources: getString(R.string.route_calculate)
Ensure RTL compatibility (future Arabic/hebrew locales)
DOCUMENTATION CONTRIBUTIONS
Best for: Writers who want to improve guides

Areas needing attention:

User Guide (USER_GUIDE.md — currently TBD)
API documentation (when API_INTEGRATION_SPEC.md published)
README updates (release announcements)
Changelog maintenance (CHANGELOG.md per release)
Style guide:

Use British English spelling (UK-based project)
Active voice preferred ("Click the button" not "The button should be clicked")
Numbered steps for sequential tasks
Bullet points for parallel items
FREQUENTLY ASKED QUESTIONS
Q: Can I use this code in my commercial app?
A: Yes, but you must also open-source your derivative work under GPL-3.0 (copyleft). See LICENSE.txt for details.

Q: Can I charge money for distribution?
A: Yes. GPL-3.0 allows charging for copies. You cannot restrict buyers from further redistribution.

Q: How often are releases?
A: Currently planning v1 release (December 2026). Post-release, aiming for quarterly minor releases.

Q: Can I request specific features be added?
A: Yes, but most new features are deferred to v2+. v1 scope is locked until Gate 1 passes. Review FEATURE_REQUEST_LOG.md for current priorities.

Q: Will you hire beta testers?
A: No paid positions currently. Beta testing is volunteer-based. Future roles possible post-launch depending on funding.

Q: How do I report security vulnerabilities?
A: Email distinctroutes@pm.me with encrypted message (PGP key available on request). Public disclosure discouraged until patch available.

RECOGNITION
All contributors are acknowledged in:

GitHub Contributors list
Release notes (CHANGELOG.md)
App "About" screen (major contributors)
Levels of recognition:

Minor contributor: 1+ bug reports or translations
Contributor: 1+ code contributions merged
Core contributor: 5+ significant contributions
Maintainer: Project Owner invites (by invitation only)
GETTING HELP
Stuck on something? Ask here:

Question Type	Where to Ask
Bug reports	GitHub Issues (public)
Feature requests	GitHub Issues (public)
Technical setup	Google Groups (public)
Translation help	Google Groups (public)
Private concerns	Email (distinctroutes@pm.me)
Urgent safety issues	Email (immediate response)
Average response time: 2-7 days (volunteer-run project)

THANK YOU
Building open-source software is a labour of love. Thank you for investing your time, skills, and energy into making safer cycling routing accessible to everyone.

Together, we can help cyclists find routes they trust.

Contributing Guide — Version 1.0
Last Updated: 2026-10-11
Review Frequency: Quarterly

END OF CONTRIBUTING GUIDE