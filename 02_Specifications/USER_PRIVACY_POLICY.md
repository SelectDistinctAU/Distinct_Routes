# PRIVACY POLICY — DISTINCT ROUTES
**Last Updated:** 2026-10-11  
**Version:** 1.0  
**Company:** SELECT DISTINCT  
**Contact:** distinctroutes@pm.me

---

## INTRODUCTION

Distinct Routes ("the App") is free, open-source software licensed under GPL-3.0. This privacy policy explains how the App handles (and importantly, does NOT handle) user data.

**At a glance:**
- ✅ No user accounts required
- ✅ No personal data collected
- ✅ No analytics or tracking
- ✅ No third-party data sharing
- ✅ All data stored locally on your device
- ✅ Open-source code — fully auditable

---

## DATA COLLECTION POLICY

### Data We DO NOT Collect:

- **Name / Email:** Not collected
- **Phone Number:** Not collected
- **Location History:** Not transmitted (GPS is used strictly for on-device route calculations)
- **Route History:** Stored locally on device only, never transmitted
- **Device Identifiers:** Not accessed
- **Advertising ID:** Not accessed
- **Usage Statistics:** Not tracked
- **Crash Reports:** Not transmitted (optional local logs only)
- **Analytics Cookies:** Not used
- **Telemetry Data:** None

### Data We DO Collect (Stored Locally On-Device Only):

- **Selected City:**
  - Purpose: Route planning
  - Storage Location: On-device private storage
  - Retention: Retained until changed by user
- **Safety Preferences:**
  - Purpose: Route calculation
  - Storage Location: On-device private storage
  - Retention: Retained until changed by user
- **Saved Routes & Ride History:**
  - Purpose: User convenience and ride journaling
  - Storage Location: On-device private storage
  - Retention: Retained until deleted by user
- **Language Preference:**
  - Purpose: UI localization
  - Storage Location: On-device private storage
  - Retention: Retained until changed by user
- **API Response Cache:**
  - Purpose: Performance optimization
  - Storage Location: On-device temporary cache
  - Retention: Automatically deleted after 24 hours

**All data remains strictly on your device. Nothing is transmitted to SELECT DISTINCT or any third party.**

---

## THIRD-PARTY SERVICES

- **BBBike.org:**
  - Purpose: Safety routing data calculation
  - Data Shared: Start, via, and end coordinates (temporary API call only)
  - License: OpenStreetMap ODbL
- **OsmAnd & Organic Maps:**
  - Purpose: Optional turn-by-turn navigation handoff
  - Data Shared: GPX route file (user-initiated export only)
  - License: GPL-3.0 / Open Source
- **Open-Meteo:**
  - Purpose: Weather data integration
  - Data Shared: Location coordinates (temporary API call only)
  - License: CC BY 4.0

**Note:** BBBike processes routing requests. Refer to BBBike's own privacy policy for their data handling practices. SELECT DISTINCT does not store BBBike API request logs.

---

## LOCATION DATA USAGE

The App accesses your device's GPS location **only when you request route calculation**. This data is:
- Sent to BBBike API for routing computation
- Used to display your position on the map (local rendering)
- **NOT stored long-term**
- **NOT transmitted to SELECT DISTINCT servers**

You can deny GPS permissions in Android Settings. The App will function with manual start/end point entry.

---

## OPEN-SOURCE TRANSPARENCY

- **Full Source Code Available:** `github.com/select-distinct/distinct-routes`
- **License:** GNU General Public License v3.0 (GPL-3.0)
- **Audit Rights:** Any user can review, compile, and verify the App's behavior independently. No hidden binaries or obfuscated code.

---

## CHILDREN'S PRIVACY

The App is not directed to children under 13. No age verification is performed. Parents/guardians are responsible for supervising device access.

---

## INTERNATIONAL DATA TRANSFERS

**No personal data leaves your device.** Therefore, no international data transfers occur. BBBike API requests (start/end coordinates) may traverse international networks but do not constitute "personal data" under GDPR as they are temporary and anonymized.

---

## YOUR RIGHTS

As a user, you have:
- ✅ **Right to access:** All your data is already on your device
- ✅ **Right to delete:** Uninstall the App to erase all local data instantly
- ✅ **Right to rectification:** Modify settings within the App
- ✅ **Right to portability:** Export routes as GPX files for import elsewhere
- ✅ **Right to object:** Deny GPS permissions; the App still functions

**No data portability request form required** — you already possess 100% of your data.

---

## SECURITY MEASURES

- **Data Encryption:** Local storage not encrypted (no sensitive personal data stored)
- **Network Security:** HTTPS-only API communication with BBBike.org
- **Code Integrity:** APK/AAB signing required by Google Play
- **Permission Minimization:** Only GPS + Internet permissions requested
- **Vulnerability Disclosure:** Report via GitHub Issues or email

---

## DATA BREACH NOTIFICATION

**Not applicable.** No user data is stored by SELECT DISTINCT on any remote server. There is no central database to breach.

In the unlikely event of a security vulnerability in the code, we will:
1. Publish an advisory via GitHub Releases
2. Issue a patched version within 72 hours
3. Notify the community via GitHub Discussions

---

## CHANGES TO THIS POLICY

This policy may be updated to reflect:
- New features in future releases
- Changes to third-party open-data service providers
- Legal/regulatory requirements

**Notification method:** Updated policy published in the GitHub repository and in-app Settings screen. No email notifications sent (no email addresses collected).

---

## COMPLIANCE FRAMEWORKS

- **GDPR:** Compliant — No EU citizen personal data processed or stored
- **CCPA:** Compliant — No personal data sold, rented, or shared
- **COPPA:** Compliant — Not directed to children under 13
- **ISO 27001:** N/A — No server infrastructure or cloud databases to certify

---

## CONTACT INFORMATION & WEB POLICY STATEMENT

- **Official Privacy Policy URL:** `https://github.com/SelectDistinctAU/Distinct_Routes/blob/main/02_Specifications/USER_PRIVACY_POLICY.md`
- **Privacy Inquiries:** `distinctroutes@pm.me`
- **Bug Reports & Feedback:** GitHub Issues (public)
- **Website:** `https://selectdistinct.au`

**Response time:** Within 14 business days

---

## ACKNOWLEDGEMENT

By using this App, you acknowledge that:
1. You have read this privacy policy.
2. You understand no personal data is collected by SELECT DISTINCT.
3. You accept that BBBike routing data may be accessed during route calculation.
4. You agree to use the App responsibly and safely.

**This App does not guarantee route safety.** Cyclists remain responsible for assessing local conditions before riding.

---

*This privacy policy complies with GDPR Article 13 and CCPA §1798.100 disclosure requirements. Since no personal data is collected, this document serves as a transparency declaration rather than a traditional privacy notice.*

*Policy Version: 1.0 — Last Updated: 2026-10-11*

---

*END OF PRIVACY POLICY*