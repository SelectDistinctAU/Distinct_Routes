# SECURITY POLICY — DISTINCT ROUTES

**Version:** 1.0  
**Last Updated:** 2026-10-11

---

## REPORTING A VULNERABILITY

We take security seriously. If you discover a vulnerability, please report it responsibly.

**Report Via:**
- Email: distinctroutes@pm.me (encrypted preferred)
- GitHub Issue: Mark as [Security] in title

**PGP Key:** Available upon request (email to request)

**Expected Response:**
- Acknowledgement: Within 48 hours
- Status update: Within 7 days
- Patch timeline: Within 72 hours for critical issues

---

## WHAT WE CONSIDER A VULNERABILITY

| Severity | Examples |
|----------|----------|
| **Critical** | Remote code execution, data exfiltration, privilege escalation |
| **High** | Authentication bypass, sensitive data exposure, XSS |
| **Medium** | Information disclosure, CSRF, minor privacy issues |
| **Low** | Misleading error messages, non-critical disclosure |

---

## WHAT IS NOT A VULNERABILITY

- Claims that app "guarantees safety" (intentional disclaimer design)
- GPS accuracy limitations (device-dependent)
- BBBike API response format (third-party service)
- Feature requests disguised as vulnerabilities

---

## VULNERABILITY RESPONSE PROCESS

Report Received ↓ Acknowledge (48 hours) ↓ Triage (Severity Assessment) ↓ Develop Patch (Critical: 72h, High: 7d, Medium: 14d) ↓ Test Patch ↓ Release Hotfix ↓ Notify Reporter ↓ Publish Advisory (if applicable)


---

## SECURITY BEST PRACTICES FOR USERS

1. Keep Android OS updated (minimum Android 7.0+)
2. Download APK only from official sources (GitHub Releases, Google Play)
3. Review permissions before installation
4. Report suspicious behaviour via email
5. Enable automatic updates in Google Play

---

## KNOWN LIMITATIONS

| Limitation | Risk Level | Mitigation |
|------------|------------|------------|
| No encryption (local storage) | Low | No sensitive data stored locally |
| No crash reporting | Low | Manual bug reports required |
| No user accounts | N/A | No credentials to compromise |
| BBBike API trust | Medium | HTTPS-only, data not stored |

---

## CREDIT

We welcome responsible disclosure and will credit security researchers who report valid vulnerabilities, unless they prefer anonymity.

---

*Security Policy — Version 1.0*  
*Last Updated: 2026-10-11*

---

*END OF SECURITY POLICY*