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
    require(proportions.size <= colors.size) { "Colors list must be at least as large as proportions list" }

    val totalProportion = proportions.sum()
    val sweepAngles = proportions.map { (it / totalProportion) * 360f }

    val sweepAnimation = remember { Animatable(0f) }

    LaunchedEffect(proportions) {
        sweepAnimation.animateTo(
            targetValue = 360f,
            animationSpec = tween(durationMillis = 1500)
        )
    }

    Canvas(modifier = modifier) {
        var startAngle = -90f
        var drawnAngle = 0f
        val currentGlobalSweepAngle = sweepAnimation.value 
        val arcStyle = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)

        for (i in sweepAngles.indices) {
            val arcSweepAngle = sweepAngles[i]
            val color = colors[i]
            
            if (currentGlobalSweepAngle > drawnAngle) {
                val sweepToDraw = minOf(arcSweepAngle, currentGlobalSweepAngle - drawnAngle)
                
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepToDraw,
                    useCenter = false,
                    style = arcStyle
                )
            }
            startAngle += arcSweepAngle
            drawnAngle += arcSweepAngle
        }
    }
}

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
            AnimatedDonutChart(
                proportions = listOf(30f, 40f, 30f),
                colors = listOf(
                    Color(0xFFE91E63),
                    Color(0xFF2196F3),
                    Color(0xFFFFC107)
                ),
                modifier = Modifier.size(240.dp),
                strokeWidth = 40.dp
            )
        }
    }
}
