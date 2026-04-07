package com.example.lab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AdaptiveProfileScreen() {
    // ใช้งาน BoxWithConstraints เพื่อเข้าถึง maxWidth / maxHeight และกำหนดโครงสร้างของ Layout ของเราแบบ Real-time
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // เช็คเงื่อนไขตาม Breakpoint (มักใช้ 600.dp สำหรับจุดที่จอเปลี่ยนไปสู่ระดับ Tablet หรือเริ่มหมุนหน้าจอแนวนอน)
        if (maxWidth < 600.dp) {
            // โหมดมือถือหน้าตั้ง: แสดงผลจากบนลงล่าง
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfilePicture()
                Spacer(modifier = Modifier.height(24.dp))
                ProfileDetails(textAlign = TextAlign.Center)
            }
        } else {
            // โหมดจอกว้าง พอดแคสต์หน้าจอ / Tablet: แสดงผลซ้ายขวาในระดับบรรทัดเดียวกัน
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfilePicture()
                Spacer(modifier = Modifier.width(32.dp))
                // ให้ฝั่งรายละเอียดกินพื้นที่ส่วนต่างที่เหลือทั้งหมด (ใช้ weight)
                Box(modifier = Modifier.weight(1f)) {
                    ProfileDetails(textAlign = TextAlign.Start)
                }
            }
        }
    }
}

@Composable
fun ProfilePicture() {
    // รูปโปรไฟล์สมมติ
    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Text("รูปโปรไฟล์", color = Color.White)
    }
}

@Composable
fun ProfileDetails(textAlign: TextAlign) {
    // ข้อมูลรายละเอียด
    Column(horizontalAlignment = if (textAlign == TextAlign.Center) Alignment.CenterHorizontally else Alignment.Start) {
        Text("ข้อมูลส่วนตัว", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "นี่คือตัวอย่างการแสดงผล Adaptive Layout โดยใช้ BoxWithConstraints\n" +
            "หากคุณหมุนหน้าจอหรือพับ/กางแอปบนจอระดับใหญ่ ตัวจัดการนี้จะคอยคำนวณและปรับผังข้อมูลแบบ Master-Detail ให้อย่างลงตัวเสมอเพื่อให้ประสบการณ์การใช้งานดียิ่งขึ้น",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = textAlign
        )
    }
}
