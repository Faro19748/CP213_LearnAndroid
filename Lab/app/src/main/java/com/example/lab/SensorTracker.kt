package com.example.lab

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorTracker(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    
    // Callback เพื่อส่งค่าพิกัดออกไปให้คนที่ดักฟังอยู่ (ในกรณีนี้คือ Activity ที่จะส่งต่อให้ ViewModel)
    var onSensorUpdated: ((Float, Float, Float) -> Unit)? = null

    fun startListening() {
        accelerometer?.also { accel ->
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val values = event.values
            if (values.size >= 3) {
                onSensorUpdated?.invoke(values[0], values[1], values[2])
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // ไม่ได้ใช้งานใน Lab นี้
    }
}
