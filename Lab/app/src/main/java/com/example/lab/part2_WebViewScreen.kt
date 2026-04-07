package com.example.lab

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class WebViewModel : ViewModel() {
    var url by mutableStateOf("https://www.google.com")
        private set

    fun updateUrl(newUrl: String) {
        var validUrl = newUrl.trim()
        if (!validUrl.startsWith("http://") && !validUrl.startsWith("https://")) {
            validUrl = "https://$validUrl"
        }
        url = validUrl
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(viewModel: WebViewModel = viewModel()) {
    // ใช้ Local State เพื่อแยกค่าที่ผู้ใช้กำลังพิมพ์ ไม่ให้ส่งผลกระทบไปหา WebView แบบทันที (ป้องกันโหลดเว็บทุกครั้งที่พิมพ์ตัวอักษร)
    var inputText by remember { mutableStateOf(viewModel.url) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                label = { Text("Enter URL") },
                singleLine = true
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(onClick = { viewModel.updateUrl(inputText) }) {
                Text("Go")
            }
        }

        // AndroidView ใช้เพื่อทำ Interoperability เอาระบบ View แบบเก่ามาฝังบน Compose
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                // factory จะถูกเรียกทำงาน "ครั้งเดียว" ตอนที่ถูกวาดขึ้นมาครั้งแรก (เหมาะสำหรับการ Initialize)
                WebView(context).apply {
                    // กำหนดให้เมื่อเปิดลิงก์ใดๆ ก็ตามให้ยังคงอยู่ในแอปของเรา ไม่เด้งไปแอพ Browser ของเครื่อง
                    webViewClient = WebViewClient()
                    
                    // ปรับแต่งตั้งค่าของ WebView ให้รัน JS ได้ เพื่อการแสดงหน้าเว็บที่สมบูรณ์
                    settings.javaScriptEnabled = true
                }
            },
            update = { webView ->
                // update จะถูกเรียกทำงานครั้งแรก และจะทำงานซ้ำ "ทุกครั้งที่เกิด Recomposition" 
                // หรือก็คือเมื่อ viewModel.url มีค่าเปลี่ยนไป มันจะมาสั่งงานบล็อกนี้ใหม่เรื่อยๆ
                webView.loadUrl(viewModel.url)
            }
        )
    }
}
