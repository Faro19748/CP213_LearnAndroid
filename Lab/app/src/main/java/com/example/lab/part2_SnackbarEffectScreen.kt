package com.example.lab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SideEffectViewModel : ViewModel() {
    // ใช้ Channel สำหรับส่ง Event (One-time event) ให้มั่นใจว่าฝั่ง UI จะรับค่าเพียงครั้งเดียว
    // จะไม่เหมือน StateFlow ที่ hold ค่าล่าสุดอยู่ ซึ่งอาจทำให้เกิดการเตือนซ้ำๆ เมื่อตะแคงจอ
    private val _errorChannel = Channel<String>()
    val errorFlow = _errorChannel.receiveAsFlow()

    fun triggerError() {
        viewModelScope.launch {
            _errorChannel.send("เกิดข้อผิดพลาดในการเชื่อมต่อเซิร์ฟเวอร์ (Simulated Error)")
        }
    }
}

@Composable
fun SnackbarEffectScreen(viewModel: SideEffectViewModel = viewModel()) {
    // State สำหรับเก็บ SnackbarHost ให้เราสามารถสั่งแสดงผลได้
    val snackbarHostState = remember { SnackbarHostState() }

    // LaunchedEffect ทำงานเมื่อ Component นี้ถูกวาดหรือเรียกใช้
    // การใช้ Unit หมายความว่าบล็อกนี้จะรันแค่ครั้งแรก และจะมีอายุยืนยาวเท่ากับ Composable นี้ (หาก Unit ไม่มีการเปลี่ยนแปลง)
    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { errorMessage ->
            // เมื่อมีการปล่อยข้อมูลจาก Channel ออกมา -> สั่งแสดง Snackbar
            // ฟังก์ชัน showSnackbar() นี้ถูก suspend เพื่อรอให้ขึ้นและหายไป
            snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = "รับทราบ"
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            // ประกาศ Host เพื่อบอกว่าถ้าสั่ง showSnackbar() มันจะโผล่ตรงไหนของ Scaffold
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { viewModel.triggerError() }) {
                Text("Trigger Error (Show Snackbar)")
            }
        }
    }
}
