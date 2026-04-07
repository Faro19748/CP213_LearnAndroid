package com.example.lab

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lab.ui.theme.LabTheme

class Part10Activity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("App Widget Concept") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Concept ของ App Widget",
                            style = MaterialTheme.typography.titleLarge
                        )
                        
                        Text(
                            text = "App Widget คือกล่อง UI ขนาดเล็กที่ไปโผล่อยู่บน Home Screen ของผู้ใช้ ช่วยให้ผู้ใช้สามารถดูข้อมูลหรือโต้ตอบกับแอปของเราได้แบบรวดเร็วโดยไม่ต้องเข้าแอป",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("ส่วนประกอบหลักในการสร้าง Widget:", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("1. AppWidgetProvider:\nคลาสย่อยของ BroadcastReceiver ที่คอยรับ Event ต่างๆ ของ Widget เช่น การขออัปเดต หรือการวาดหน้าจอครั้งแรก\n\n" +
                                     "2. XML Layout:\nปกติ App Widget ยังต้องสร้างด้วย RemoteViews ดังนั้นจึงต้องใช้เลย์เอาต์ดั้งเดิม (เช่น LinearLayout, TextView) และยังไม่สามารถใช้คำสั่ง Jetpack Compose ตรงๆ แทนได้ (เว้นแต่จะใช้ผ่านไลบรารี Jetpack Glance)\n\n" +
                                     "3. AppWidgetProviderInfo:\nไฟล์ XML ในโฟลเดอร์ res/xml ที่เอาไว้กำหนดข้อมูลเบื้องต้น ขนาด Layout เริ่มต้น และความถี่ในการอัปเดตของ Widget\n\n" +
                                     "4. AndroidManifest:\nประกาศให้ระบบเรียกใช้งาน Receiver นี้ และแนบ Meta-data เข้ากับ provider info")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { pinAppWidget() },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("ลองเพิ่ม Widget ลงในหน้า Home")
                        }
                        
                        Text(
                            text = "*หากเครื่องรองรับ ระบบจะแสดง Pop-up ให้กดเพิ่ม Widget แต่หากไม่แสดงสามารถเพิ่มได้เองผ่านเมนู Widgets ของ Launcher",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    private fun pinAppWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val myProvider = ComponentName(this, LabAppWidgetProvider::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                // สร้าง PendingIntent สำหรับรับ Callback หากการติดตั้งสำเร็จ (ใช้เป็น null ก็ได้)
                val successCallback = PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, LabAppWidgetProvider::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                appWidgetManager.requestPinAppWidget(myProvider, null, successCallback)
            } else {
                Toast.makeText(this, "Pinned Widgets ไม่รองรับใน Launcher รุ่นนี้", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "รองรับฟีเจอร์นี้ตั้งแต่ Android 8.0 ขึ้นไปเท่านั้น", Toast.LENGTH_SHORT).show()
        }
    }
}
