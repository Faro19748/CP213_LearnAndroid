package com.example.lab

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedDonutChart(
    proportions: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 40.dp
) {
    // ตรวจสอบข้อมูลเบื้องต้น กำหนดให้ List สีมีจำนวนพอดีหรือมากกว่า ค่าเปอร์เซ็นต์
    require(proportions.size <= colors.size) { "Colors list must be at least as large as proportions list" }

    // คำนวณผลรวมทั้งหมด (เผื่อว่าพารามิเตอร์ส่งมาไม่ได้รวมกันเป็น 100 พอดี จะได้แปรผันถูกหลักส่วน)
    val totalProportion = proportions.sum()
    
    // แปลงสัดส่วนให้เป็นองศาตามโครงสร้างวงกลม (360 องศา)
    val sweepAngles = proportions.map { (it / totalProportion) * 360f }

    // 3. สร้าง State สำหรับจำค่า Animation ของการกวาดองศาตั้งแต่ 0 ถึง 360
    val sweepAnimation = remember { Animatable(0f) }

    // เริ่มทำงาน Animation เมื่อเปิดหน้าจอนี้ขึ้นมา
    LaunchedEffect(proportions) {
        sweepAnimation.animateTo(
            targetValue = 360f,
            animationSpec = tween(durationMillis = 1500) // ระยะเวลาวาด 1.5 วินาที
        )
    }

    Canvas(modifier = modifier) {
        var startAngle = -90f // เริ่มวาดจากด้านบนสุดตามเข็มนาฬิกา (ตําแหน่ง 12 นาฬิกา)
        var drawnAngle = 0f // ค่าสะสมขององศาที่ได้พิจารณาวาดไปแล้วในลูป

        // ค่าองศารวมที่มีสิทธิ์วาดในขณะนั้น จากการกระทำของ Animation 
        val currentGlobalSweepAngle = sweepAnimation.value 
        
        // 2. สไตล์วงกลมแบบมีรูตรงกลาง (โดนัท) โดยกำหนดความกว้างเส้น (Stroke) 
        val arcStyle = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)

        for (i in sweepAngles.indices) {
            val arcSweepAngle = sweepAngles[i]
            val color = colors[i]
            
            // เช็คว่า Animation วาดมาถึงส่วนนี้หรือยัง
            if (currentGlobalSweepAngle > drawnAngle) {
                // คำนวณองศาที่จะวาดจริงๆ (ถ้า Animation ยังวิ่งไม่สุดส่วนนี้ ก็วาดแค่เท่าที่วิ่งถึง)
                val sweepToDraw = minOf(arcSweepAngle, currentGlobalSweepAngle - drawnAngle)
                
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepToDraw,
                    useCenter = false, // false = ไม่วาดเส้นไปที่จุดศูนย์กลาง (เพื่อให้มีรูตรงกลางแบบโดนัท)
                    style = arcStyle
                )
            }
            startAngle += arcSweepAngle // ขยับจุด Start Angle สำหรับชิ้นต่อไป
            drawnAngle += arcSweepAngle // เก็บสะสมค่าที่ถูกวาดไปแล้ว
        }
    }
}

// ----------------------------------------------------
// Preview 
// ----------------------------------------------------
@Preview(showBackground = true)
@Composable
fun AnimatedDonutChartPreview() {
    Surface(color = Color.White) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            // 1. รับค่าเป็น List เปอร์เซ็นต์ กับ List ของสี
            AnimatedDonutChart(
                proportions = listOf(30f, 40f, 30f),
                colors = listOf(
                    Color(0xFFE91E63), // Pink
                    Color(0xFF2196F3), // Blue
                    Color(0xFFFFC107)  // Amber
                ),
                modifier = Modifier.size(240.dp), // กำหนดขนาดของโดนัท
                strokeWidth = 40.dp // ความหนาของขอบโดนัท
            )
        }
    }
}
