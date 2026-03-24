package com.example.lab

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SensorData(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f
)

class SensorViewModel : ViewModel() {
    private val _sensorData = MutableStateFlow(SensorData())
    val sensorData: StateFlow<SensorData> = _sensorData.asStateFlow()

    fun updateSensorData(x: Float, y: Float, z: Float) {
        _sensorData.value = SensorData(x, y, z)
    }
}
