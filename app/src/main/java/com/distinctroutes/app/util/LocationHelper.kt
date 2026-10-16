package com.distinctroutes.app.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

data class GpsLocationResult(
    val lat: Double,
    val lon: Double,
    val accuracyMeters: Float
)

class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun hasLocationPermission(): Boolean {
        val finePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return finePermission || coarsePermission
    }

    suspend fun getCurrentLocation(): GpsLocationResult? = suspendCancellableCoroutine { continuation ->
        if (!hasLocationPermission()) {
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        if (locationManager == null) {
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }

        val lastKnown = try {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } catch (e: SecurityException) {
            null
        }

        if (lastKnown != null && (System.currentTimeMillis() - lastKnown.time) < 60_000) {
            continuation.resume(
                GpsLocationResult(
                    lat = lastKnown.latitude,
                    lon = lastKnown.longitude,
                    accuracyMeters = lastKnown.accuracy
                )
            )
            return@suspendCancellableCoroutine
        }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                try {
                    locationManager.removeUpdates(this)
                } catch (e: SecurityException) { }

                if (continuation.isActive) {
                    continuation.resume(
                        GpsLocationResult(
                            lat = location.latitude,
                            lon = location.longitude,
                            accuracyMeters = location.accuracy
                        )
                    )
                }
            }

            override fun onProviderDisabled(provider: String) {
                if (continuation.isActive) continuation.resume(null)
            }
        }

        try {
            val provider = if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                LocationManager.GPS_PROVIDER
            } else {
                LocationManager.NETWORK_PROVIDER
            }
            locationManager.requestSingleUpdate(provider, listener, null)
        } catch (e: SecurityException) {
            if (continuation.isActive) continuation.resume(null)
        }
    }
}