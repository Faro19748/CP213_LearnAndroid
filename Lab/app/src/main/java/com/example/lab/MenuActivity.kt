package com.example.lab

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.lab.ui.theme.LabTheme

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            val intent = Intent(this@MenuActivity, Part1Activity::class.java)
                            startActivity(intent)
                        }) {
                            Text("Go to Part 1 (UI & Animation)")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            val intent = Intent(this@MenuActivity, Part2Activity::class.java)
                            startActivity(intent)
                        }) {
                            Text("Go to Part 2 (Advanced UI)")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            val intent = Intent(this@MenuActivity, TransitionMainActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text("Go to Activity Transitions")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            val intent = Intent(this@MenuActivity, RPGcardActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text("Go to RPG Card")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            val intent = Intent(this@MenuActivity, Part9Activity::class.java)
                            startActivity(intent)
                        }) {
                            Text("Go to Part 9 (Collapsing Toolbar)")
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(onClick = {
                            val intent = Intent(this@MenuActivity, Part10Activity::class.java)
                            startActivity(intent)
                        }) {
                            Text("Go to Part 10 (App Widget)")
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 32.dp))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = " Sensor & GPS (MVVM)",
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Button(onClick = {
                                val intent = Intent(this@MenuActivity, SensorActivity::class.java)
                                startActivity(intent)
                            }) {
                                Text("Sensor")
                            }
                            Button(onClick = {
                                val intent = Intent(this@MenuActivity, GpsActivity::class.java)
                                startActivity(intent)
                            }) {
                                Text("GPS")
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 32.dp))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = " Camera & Permission",
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        CameraScreenLogic()

                    }
                }
            }
        }
    }
}

@Composable
fun CameraScreenLogic() {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { result: Bitmap? ->
        bitmap = result
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val contentResolver = context.contentResolver
            val b = try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(contentResolver, it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            if (b != null) {
                bitmap = b
            }
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            takePictureLauncher.launch()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Captured/Selected Image",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)
            )
        } ?: Text(text = "No image available", modifier = Modifier.padding(bottom = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    takePictureLauncher.launch()
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text(text = "Take Photo")
            }

            Button(onClick = {
                galleryLauncher.launch("image/*")
            }) {
                Text(text = "Open Gallery")
            }
        }
    }
}
