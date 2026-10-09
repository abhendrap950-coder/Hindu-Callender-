package com.example.panchang.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SacredDiyaMotif(modifier: Modifier = Modifier.size(36.dp)) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f

        // 1. Glowing Flame Halo
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFFEA00), Color(0xFFFF6D00).copy(alpha = 0.6f), Color.Transparent),
                center = Offset(cx, cy - 6f),
                radius = 16f
            ),
            radius = 16f,
            center = Offset(cx, cy - 6f)
        )

        // 2. Flame (Teardrop shape)
        val flamePath = Path().apply {
            moveTo(cx, cy - 18f)
            cubicTo(cx + 6f, cy - 10f, cx + 5f, cy - 2f, cx, cy - 1f)
            cubicTo(cx - 5f, cy - 2f, cx - 6f, cy - 10f, cx, cy - 18f)
            close()
        }
        drawPath(flamePath, Brush.verticalGradient(listOf(Color(0xFFFFF9C4), Color(0xFFFFB300), Color(0xFFFF3D00))))

        // Inner blue-white core of flame
        val corePath = Path().apply {
            moveTo(cx, cy - 10f)
            cubicTo(cx + 2.5f, cy - 5f, cx + 2f, cy - 1f, cx, cy - 1f)
            cubicTo(cx - 2f, cy - 1f, cx - 2.5f, cy - 5f, cx, cy - 10f)
            close()
        }
        drawPath(corePath, Brush.verticalGradient(listOf(Color.White, Color(0xFF81D4FA))))

        // 3. Brass Diya Base Bowl
        val diyaBowl = Path().apply {
            moveTo(cx - 14f, cy)
            lineTo(cx + 14f, cy)
            cubicTo(cx + 12f, cy + 10f, cx - 12f, cy + 10f, cx - 14f, cy)
            close()
        }
        drawPath(diyaBowl, Brush.linearGradient(listOf(Color(0xFFFFD54F), Color(0xFFC67C00), Color(0xFF8D5300))))
        drawPath(diyaBowl, Color(0xFFFFE082), style = Stroke(width = 1f))

        // Stand base
        val stand = Path().apply {
            moveTo(cx - 6f, cy + 10f)
            lineTo(cx + 6f, cy + 10f)
            lineTo(cx + 9f, cy + 13f)
            lineTo(cx - 9f, cy + 13f)
            close()
        }
        drawPath(stand, Color(0xFFC67C00))
    }
}

@Composable
fun SacredShivaLingamMotif(modifier: Modifier = Modifier.size(36.dp)) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f

        // Lingam black stone cylinder with rounded top
        val lingamPath = Path().apply {
            moveTo(cx - 7f, cy + 5f)
            lineTo(cx - 7f, cy - 5f)
            cubicTo(cx - 7f, cy - 13f, cx + 7f, cy - 13f, cx + 7f, cy - 5f)
            lineTo(cx + 7f, cy + 5f)
            close()
        }
        drawPath(lingamPath, Brush.linearGradient(listOf(Color(0xFF263238), Color(0xFF000000))))

        // White Tripundra on Lingam
        for (i in -1..1) {
            drawLine(
                color = Color.White,
                start = Offset(cx - 4.5f, cy - 4f + (i * 2f)),
                end = Offset(cx + 4.5f, cy - 4f + (i * 2f)),
                strokeWidth = 1f
            )
        }
        // Red bindu
        drawCircle(color = Color(0xFFD50000), radius = 1f, center = Offset(cx, cy - 4f))

        // Yoni base (Pitham)
        val yoniPath = Path().apply {
            moveTo(cx - 15f, cy + 5f)
            lineTo(cx + 15f, cy + 5f)
            cubicTo(cx + 12f, cy + 11f, cx - 12f, cy + 11f, cx - 15f, cy + 5f)
            close()
        }
        drawPath(yoniPath, Color(0xFF212121))
        drawPath(yoniPath, Color(0xFFFFD54F), style = Stroke(width = 1f))
    }
}
