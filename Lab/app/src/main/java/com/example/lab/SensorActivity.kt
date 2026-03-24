package com.example.lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class SensorActivity : ComponentActivity() {

    private val viewModel: SensorViewModel by viewModels()
    private lateinit var sensorTracker: SensorTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ขั้นตอนที่ 1 & 2: สร้างคลาสแยกแล้วรับค่าจาก callback ส่งเข้า ViewModel
        sensorTracker = SensorTracker(this)
        sensorTracker.onSensorUpdated = { x, y, z ->
            viewModel.updateSensorData(x, y, z)
        }

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                SensorScreen(viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorTracker.startListening()
    }

    override fun onPause() {
        super.onPause()
        sensorTracker.stopListening()
    }
}

@Composable
fun SensorScreen(viewModel: SensorViewModel) {
    // ใช้ collectAsState เพื่อ observe StateFlow จาก ViewModel
    val sensorData by viewModel.sensorData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Accelerometer Data", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "X: ${"%.2f".format(sensorData.x)}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Y: ${"%.2f".format(sensorData.y)}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Z: ${"%.2f".format(sensorData.z)}", style = MaterialTheme.typography.bodyLarge)
    }
}
