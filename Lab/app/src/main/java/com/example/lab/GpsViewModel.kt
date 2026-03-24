package com.example.lab

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GpsData(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

class GpsViewModel : ViewModel() {
    private val _gpsData = MutableStateFlow(GpsData())
    val gpsData: StateFlow<GpsData> = _gpsData.asStateFlow()

    fun updateGpsData(lat: Double, lng: Double) {
        _gpsData.value = GpsData(lat, lng)
    }
}
