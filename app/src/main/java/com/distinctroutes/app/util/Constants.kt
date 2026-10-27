package com.distinctroutes.app.util

/**
 * Application-wide constants for Distinct Routes.
 *
 * Centralised configuration hub for API endpoints, versioning, timeouts,
 * and navigation handoff packages. Changes here propagate across the
 * entire codebase via compile-time constants.
 *
 * @author SELECT DISTINCT
 * @since 0.1.0-alpha
 */
object Constants {

    /**
     * BBBike API base URL.
     *
     * All routing requests route through this endpoint. No authentication
     * required per BBBike public API terms.
     *
     * @see <https://wiki.bbbike.org/>
     */
    const val BBBIKE_API_BASE_URL = "https://bbbike.org/"

    /**
     * BBBike route service path (OSRM backend).
     *
     * Appended to [BBBIKE_API_BASE_URL] to form complete route requests.
     * Format: `{base_url}{route_path}/{profile}/{waypoints}`
     */
    const val BBBIKE_ROUTE_PATH = "osrm/route/"

    /**
     * App version name (semantic versioning).
     */
    const val VERSION_NAME = "0.2.0-alpha.4"

    /**
     * App version code (incremental integer for Play Store / F-Droid).
     */
    const val VERSION_CODE = 4

    /**
     * Launch-supported locales.
     *
     * 9 languages on initial release as per [PROJECT_DEFINITION.md].
     * TalkBack accessibility assumed for all.
     */
    val SUPPORTED_LOCALES = listOf(
        "en-AU",  // Australian English
        "en-GB",  // British English
        "en-US",  // American English
        "de",     // German
        "it",     // Italian
        "es",     // Spanish
        "pt",     // Portuguese
        "fr",     // French
        "zh"      // Chinese (Simplified)
    )

    /**
     * OsmAnd navigation handoff package name.
     *
     * Used for Intent-based navigation handoff when user selects
     * turn-by-turn guidance within OsmAnd.
     *
     * @see <https://play.google.com/store/apps/details?id=org.osmand>
     */
    const val OSMAND_NAVIGATOR_PACKAGE = "org.osmand"

    /**
     * Network timeout for BBBike API requests.
     *
     * 30 seconds balances patience with user experience on
     * congested urban networks.
     */
    const val NETWORK_TIMEOUT_MS = 30_000L

    /**
     * Connection pool idle timeout (milliseconds).
     *
     * Recycles HTTP connections to reduce handshake overhead on
     * repeated routing requests.
     */
    const val CONNECTION_POOL_IDLE_MS = 5_000L

    /**
     * GPX export filename prefix.
     *
     * User-exported route files follow format:
     * `distinct-routes-{timestamp}.gpx`
     */
    const val GPX_EXPORT_PREFIX = "distinct-routes-"

    /**
     * Default bike profile for BBBike routing.
     *
     * Overridable via UI settings for cargo/e-bike/tandem variants.
     */
    const val DEFAULT_ROUTING_PROFILE = "bike"

    /**
     * Map view zoom level (initial load).
     */
    const val INITIAL_MAP_ZOOM = 14f

    /**
     * SharedPreferences keys
     */
    const val PREFS_NAME = "DistinctRoutesPrefs"
    const val KEY_SELECTED_CITY = "selected_city"

    /**
     * Privacy directive: No telemetry identifiers.
     *
     * Enforced at application level — no Firebase Analytics,
     * no crash reporting SDK, no user accounts. All data stays local.
     *
     * @see [USER_PRIVACY_POLICY.md](../USER_PRIVACY_POLICY.md)
     */
    const val TELEMETRY_ENABLED = false
}