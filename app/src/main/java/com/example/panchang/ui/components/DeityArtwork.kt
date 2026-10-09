package com.example.panchang.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.panchang.model.DeityType
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DeitySacredArtwork(
    deityType: DeityType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2.2f

            // 1. Divine Multi-Layered Glowing Halo / Mandala
            drawGlowingHalo(center, radius, deityType)

            // 2. Deity Emblem & Sacred Insignia
            when (deityType) {
                DeityType.SHIVA -> drawShivaInsignia(center, radius)
                DeityType.HANUMAN -> drawHanumanInsignia(center, radius)
                DeityType.GANESHA -> drawGaneshaInsignia(center, radius)
                DeityType.VISHNU -> drawVishnuInsignia(center, radius)
                DeityType.LAKSHMI -> drawLakshmiInsignia(center, radius)
                DeityType.SHANI -> drawShaniInsignia(center, radius)
                DeityType.SURYA -> drawSuryaInsignia(center, radius)
                DeityType.DURGA -> drawDurgaInsignia(center, radius)
                DeityType.KRISHNA -> drawKrishnaInsignia(center, radius)
                DeityType.RAM -> drawRamInsignia(center, radius)
            }
        }
    }
}

private fun DrawScope.drawGlowingHalo(center: Offset, radius: Float, deityType: DeityType) {
    val haloColor = when (deityType) {
        DeityType.SHIVA -> Color(0xFF64B5F6)
        DeityType.HANUMAN -> Color(0xFFFF7043)
        DeityType.GANESHA -> Color(0xFFFFB300)
        DeityType.VISHNU -> Color(0xFFFFD54F)
        DeityType.LAKSHMI -> Color(0xFFFFD700)
        DeityType.SHANI -> Color(0xFF7E57C2)
        DeityType.SURYA -> Color(0xFFFF9800)
        DeityType.DURGA -> Color(0xFFE53935)
        DeityType.KRISHNA -> Color(0xFF42A5F5)
        DeityType.RAM -> Color(0xFFFFCA28)
    }

    // Outer radial glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(haloColor.copy(alpha = 0.45f), haloColor.copy(alpha = 0.08f), Color.Transparent),
            center = center,
            radius = radius * 1.35f
        ),
        radius = radius * 1.35f,
        center = center
    )

    // Golden radiant aura ring
    drawCircle(
        color = Color(0xFFFFD54F).copy(alpha = 0.7f),
        radius = radius * 0.95f,
        center = center,
        style = Stroke(width = 2.5f)
    )
    drawCircle(
        color = Color(0xFFFFB300).copy(alpha = 0.4f),
        radius = radius * 1.05f,
        center = center,
        style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f)))
    )

    // Ray petals (16 rays)
    for (i in 0 until 16) {
        val angle = Math.toRadians((i * 360.0 / 16.0))
        val startX = center.x + (radius * 0.95f) * cos(angle).toFloat()
        val startY = center.y + (radius * 0.95f) * sin(angle).toFloat()
        val endX = center.x + (radius * 1.18f) * cos(angle).toFloat()
        val endY = center.y + (radius * 1.18f) * sin(angle).toFloat()
        drawLine(
            color = Color(0xFFFFE082).copy(alpha = 0.6f),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 2.5f,
            cap = StrokeCap.Round
        )
    }
}

// Lord Shiva: Sacred Trishul, Damru, Tripundra & Crescent Moon
private fun DrawScope.drawShivaInsignia(center: Offset, radius: Float) {
    val gold = Color(0xFFFFD54F)
    val sacredAsh = Color(0xFFECEFF1)
    val saffron = Color(0xFFFF7043)

    // Crescent Moon
    val moonCenter = Offset(center.x - radius * 0.35f, center.y - radius * 0.45f)
    val moonPath = Path().apply {
        addArc(
            Rect(moonCenter.x - radius * 0.25f, moonCenter.y - radius * 0.25f, moonCenter.x + radius * 0.25f, moonCenter.y + radius * 0.25f),
            120f,
            240f
        )
    }
    drawPath(moonPath, sacredAsh, style = Stroke(width = 4f, cap = StrokeCap.Round))

    // Central Trishul Staff
    drawLine(
        color = gold,
        start = Offset(center.x, center.y + radius * 0.75f),
        end = Offset(center.x, center.y - radius * 0.75f),
        strokeWidth = 5f,
        cap = StrokeCap.Round
    )

    // Trishul central prong
    val prongTipY = center.y - radius * 0.82f
    val prongBaseY = center.y - radius * 0.35f
    drawLine(color = gold, start = Offset(center.x, prongBaseY), end = Offset(center.x, prongTipY), strokeWidth = 6f)

    // Trishul side prongs (curved)
    val leftCurve = Path().apply {
        moveTo(center.x, prongBaseY)
        cubicTo(center.x - radius * 0.4f, prongBaseY, center.x - radius * 0.45f, prongTipY + radius * 0.15f, center.x - radius * 0.32f, prongTipY + radius * 0.05f)
    }
    drawPath(leftCurve, gold, style = Stroke(width = 4.5f, cap = StrokeCap.Round))

    val rightCurve = Path().apply {
        moveTo(center.x, prongBaseY)
        cubicTo(center.x + radius * 0.4f, prongBaseY, center.x + radius * 0.45f, prongTipY + radius * 0.15f, center.x + radius * 0.32f, prongTipY + radius * 0.05f)
    }
    drawPath(rightCurve, gold, style = Stroke(width = 4.5f, cap = StrokeCap.Round))

    // Damru in the middle
    val damruY = center.y
    val damruW = radius * 0.28f
    val damruH = radius * 0.22f
    val damruPath = Path().apply {
        moveTo(center.x - damruW, damruY - damruH)
        lineTo(center.x + damruW, damruY - damruH)
        lineTo(center.x, damruY)
        lineTo(center.x + damruW, damruY + damruH)
        lineTo(center.x - damruW, damruY + damruH)
        lineTo(center.x, damruY)
        close()
    }
    drawPath(damruPath, Brush.linearGradient(listOf(Color(0xFF8D6E63), Color(0xFFD7CCC8))))
    drawPath(damruPath, gold, style = Stroke(width = 2.5f))

    // Tripundra (Three horizontal sacred lines & red bindu)
    val tripundraY = center.y + radius * 0.38f
    for (i in -1..1) {
        drawLine(
            color = sacredAsh,
            start = Offset(center.x - radius * 0.26f, tripundraY + (i * 7f)),
            end = Offset(center.x + radius * 0.26f, tripundraY + (i * 7f)),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )
    }
    drawCircle(color = saffron, radius = 5f, center = Offset(center.x, tripundraY))
}

// Lord Hanuman: Sacred Gada & Divine Ram Tilak
private fun DrawScope.drawHanumanInsignia(center: Offset, radius: Float) {
    val gold = Color(0xFFFFD54F)
    val vermilion = Color(0xFFFF3D00)
    val saffron = Color(0xFFFF9100)

    // Ram Tilak in background
    val tilakY = center.y - radius * 0.35f
    val tilakPath = Path().apply {
        moveTo(center.x - radius * 0.22f, tilakY - radius * 0.3f)
        lineTo(center.x - radius * 0.22f, tilakY)
        cubicTo(center.x - radius * 0.22f, tilakY + radius * 0.25f, center.x, tilakY + radius * 0.35f, center.x, tilakY + radius * 0.45f)
        cubicTo(center.x, tilakY + radius * 0.35f, center.x + radius * 0.22f, tilakY + radius * 0.25f, center.x + radius * 0.22f, tilakY)
        lineTo(center.x + radius * 0.22f, tilakY - radius * 0.3f)
    }
    drawPath(tilakPath, vermilion, style = Stroke(width = 5f, cap = StrokeCap.Round))
    // Central red bindu/line
    drawLine(color = Color(0xFFD50000), start = Offset(center.x, tilakY - radius * 0.25f), end = Offset(center.x, tilakY + radius * 0.35f), strokeWidth = 4f)

    // Golden Sacred Gada (Mace)
    val gadaCenterY = center.y + radius * 0.25f
    // Mace head (fluted sphere)
    drawCircle(
        brush = Brush.radialGradient(listOf(Color(0xFFFFF9C4), gold, Color(0xFFE65100))),
        radius = radius * 0.32f,
        center = Offset(center.x, gadaCenterY)
    )
    drawCircle(color = Color(0xFFB71C1C), radius = radius * 0.32f, center = Offset(center.x, gadaCenterY), style = Stroke(width = 3f))

    // Mace handle
    drawLine(
        color = gold,
        start = Offset(center.x, gadaCenterY - radius * 0.32f),
        end = Offset(center.x, gadaCenterY + radius * 0.65f),
        strokeWidth = 6f,
        cap = StrokeCap.Round
    )
    // Mace base ring
    drawCircle(color = saffron, radius = 9f, center = Offset(center.x, gadaCenterY + radius * 0.65f))
}

// Lord Ganesha: Modak, Swastika & Elephant Trunk Silhouette
private fun DrawScope.drawGaneshaInsignia(center: Offset, radius: Float) {
    val gold = Color(0xFFFFD54F)
    val vermilion = Color(0xFFE53935)
    val saffron = Color(0xFFFF8F00)

    // Sacred Swastika in aura
    val swastikaSize = radius * 0.45f
    val sHalf = swastikaSize / 2f
    val sQuarter = swastikaSize / 4f
    val scX = center.x
    val scY = center.y - radius * 0.25f

    // Vertical line
    drawLine(color = vermilion, start = Offset(scX, scY - sHalf), end = Offset(scX, scY + sHalf), strokeWidth = 5f, cap = StrokeCap.Round)
    // Horizontal line
    drawLine(color = vermilion, start = Offset(scX - sHalf, scY), end = Offset(scX + sHalf, scY), strokeWidth = 5f, cap = StrokeCap.Round)
    // Branches
    drawLine(color = vermilion, start = Offset(scX, scY - sHalf), end = Offset(scX + sQuarter, scY - sHalf), strokeWidth = 5f, cap = StrokeCap.Round)
    drawLine(color = vermilion, start = Offset(scX + sHalf, scY), end = Offset(scX + sHalf, scY + sQuarter), strokeWidth = 5f, cap = StrokeCap.Round)
    drawLine(color = vermilion, start = Offset(scX, scY + sHalf), end = Offset(scX - sQuarter, scY + sHalf), strokeWidth = 5f, cap = StrokeCap.Round)
    drawLine(color = vermilion, start = Offset(scX - sHalf, scY), end = Offset(scX - sHalf, scY - sQuarter), strokeWidth = 5f, cap = StrokeCap.Round)

    // 4 auspicious bindus in 4 quadrants
    drawCircle(color = vermilion, radius = 4f, center = Offset(scX + sQuarter / 1.5f, scY - sQuarter / 1.5f))
    drawCircle(color = vermilion, radius = 4f, center = Offset(scX + sQuarter / 1.5f, scY + sQuarter / 1.5f))
    drawCircle(color = vermilion, radius = 4f, center = Offset(scX - sQuarter / 1.5f, scY + sQuarter / 1.5f))
    drawCircle(color = vermilion, radius = 4f, center = Offset(scX - sQuarter / 1.5f, scY - sQuarter / 1.5f))

    // Golden Modak with plate
    val modakY = center.y + radius * 0.42f
    val modakPath = Path().apply {
        moveTo(center.x - radius * 0.22f, modakY + radius * 0.15f)
        cubicTo(center.x - radius * 0.25f, modakY - radius * 0.05f, center.x, modakY - radius * 0.28f, center.x, modakY - radius * 0.32f)
        cubicTo(center.x, modakY - radius * 0.28f, center.x + radius * 0.25f, modakY - radius * 0.05f, center.x + radius * 0.22f, modakY + radius * 0.15f)
        close()
    }
    drawPath(modakPath, Brush.verticalGradient(listOf(Color(0xFFFFF9C4), saffron, Color(0xFFE65100))))
    drawPath(modakPath, gold, style = Stroke(width = 3f))
}

// Lord Vishnu: Sudarshana Chakra, Shankha & Padma
private fun DrawScope.drawVishnuInsignia(center: Offset, radius: Float) {
    val gold = Color(0xFFFFD54F)
    val cyan = Color(0xFF4FC3F7)

    // Sudarshana Chakra
    val chakraRadius = radius * 0.65f
    drawCircle(color = gold, radius = chakraRadius, center = center, style = Stroke(width = 4f))
    drawCircle(color = cyan, radius = chakraRadius * 0.4f, center = center, style = Stroke(width = 3f))

    // 12 serrated flaming blades around Chakra
    for (i in 0 until 12) {
        val angle = Math.toRadians((i * 360.0 / 12.0))
        val bx = center.x + chakraRadius * cos(angle).toFloat()
        val by = center.y + chakraRadius * sin(angle).toFloat()
        val tipAngle = angle + 0.2
        val tx = center.x + (chakraRadius * 1.25f) * cos(tipAngle).toFloat()
        val ty = center.y + (chakraRadius * 1.25f) * sin(tipAngle).toFloat()

        val bladePath = Path().apply {
            moveTo(bx, by)
            lineTo(tx, ty)
            val nextAngle = Math.toRadians(((i + 0.5) * 360.0 / 12.0))
            lineTo(center.x + chakraRadius * cos(nextAngle).toFloat(), center.y + chakraRadius * sin(nextAngle).toFloat())
            close()
        }
        drawPath(bladePath, Brush.linearGradient(listOf(Color(0xFFFFD54F), Color(0xFFFF6F00))))
    }

    // Sacred Shankha (Conch) silhouette in center
    drawCircle(color = Color.White, radius = radius * 0.18f, center = center)
    drawCircle(color = gold, radius = radius * 0.18f, center = center, style = Stroke(width = 2.5f))
}

// Maa Lakshmi: Sacred Kalash, Lotus & Gold Coins Stream
private fun DrawScope.drawLakshmiInsignia(center: Offset, radius: Float) {
    val gold = Color(0xFFFFD54F)
    val pink = Color(0xFFF06292)

    // Lotus petals at base
    val lotusY = center.y + radius * 0.25f
    val lotusW = radius * 0.6f
    for (i in -2..2) {
        val px = center.x + (i * lotusW * 0.22f)
        val petalPath = Path().apply {
            moveTo(px, lotusY + radius * 0.2f)
            cubicTo(px - radius * 0.15f, lotusY, px, lotusY - radius * 0.25f, px, lotusY - radius * 0.25f)
            cubicTo(px, lotusY - radius * 0.25f, px + radius * 0.15f, lotusY, px, lotusY + radius * 0.2f)
        }
        drawPath(petalPath, Brush.verticalGradient(listOf(Color(0xFFF8BBD0), pink, Color(0xFFC2185B))))
        drawPath(petalPath, gold, style = Stroke(width = 2f))
    }

    // Golden Kalash above lotus
    val kalashY = center.y - radius * 0.12f
    val kalashPath = Path().apply {
        moveTo(center.x - radius * 0.28f, kalashY + radius * 0.25f)
        cubicTo(center.x - radius * 0.35f, kalashY, center.x - radius * 0.15f, kalashY - radius * 0.25f, center.x - radius * 0.18f, kalashY - radius * 0.28f)
        lineTo(center.x + radius * 0.18f, kalashY - radius * 0.28f)
        cubicTo(center.x + radius * 0.15f, kalashY - radius * 0.25f, center.x + radius * 0.35f, kalashY, center.x + radius * 0.28f, kalashY + radius * 0.25f)
        close()
    }
    drawPath(kalashPath, Brush.radialGradient(listOf(Color(0xFFFFF9C4), gold, Color(0xFFFF8F00))))
    drawPath(kalashPath, gold, style = Stroke(width = 3f))

    // Gold coins showering down
    val coinOffsets = listOf(
        Offset(center.x - radius * 0.45f, center.y + radius * 0.1f),
        Offset(center.x - radius * 0.35f, center.y + radius * 0.35f),
        Offset(center.x + radius * 0.45f, center.y + radius * 0.1f),
        Offset(center.x + radius * 0.35f, center.y + radius * 0.35f),
        Offset(center.x, center.y - radius * 0.5f)
    )
    for (co in coinOffsets) {
        drawCircle(color = Color(0xFFFFD700), radius = radius * 0.08f, center = co)
        drawCircle(color = Color(0xFFFFA000), radius = radius * 0.08f, center = co, style = Stroke(width = 2f))
    }
}

// Lord Shani: Celestial Blue Aura, Bow & Star Wheel
private fun DrawScope.drawShaniInsignia(center: Offset, radius: Float) {
    val darkBlue = Color(0xFF1A237E)
    val indigo = Color(0xFF3949AB)
    val gold = Color(0xFFFFD54F)

    // Indigo Planetary Ring
    drawCircle(
        brush = Brush.radialGradient(listOf(indigo, darkBlue)),
        radius = radius * 0.5f,
        center = center
    )
    drawCircle(color = gold, radius = radius * 0.5f, center = center, style = Stroke(width = 3f))

    // Orbit ring (tilted ellipse)
    drawOval(
        color = Color(0xFF8C9EFF),
        topLeft = Offset(center.x - radius * 0.85f, center.y - radius * 0.28f),
        size = Size(radius * 1.7f, radius * 0.56f),
        style = Stroke(width = 3.5f)
    )

    // Sacred Bow & Arrow of Justice
    drawLine(color = gold, start = Offset(center.x - radius * 0.4f, center.y + radius * 0.4f), end = Offset(center.x + radius * 0.4f, center.y - radius * 0.4f), strokeWidth = 3.5f)
}

// Surya Dev: 12-Ray Radiant Sun & Lotus
private fun DrawScope.drawSuryaInsignia(center: Offset, radius: Float) {
    val gold = Color(0xFFFFD54F)
    val saffron = Color(0xFFFF6F00)

    // Inner Glowing Sun Core
    drawCircle(
        brush = Brush.radialGradient(listOf(Color(0xFFFFFDE7), Color(0xFFFFD54F), saffron)),
        radius = radius * 0.48f,
        center = center
    )

    // 12 Radiant Triangular Sun Rays
    for (i in 0 until 12) {
        val angle = Math.toRadians((i * 360.0 / 12.0))
        val angle1 = Math.toRadians((i * 360.0 / 12.0 - 10.0))
        val angle2 = Math.toRadians((i * 360.0 / 12.0 + 10.0))

        val p1x = center.x + radius * 0.48f * cos(angle1).toFloat()
        val p1y = center.y + radius * 0.48f * sin(angle1).toFloat()
        val p2x = center.x + radius * 0.48f * cos(angle2).toFloat()
        val p2y = center.y + radius * 0.48f * sin(angle2).toFloat()
        val tipX = center.x + radius * 0.95f * cos(angle).toFloat()
        val tipY = center.y + radius * 0.95f * sin(angle).toFloat()

        val rayPath = Path().apply {
            moveTo(p1x, p1y)
            lineTo(tipX, tipY)
            lineTo(p2x, p2y)
            close()
        }
        drawPath(rayPath, Brush.linearGradient(listOf(gold, saffron)))
    }
}

// Maa Durga: Sacred Trishul, Lion Crown & Red Lotus
private fun DrawScope.drawDurgaInsignia(center: Offset, radius: Float) {
    val crimson = Color(0xFFD50000)
    val gold = Color(0xFFFFD54F)

    // Red sacred shield aura
    drawCircle(
        brush = Brush.radialGradient(listOf(Color(0xFFFF5252), crimson, Color(0xFF880E4F))),
        radius = radius * 0.55f,
        center = center
    )
    drawCircle(color = gold, radius = radius * 0.55f, center = center, style = Stroke(width = 3.5f))

    // Trishul of Shakti
    drawLine(color = gold, start = Offset(center.x, center.y + radius * 0.5f), end = Offset(center.x, center.y - radius * 0.55f), strokeWidth = 5f, cap = StrokeCap.Round)
    val prongBase = center.y - radius * 0.2f
    val tipY = center.y - radius * 0.55f
    val leftP = Path().apply {
        moveTo(center.x, prongBase)
        cubicTo(center.x - radius * 0.35f, prongBase, center.x - radius * 0.35f, tipY + radius * 0.1f, center.x - radius * 0.25f, tipY)
    }
    drawPath(leftP, gold, style = Stroke(width = 4f, cap = StrokeCap.Round))
    val rightP = Path().apply {
        moveTo(center.x, prongBase)
        cubicTo(center.x + radius * 0.35f, prongBase, center.x + radius * 0.35f, tipY + radius * 0.1f, center.x + radius * 0.25f, tipY)
    }
    drawPath(rightP, gold, style = Stroke(width = 4f, cap = StrokeCap.Round))
}

// Lord Krishna: Bansuri (Flute) & Peacock Feather (Mor Pankh)
private fun DrawScope.drawKrishnaInsignia(center: Offset, radius: Float) {
    val gold = Color(0xFFFFD54F)
    val peacockTeal = Color(0xFF00B4D8)
    val peacockBlue = Color(0xFF0077B6)
    val peacockGreen = Color(0xFF38B000)

    // Peacock Feather (Mor Pankh) Eye
    val eyeCenter = Offset(center.x, center.y - radius * 0.25f)
    drawOval(color = peacockGreen, topLeft = Offset(eyeCenter.x - radius * 0.28f, eyeCenter.y - radius * 0.35f), size = Size(radius * 0.56f, radius * 0.7f))
    drawOval(color = peacockTeal, topLeft = Offset(eyeCenter.x - radius * 0.2f, eyeCenter.y - radius * 0.25f), size = Size(radius * 0.4f, radius * 0.5f))
    drawOval(color = peacockBlue, topLeft = Offset(eyeCenter.x - radius * 0.12f, eyeCenter.y - radius * 0.15f), size = Size(radius * 0.24f, radius * 0.3f))
    drawCircle(color = gold, radius = radius * 0.05f, center = eyeCenter)

    // Golden Bansuri (Flute) placed diagonally
    val fluteStart = Offset(center.x - radius * 0.65f, center.y + radius * 0.4f)
    val fluteEnd = Offset(center.x + radius * 0.65f, center.y - radius * 0.1f)
    drawLine(color = gold, start = fluteStart, end = fluteEnd, strokeWidth = 8f, cap = StrokeCap.Round)

    // Flute finger holes
    for (i in 1..4) {
        val t = 0.35f + (i * 0.12f)
        val hx = fluteStart.x + (fluteEnd.x - fluteStart.x) * t
        val hy = fluteStart.y + (fluteEnd.y - fluteStart.y) * t
        drawCircle(color = Color(0xFF5D4037), radius = 3.5f, center = Offset(hx, hy))
    }
}

// Lord Ram: Kodanda Dhanush (Bow) & Sacred Arrow
private fun DrawScope.drawRamInsignia(center: Offset, radius: Float) {
    val gold = Color(0xFFFFD54F)
    val saffron = Color(0xFFFF9100)

    // Great Kodanda Bow
    val bowPath = Path().apply {
        moveTo(center.x - radius * 0.45f, center.y + radius * 0.6f)
        cubicTo(center.x - radius * 0.85f, center.y + radius * 0.2f, center.x - radius * 0.85f, center.y - radius * 0.2f, center.x - radius * 0.45f, center.y - radius * 0.6f)
    }
    drawPath(bowPath, gold, style = Stroke(width = 6f, cap = StrokeCap.Round))

    // Bow String
    drawLine(color = Color.White.copy(alpha = 0.8f), start = Offset(center.x - radius * 0.45f, center.y + radius * 0.6f), end = Offset(center.x - radius * 0.45f, center.y - radius * 0.6f), strokeWidth = 2.5f)

    // Sacred Arrow pointing upward-right
    val arrowStart = Offset(center.x - radius * 0.45f, center.y)
    val arrowEnd = Offset(center.x + radius * 0.65f, center.y - radius * 0.3f)
    drawLine(color = saffron, start = arrowStart, end = arrowEnd, strokeWidth = 5f, cap = StrokeCap.Round)

    // Arrowhead tip
    val tipPath = Path().apply {
        moveTo(arrowEnd.x, arrowEnd.y)
        lineTo(arrowEnd.x - radius * 0.15f, arrowEnd.y - radius * 0.08f)
        lineTo(arrowEnd.x - radius * 0.08f, arrowEnd.y)
        lineTo(arrowEnd.x - radius * 0.15f, arrowEnd.y + radius * 0.08f)
        close()
    }
    drawPath(tipPath, gold)
}
