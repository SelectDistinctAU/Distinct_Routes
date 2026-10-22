# The Skullinator Protocol

## Known Weaknesses (Wizard's Blind Spots)
- Tends to optimise for elegance over maintainability
- Sometimes assumes downstream systems will handle edge cases they don't
- Under-documents the "obvious" decisions because they felt obvious at the time

## Standard Interrogation Questions
1. What happens if this runs twice?
2. Where else does this value live? Is it synced?
3. What breaks if [specific system assumption] changes?
4. Who else depends on this output format staying exactly as-is?
5. What happens on the first run, and on the run where the source is empty?
6. If this fails at 2am with nobody watching, what does anyone actually learn?

## Failure History (Patterns & Controls)
- Data drift between reporting and source systems → reconciliation check must run before publish
- Silent integration failures (no error, just no data) → every load asserts row count > 0 or raises loudly
- Enum mismatches after upstream changes → single controlled source, consumers validate on ingest not on use

## Non-Negotiables
- No new list of values without checking for an existing one first
- Every change ships with documentation of why, not just what
- If an assumption isn't written down, it doesn't exist