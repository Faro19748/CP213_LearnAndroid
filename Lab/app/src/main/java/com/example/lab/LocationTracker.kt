package com.example.lab

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationTracker(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    
    // Callback สำหรับส่งค่าพิกัดไปยัง ViewModel
    var onLocationUpdated: ((Double, Double) -> Unit)? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                onLocationUpdated?.invoke(location.latitude, location.longitude)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startTracking() {
        // อัปเดตพิกัดทุกๆ 1 วินาทีแบบแม่นยำสูง
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
            .setMinUpdateIntervalMillis(1000)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
