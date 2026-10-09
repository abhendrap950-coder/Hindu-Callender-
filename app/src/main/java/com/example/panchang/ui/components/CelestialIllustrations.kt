package com.example.panchang.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

enum class CelestialType {
    SUNRISE,
    SUNSET,
    MOONRISE,
    MOONSET
}

@Composable
fun CelestialBadge(
    type: CelestialType,
    modifier: Modifier = Modifier.size(38.dp)
) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension / 2.1f

        when (type) {
            CelestialType.SUNRISE -> {
                // Golden Morning Sun rising over water/horizon
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFFF3A1), Color(0xFFFF9800), Color(0xFFD84315)),
                        center = center,
                        radius = radius
                    ),
                    radius = radius,
                    center = center
                )
                // Horizon line and water reflections
                val horizonY = center.y + radius * 0.25f
                drawLine(
                    color = Color(0xFFFFD54F),
                    start = Offset(center.x - radius * 0.8f, horizonY),
                    end = Offset(center.x + radius * 0.8f, horizonY),
                    strokeWidth = 1.5f
                )
                for (i in 1..3) {
                    val y = horizonY + (i * 3.5f)
                    val w = radius * (0.6f - i * 0.12f)
                    drawLine(
                        color = Color(0xFFFFE082).copy(alpha = 0.8f),
                        start = Offset(center.x - w, y),
                        end = Offset(center.x + w, y),
                        strokeWidth = 1.5f
                    )
                }
                // Radiant sun rays
                for (i in 0 until 8) {
                    val angle = Math.toRadians((i * 45.0 - 90.0))
                    val sx = center.x + radius * 0.55f * cos(angle).toFloat()
                    val sy = center.y + radius * 0.55f * sin(angle).toFloat()
                    val ex = center.x + radius * 0.9f * cos(angle).toFloat()
                    val ey = center.y + radius * 0.9f * sin(angle).toFloat()
                    drawLine(
                        color = Color(0xFFFFF9C4),
                        start = Offset(sx, sy),
                        end = Offset(ex, ey),
                        strokeWidth = 1.8f,
                        cap = StrokeCap.Round
                    )
                }
            }
            CelestialType.SUNSET -> {
                // Twilight Evening Sunset over crimson sea
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFFCC80), Color(0xFFE64A19), Color(0xFF4A148C)),
                        center = center,
                        radius = radius
                    ),
                    radius = radius,
                    center = center
                )
                // Half submerged sun disk
                val horizonY = center.y + radius * 0.2f
                drawCircle(
                    color = Color(0xFFFFE082),
                    radius = radius * 0.42f,
                    center = Offset(center.x, horizonY)
                )
                // Silhouette ripples
                for (i in 1..4) {
                    val y = horizonY + (i * 3.2f)
                    val w = radius * (0.7f - i * 0.1f)
                    drawLine(
                        color = Color(0xFFFFAB91).copy(alpha = 0.7f),
                        start = Offset(center.x - w, y),
                        end = Offset(center.x + w, y),
                        strokeWidth = 1.5f
                    )
                }
            }
            CelestialType.MOONRISE -> {
                // Luminous Full Moon over deep blue ocean
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFE0F7FA), Color(0xFF0277BD), Color(0xFF012443)),
                        center = center,
                        radius = radius
                    ),
                    radius = radius,
                    center = center
                )
                // Moon sphere with gentle crater texture
                val moonCenter = Offset(center.x, center.y - radius * 0.15f)
                val moonRadius = radius * 0.46f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFFFFFF), Color(0xFFECEFF1), Color(0xFFB0BEC5)),
                        center = moonCenter,
                        radius = moonRadius
                    ),
                    radius = moonRadius,
                    center = moonCenter
                )
                // Water moonlight path
                val waterY = center.y + radius * 0.35f
                for (i in 1..4) {
                    val y = waterY + (i * 2.8f)
                    val w = radius * (0.35f + i * 0.08f)
                    drawLine(
                        color = Color(0xFFB2EBF2).copy(alpha = 0.65f),
                        start = Offset(center.x - w, y),
                        end = Offset(center.x + w, y),
                        strokeWidth = 1.2f
                    )
                }
            }
            CelestialType.MOONSET -> {
                // Starry Night Moon setting behind mist
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF5C6BC0), Color(0xFF1A237E), Color(0xFF0A0E27)),
                        center = center,
                        radius = radius
                    ),
                    radius = radius,
                    center = center
                )
                // Moon crescent or globe
                val moonCenter = Offset(center.x, center.y - radius * 0.1f)
                drawCircle(
                    color = Color(0xFFE8EAF6),
                    radius = radius * 0.42f,
                    center = moonCenter
                )
                // Stars in dark sky
                drawCircle(color = Color.White, radius = 1.2f, center = Offset(center.x - radius * 0.55f, center.y - radius * 0.45f))
                drawCircle(color = Color.White, radius = 1f, center = Offset(center.x + radius * 0.6f, center.y - radius * 0.35f))
                drawCircle(color = Color.White, radius = 1.2f, center = Offset(center.x + radius * 0.35f, center.y + radius * 0.5f))
            }
        }

        // Elegant Gold Rim Border
        drawCircle(
            color = Color(0xFFFFD54F),
            radius = radius,
            center = center,
            style = Stroke(width = 1.5f)
        )
    }
}
