package com.mirodeon.genericapp.utils.location

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.mirodeon.genericapp.utils.permission.PermissionsCheckerManager

class LocationManager(
    activity: Activity,
    var locationCallback: LocationCallback?,
    private val requestCode: Int,
    instanceKey: String
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    private val permissionsChecker = PermissionsCheckerManager(activity)

    companion object {
        val instance: MutableMap<String, LocationManager> = mutableMapOf()
    }

    init {
        if (instance[instanceKey] == null) {
            instance[instanceKey] = this
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        permissionsChecker.checkPermissionsLocation(requestCode) {
            locationCallback?.let {
                fusedLocationClient.requestLocationUpdates(
                    getRequestForFusedLocation(),
                    it,
                    Looper.getMainLooper()
                )
            }
        }
    }

    fun removeUpdates() {
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
    }

    private fun getRequestForFusedLocation(): LocationRequest {
        return LocationRequest.Builder(1000)
            .setMinUpdateDistanceMeters(1f)
            .setPriority(PRIORITY_HIGH_ACCURACY)
            .build()
    }

}