package com.example.lab

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class GpsActivity : ComponentActivity() {

    private val viewModel: GpsViewModel by viewModels()
    private lateinit var locationTracker: LocationTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        locationTracker = LocationTracker(this)
        locationTracker.onLocationUpdated = { lat, lng ->
            viewModel.updateGpsData(lat, lng)
        }

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                GpsScreen(
                    viewModel = viewModel,
                    onStartTracking = { locationTracker.startTracking() },
                    onStopTracking = { locationTracker.stopTracking() }
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        locationTracker.stopTracking()
    }
}

@Composable
fun GpsScreen(
    viewModel: GpsViewModel,
    onStartTracking: () -> Unit,
    onStopTracking: () -> Unit
) {
    val gpsData by viewModel.gpsData.collectAsState()
    val context = LocalContext.current
    var isTracking by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            isTracking = true
            onStartTracking()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (isTracking) {
                onStopTracking()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "GPS Location Data", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Latitude: ${gpsData.latitude}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Longitude: ${gpsData.longitude}", style = MaterialTheme.typography.bodyLarge)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = {
                    val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        isTracking = true
                        onStartTracking()
                    } else {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                enabled = !isTracking
            ) {
                Text("Start Tracking")
            }
            
            Button(
                onClick = {
                    isTracking = false
                    onStopTracking()
                },
                enabled = isTracking
            ) {
                Text("Stop")
            }
        }
    }
}
