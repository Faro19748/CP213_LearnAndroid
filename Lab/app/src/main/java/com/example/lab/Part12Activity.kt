package com.example.lab

import android.os.Bundle
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lab.ui.theme.LabTheme
import kotlinx.coroutines.launch

class Part12Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DialogAndBottomSheetScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAndBottomSheetScreen(modifier: Modifier = Modifier) {
    var showSheet by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Mission 12: Dialog & Bottom Sheet",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Concept:\n" +
                    "1. Middle Dialog (AlertDialog): ใช้สำหรับขัดจังหวะผู้ใช้เพื่อยืนยันการกระทำ หรือแจ้งเตือนข้อมูลสำคัญที่ต้องตัดสินใจทันที\n" +
                    "2. Modal Bottom Sheet: ใช้สำหรับแสดงตัวเลือกเพิ่มเติม หรือรายละเอียดที่ไม่ต้องการเปลี่ยนหน้าจอ โดยจะเลื่อนขึ้นมาจากด้านล่าง และมักจะใช้พื้นที่มากกว่า Dialog",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Show Middle Dialog")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showSheet = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Show Modal Bottom Sheet")
        }

        // --- Middle Dialog (AlertDialog) ---
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Confirm Action") },
                text = { Text("Are you sure you want to proceed with this mission?") },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // --- Modal Bottom Sheet ---
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                // Sheet Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 64.dp, start = 16.dp, end = 16.dp, top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bottom Sheet Content",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("นี่คือพื้นที่สำหรับแสดงตัวเลือกเพิ่มเติม เช่น การแชร์, การแก้ไข หรือเมนูอื่นๆ")
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showSheet = false
                            }
                        }
                    }) {
                        Text("Close Sheet")
                    }
                }
            }
        }
    }
}
