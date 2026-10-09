package com.example.panchang.ui.components

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.R
import com.example.panchang.model.DailyPanchang
import com.example.panchang.model.DeityType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun getDeityDrawableResource(deityType: DeityType): Int {
    return when (deityType) {
        DeityType.SHIVA -> R.drawable.shiva_mahadev
        DeityType.HANUMAN -> R.drawable.hanuman_bhagwan
        DeityType.GANESHA -> R.drawable.ganesha_bhagwan
        DeityType.LAKSHMI -> R.drawable.lakshmi_mata
        else -> R.drawable.shiva_mahadev
    }
}

object PosterExporter {

    suspend fun generatePosterBitmap(context: Context, panchang: DailyPanchang): Bitmap = withContext(Dispatchers.Default) {
        val width = 1080
        val height = 1920
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 1. Background Gradient (Deep Royal Maroon to Emerald/Navy Dark)
        val bgPaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                intArrayOf(
                    android.graphics.Color.rgb(56, 5, 2),   // Deep Maroon
                    android.graphics.Color.rgb(82, 7, 4),   // Crimson Maroon
                    android.graphics.Color.rgb(8, 28, 30),  // Deep Teal/Navy
                    android.graphics.Color.rgb(4, 18, 20)   // Dark Emerald
                ),
                floatArrayOf(0f, 0.35f, 0.75f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // 2. Ornate Golden Frame Borders
        val goldBorder = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(226, 196, 117)
            style = Paint.Style.STROKE
            strokeWidth = 6f
        }
        val innerBorder = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.argb(160, 255, 224, 130)
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawRect(24f, 24f, width - 24f, height - 24f, goldBorder)
        canvas.drawRect(36f, 36f, width - 36f, height - 36f, innerBorder)

        // Corner flourishes
        val cornerPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 215, 0)
            style = Paint.Style.FILL
        }
        drawCornerFlourish(canvas, 24f, 24f, cornerPaint, 1f, 1f)
        drawCornerFlourish(canvas, width - 24f, 24f, cornerPaint, -1f, 1f)
        drawCornerFlourish(canvas, 24f, height - 24f, cornerPaint, 1f, -1f)
        drawCornerFlourish(canvas, width - 24f, height - 24f, cornerPaint, -1f, -1f)

        val centerX = width / 2f

        // 3. TOP HERO AREA: Lord Shiva / Deity Real Photo with Divine Frame
        val heroTop = 46f
        val heroBottom = 480f
        drawHeroDeityPhoto(canvas, context, panchang, centerX, heroTop, heroBottom, width)

        // 4. "आज का पंचांग" Crest
        val crestTop = 495f
        val crestBottom = 590f
        val crestRect = RectF(centerX - 350f, crestTop, centerX + 350f, crestBottom)
        val crestPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(94, 11, 7)
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(crestRect, 22f, 22f, crestPaint)
        val crestBorderPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 215, 0)
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        canvas.drawRoundRect(crestRect, 22f, 22f, crestBorderPaint)

        val crestTextPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 249, 230)
            textSize = 54f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            setShadowLayer(6f, 0f, 2f, android.graphics.Color.rgb(212, 175, 55))
        }
        canvas.drawText("🔱  आज का पंचांग  🔱", centerX, crestTop + 66f, crestTextPaint)

        // 5. Date & Samvat Header
        val dateTextPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.WHITE
            textSize = 44f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("${panchang.weekdayHindi}, ${panchang.gregorianFormatted}", centerX, 642f, dateTextPaint)

        val samvatPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 224, 130)
            textSize = 28f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        canvas.drawText("${panchang.hinduMonthHindi}  |  ${panchang.samvatHindi}  |  ${panchang.sakaSamvatHindi}", centerX, 684f, samvatPaint)

        // 6. SUN & MOON INFORMATION: 4 Celestial Cards
        val cardWidth = 222f
        val cardHeight = 145f
        val gap = 16f
        val startX = centerX - (2 * cardWidth + 1.5f * gap)
        val cardY = 712f

        drawCelestialCard(canvas, startX + 0 * (cardWidth + gap), cardY, cardWidth, cardHeight, "सूर्योदय", panchang.sunrise, 0)
        drawCelestialCard(canvas, startX + 1 * (cardWidth + gap), cardY, cardWidth, cardHeight, "सूर्यास्त", panchang.sunset, 1)
        drawCelestialCard(canvas, startX + 2 * (cardWidth + gap), cardY, cardWidth, cardHeight, "चंद्रोदय", panchang.moonrise, 2)
        drawCelestialCard(canvas, startX + 3 * (cardWidth + gap), cardY, cardWidth, cardHeight, "चंद्रास्त", panchang.moonset, 3)

        // 7. PANCHANG DETAILS: Traditional Golden Parchment Manuscript Panel
        val pTop = 880f
        val pBottom = 1140f
        val pRect = RectF(50f, pTop, width - 50f, pBottom)
        val parchmentBg = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(251, 242, 222)
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(pRect, 22f, 22f, parchmentBg)
        val parchmentStroke = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(214, 185, 134)
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        canvas.drawRoundRect(pRect, 22f, 22f, parchmentStroke)

        // Divider
        val pDivPaint = Paint().apply {
            color = android.graphics.Color.rgb(214, 185, 134)
            strokeWidth = 2f
        }
        canvas.drawLine(centerX, pTop + 20f, centerX, pBottom - 20f, pDivPaint)

        val pLabelPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(110, 77, 50)
            textSize = 29f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val pValPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(42, 21, 8)
            textSize = 29f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        var rowY = pTop + 62f
        // Row 1
        canvas.drawText("📅 तिथि :", 75f, rowY, pLabelPaint)
        canvas.drawText("${panchang.tithi.name} (${panchang.tithi.endsAt} तक)", 210f, rowY, pValPaint)
        canvas.drawText("🌸 योग :", centerX + 25f, rowY, pLabelPaint)
        canvas.drawText("${panchang.yoga.name} (${panchang.yoga.endsAt} तक)", centerX + 155f, rowY, pValPaint)

        rowY += 75f
        // Row 2
        canvas.drawText("🌙 पक्ष :", 75f, rowY, pLabelPaint)
        canvas.drawText(panchang.tithi.paksha, 210f, rowY, pValPaint)
        canvas.drawText("⚙️ करण :", centerX + 25f, rowY, pLabelPaint)
        canvas.drawText(panchang.karana.name, centerX + 155f, rowY, pValPaint)

        rowY += 75f
        // Row 3
        canvas.drawText("⭐ नक्षत्र :", 75f, rowY, pLabelPaint)
        canvas.drawText("${panchang.nakshatra.name} (${panchang.nakshatra.endsAt} तक)", 210f, rowY, pValPaint)
        canvas.drawText("🏛️ वार :", centerX + 25f, rowY, pLabelPaint)
        canvas.drawText(panchang.weekdayHindi, centerX + 155f, rowY, pValPaint)

        // 8. SPECIAL OCCASIONS ("आज के विशेष अवसर")
        val occTop = 1165f
        drawRibbonHeader(canvas, centerX, occTop, "🪔 आज के विशेष अवसर")

        val occCardRect = RectF(50f, occTop + 36f, width - 50f, occTop + 230f)
        canvas.drawRoundRect(occCardRect, 20f, 20f, parchmentBg)
        canvas.drawRoundRect(occCardRect, 20f, 20f, parchmentStroke)

        // Miniature Deity Om Box
        val mDeityRect = RectF(75f, occTop + 55f, 200f, occTop + 215f)
        canvas.drawRoundRect(mDeityRect, 14f, 14f, Paint().apply { color = android.graphics.Color.rgb(94, 11, 7) })
        canvas.drawRoundRect(mDeityRect, 14f, 14f, crestBorderPaint)
        canvas.drawText("🕉️", 137f, occTop + 148f, Paint().apply {
            isAntiAlias = true
            textSize = 52f
            textAlign = Paint.Align.CENTER
        })

        // Middle bullet items
        var bY = occTop + 90f
        val bulletPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(122, 12, 8)
            textSize = 27f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val bulletSubPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(42, 21, 8)
            textSize = 27f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val eventTitle = if (panchang.festivals.isNotEmpty()) panchang.festivals.first().nameHindi else if (panchang.vrats.isNotEmpty()) panchang.vrats.first().nameHindi else "आज कोई प्रमुख त्योहार नहीं"
        canvas.drawText("• $eventTitle", 220f, bY, bulletPaint)
        bY += 42f
        canvas.drawText("• ${panchang.weekdayHindi} पावन दिन", 220f, bY, bulletSubPaint)
        bY += 42f
        canvas.drawText("• ${panchang.deity.nameHindi} की विशेष आराधना", 220f, bY, bulletSubPaint)

        // Right "आज का सुझाव" Box
        val sugRect = RectF(width - 325f, occTop + 55f, width - 75f, occTop + 215f)
        canvas.drawRoundRect(sugRect, 14f, 14f, Paint().apply { color = android.graphics.Color.rgb(255, 241, 214) })
        canvas.drawRoundRect(sugRect, 14f, 14f, Paint().apply {
            color = android.graphics.Color.rgb(226, 196, 117)
            style = Paint.Style.STROKE
            strokeWidth = 2f
        })
        canvas.drawText("आज का सुझाव", sugRect.centerX(), occTop + 90f, Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(94, 11, 7)
            textSize = 23f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        })
        canvas.drawText(
            when (panchang.deity.type) {
                DeityType.SHIVA -> "शिवलिंग पर जल व"
                DeityType.HANUMAN -> "हनुमान चालीसा का"
                DeityType.GANESHA -> "भगवान गणेश को दूर्वा"
                else -> "ईश्वर का ध्यान व"
            },
            sugRect.centerX(),
            occTop + 135f,
            Paint().apply {
                isAntiAlias = true
                color = android.graphics.Color.rgb(42, 21, 8)
                textSize = 21f
                textAlign = Paint.Align.CENTER
            }
        )
        canvas.drawText(
            when (panchang.deity.type) {
                DeityType.SHIVA -> "बिल्वपत्र अर्पित करें।"
                DeityType.HANUMAN -> "पाठ करें।"
                DeityType.GANESHA -> "अर्पित करें।"
                else -> "सत्कर्म करें।"
            },
            sugRect.centerX(),
            occTop + 172f,
            Paint().apply {
                isAntiAlias = true
                color = android.graphics.Color.rgb(42, 21, 8)
                textSize = 21f
                textAlign = Paint.Align.CENTER
            }
        )

        // 9. SHUBH MUHURAT ("शुभ एवं अशुभ समय")
        val muhTop = 1420f
        drawRibbonHeader(canvas, centerX, muhTop, "⏰ शुभ एवं अशुभ मुहूर्त")

        val mCardWidth = 186f
        val mCardHeight = 135f
        val mGap = 12f
        val mStartX = centerX - (2.5f * mCardWidth + 2f * mGap)
        val mCardY = muhTop + 38f

        drawMuhuratBox(canvas, mStartX + 0 * (mCardWidth + mGap), mCardY, mCardWidth, mCardHeight, "ब्रह्म मुहूर्त", panchang.brahmaMuhurat.timeRange, android.graphics.Color.rgb(255, 243, 224), android.graphics.Color.rgb(230, 81, 0))
        drawMuhuratBox(canvas, mStartX + 1 * (mCardWidth + mGap), mCardY, mCardWidth, mCardHeight, "अभिजीत", panchang.abhijitMuhurat.timeRange, android.graphics.Color.rgb(232, 245, 233), android.graphics.Color.rgb(46, 125, 50))
        drawMuhuratBox(canvas, mStartX + 2 * (mCardWidth + mGap), mCardY, mCardWidth, mCardHeight, "अमृत काल", panchang.amritKaal.timeRange, android.graphics.Color.rgb(255, 248, 225), android.graphics.Color.rgb(245, 127, 23))
        drawMuhuratBox(canvas, mStartX + 3 * (mCardWidth + mGap), mCardY, mCardWidth, mCardHeight, "राहुकाल (अशुभ)", panchang.rahuKaal.timeRange, android.graphics.Color.rgb(255, 235, 238), android.graphics.Color.rgb(198, 40, 40))
        drawMuhuratBox(canvas, mStartX + 4 * (mCardWidth + mGap), mCardY, mCardWidth, mCardHeight, "यमगण्ड (अशुभ)", panchang.yamaganda.timeRange, android.graphics.Color.rgb(239, 235, 233), android.graphics.Color.rgb(78, 52, 46))

        // 10. DAILY SPIRITUAL MESSAGE / QUOTE SCROLL
        val quoteTop = 1620f
        val quoteRect = RectF(50f, quoteTop, width - 50f, quoteTop + 145f)
        canvas.drawRoundRect(quoteRect, 20f, 20f, parchmentBg)
        canvas.drawRoundRect(quoteRect, 20f, 20f, parchmentStroke)

        canvas.drawText("॥ ${panchang.deity.sacredMantra} ॥", centerX, quoteTop + 45f, Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(198, 124, 0)
            textSize = 34f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        })
        canvas.drawText("\"सकारात्मक विचार, ईश्वर का स्मरण", centerX, quoteTop + 90f, Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(42, 21, 8)
            textSize = 26f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
        })
        canvas.drawText("और सत्कर्म – यही जीवन को सफल बनाते हैं।\"", centerX, quoteTop + 128f, Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(42, 21, 8)
            textSize = 26f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
        })

        // 11. LOCATION FOOTER
        val footerPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 235, 150)
            textSize = 34f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("📍 स्थान: ${panchang.cityName}", centerX, 1825f, footerPaint)

        return@withContext bitmap
    }

    // Overload for backward compatibility
    suspend fun generatePosterBitmap(panchang: DailyPanchang): Bitmap = withContext(Dispatchers.Default) {
        // Fallback without context if ever called
        val width = 1080
        val height = 1920
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    private fun drawHeroDeityPhoto(
        canvas: Canvas,
        context: Context,
        panchang: DailyPanchang,
        cx: Float,
        top: Float,
        bottom: Float,
        width: Int
    ) {
        val heroHeight = bottom - top
        val heroRect = RectF(45f, top, width - 45f, bottom)

        // Try to decode real Lord Shiva / Deity drawable
        val deityRes = getDeityDrawableResource(panchang.deity.type)
        try {
            val options = BitmapFactory.Options().apply { inPreferredConfig = Bitmap.Config.ARGB_8888 }
            val rawBmp = BitmapFactory.decodeResource(context.resources, deityRes, options)
            if (rawBmp != null) {
                // Object-fit: cover into heroRect
                val saveCount = canvas.save()
                val clipPath = Path().apply {
                    addRoundRect(heroRect, 20f, 20f, Path.Direction.CW)
                }
                canvas.clipPath(clipPath)

                // Compute crop matrix
                val scale = Math.max(heroRect.width() / rawBmp.width.toFloat(), heroRect.height() / rawBmp.height.toFloat())
                val scaledWidth = rawBmp.width * scale
                val scaledHeight = rawBmp.height * scale
                val dx = heroRect.left + (heroRect.width() - scaledWidth) / 2f
                val dy = heroRect.top + (heroRect.height() - scaledHeight) / 2f

                val matrix = Matrix().apply {
                    postScale(scale, scale)
                    postTranslate(dx, dy)
                }
                val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
                canvas.drawBitmap(rawBmp, matrix, paint)

                // Overlay gradient vignette (transparent top -> rich deep maroon bottom)
                val vignette = Paint().apply {
                    shader = LinearGradient(
                        0f, top, 0f, bottom,
                        intArrayOf(
                            android.graphics.Color.argb(40, 0, 0, 0),
                            android.graphics.Color.argb(0, 0, 0, 0),
                            android.graphics.Color.argb(120, 56, 5, 2),
                            android.graphics.Color.argb(230, 56, 5, 2)
                        ),
                        floatArrayOf(0f, 0.4f, 0.75f, 1f),
                        Shader.TileMode.CLAMP
                    )
                }
                canvas.drawRect(heroRect, vignette)
                canvas.restoreToCount(saveCount)
            }
        } catch (e: Exception) {
            // Fallback gradient if resource loading failed
            val fallbackPaint = Paint().apply { color = android.graphics.Color.rgb(82, 7, 4) }
            canvas.drawRoundRect(heroRect, 20f, 20f, fallbackPaint)
        }

        // Golden Frame around Hero Photo
        val framePaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 215, 0)
            style = Paint.Style.STROKE
            strokeWidth = 3.5f
        }
        canvas.drawRoundRect(heroRect, 20f, 20f, framePaint)

        // Draw Diya lamps on left and right base of hero photo
        try {
            val diyaBmp = BitmapFactory.decodeResource(context.resources, R.drawable.diya_lamp)
            if (diyaBmp != null) {
                val dSize = 90f
                val dPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
                // Left Diya
                val leftRect = RectF(55f, bottom - dSize - 8f, 55f + dSize, bottom - 8f)
                canvas.drawBitmap(diyaBmp, null, leftRect, dPaint)
                // Right Diya
                val rightRect = RectF(width - 45f - dSize - 10f, bottom - dSize - 8f, width - 45f - 10f, bottom - 8f)
                canvas.drawBitmap(diyaBmp, null, rightRect, dPaint)
            }
        } catch (e: Exception) {
            // Ignore
        }

        // Top Devotional Badge
        val badgeW = 380f
        val badgeH = 54f
        val badgeRect = RectF(cx - badgeW / 2f, top + 14f, cx + badgeW / 2f, top + 14f + badgeH)
        val badgeBg = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.argb(220, 94, 11, 7)
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(badgeRect, 27f, 27f, badgeBg)
        val badgeBorder = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 215, 0)
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawRoundRect(badgeRect, 27f, 27f, badgeBorder)

        val badgeText = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 235, 150)
            textSize = 28f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("॥ ${panchang.deity.sacredMantra} ॥", cx, top + 14f + 37f, badgeText)

        // Sacred couplet near bottom of hero area
        val coupletPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 248, 225)
            textSize = 27f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
            setShadowLayer(4f, 0f, 2f, android.graphics.Color.BLACK)
        }
        val line = when (panchang.deity.type) {
            DeityType.SHIVA -> "हर दिन एक नई शुरुआत है, जब शिव का नाम साथ है।"
            DeityType.HANUMAN -> "संकट कटे मिटे सब पीरा, जो सुमिरै हनुमत बलबीरा।"
            DeityType.GANESHA -> "विघ्न हरण मंगल करन, श्री गणपति महाराज।"
            else -> "सद्कर्म और ईश्वर स्मरण से जीवन मंगलमय होता है।"
        }
        canvas.drawText(line, cx, bottom - 24f, coupletPaint)
    }

    private fun drawCelestialCard(canvas: Canvas, x: Float, y: Float, w: Float, h: Float, title: String, time: String, type: Int) {
        val rect = RectF(x, y, x + w, y + h)
        val bg = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(10, 43, 47)
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(rect, 16f, 16f, bg)
        val border = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.argb(150, 212, 175, 55)
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawRoundRect(rect, 16f, 16f, border)

        // Celestial Icon disk
        val iconCx = rect.centerX()
        val iconCy = y + 38f
        val iconRadius = 22f
        val iconBg = Paint().apply {
            isAntiAlias = true
            color = when (type) {
                0 -> android.graphics.Color.rgb(255, 152, 0) // Sunrise
                1 -> android.graphics.Color.rgb(230, 74, 25) // Sunset
                2 -> android.graphics.Color.rgb(2, 119, 189) // Moonrise
                else -> android.graphics.Color.rgb(26, 35, 126) // Moonset
            }
        }
        canvas.drawCircle(iconCx, iconCy, iconRadius, iconBg)
        canvas.drawCircle(iconCx, iconCy, iconRadius, Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 215, 0)
            style = Paint.Style.STROKE
            strokeWidth = 2f
        })

        // Celestial Symbol inside icon
        val symPaint = Paint().apply {
            isAntiAlias = true
            textSize = 24f
            textAlign = Paint.Align.CENTER
        }
        val sym = when (type) {
            0 -> "☀️"
            1 -> "🌅"
            2 -> "🌙"
            else -> "🌑"
        }
        canvas.drawText(sym, iconCx, iconCy + 9f, symPaint)

        // Title
        val titlePaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 213, 79)
            textSize = 24f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(title, rect.centerX(), y + 90f, titlePaint)

        // Time
        val timePaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.WHITE
            textSize = 26f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(time, rect.centerX(), y + 124f, timePaint)
    }

    private fun drawRibbonHeader(canvas: Canvas, cx: Float, y: Float, title: String) {
        val rect = RectF(cx - 230f, y, cx + 230f, y + 48f)
        canvas.drawRoundRect(rect, 10f, 10f, Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(94, 11, 7)
        })
        canvas.drawRoundRect(rect, 10f, 10f, Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 215, 0)
            style = Paint.Style.STROKE
            strokeWidth = 2f
        })
        canvas.drawText(title, cx, y + 33f, Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 249, 230)
            textSize = 25f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        })
    }

    private fun drawMuhuratBox(canvas: Canvas, x: Float, y: Float, w: Float, h: Float, title: String, timeRange: String, bgColor: Int, textColor: Int) {
        val rect = RectF(x, y, x + w, y + h)
        canvas.drawRoundRect(rect, 14f, 14f, Paint().apply {
            isAntiAlias = true
            color = bgColor
        })
        canvas.drawRoundRect(rect, 14f, 14f, Paint().apply {
            isAntiAlias = true
            color = textColor
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
        })

        val times = timeRange.split(" - ")
        val t1 = times.getOrNull(0) ?: timeRange
        val t2 = times.getOrNull(1) ?: ""

        canvas.drawText(title, rect.centerX(), y + 36f, Paint().apply {
            isAntiAlias = true
            color = textColor
            textSize = 21f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        })

        canvas.drawText(t1, rect.centerX(), y + 72f, Paint().apply {
            isAntiAlias = true
            color = textColor
            textSize = 21f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        })

        if (t2.isNotEmpty()) {
            canvas.drawText("से $t2", rect.centerX(), y + 104f, Paint().apply {
                isAntiAlias = true
                color = textColor
                textSize = 19f
                textAlign = Paint.Align.CENTER
            })
        }
    }

    private fun drawCornerFlourish(canvas: Canvas, x: Float, y: Float, paint: Paint, dirX: Float, dirY: Float) {
        val path = Path().apply {
            moveTo(x, y)
            lineTo(x + 40f * dirX, y)
            quadTo(x + 10f * dirX, y + 10f * dirY, x, y + 40f * dirY)
            close()
        }
        canvas.drawPath(path, paint)
    }

    suspend fun savePosterToGallery(context: Context, bitmap: Bitmap): Boolean = withContext(Dispatchers.IO) {
        val filename = "Panchang_${System.currentTimeMillis()}.jpg"
        return@withContext try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/HinduPanchang")
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { stream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 98, stream)
                    }
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, contentValues, null, null)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "पंचांग कार्ड गैलरी में सुरक्षित हो गया! 🪔", Toast.LENGTH_LONG).show()
                    }
                    true
                } else false
            } else {
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val appDir = File(dir, "HinduPanchang").apply { if (!exists()) mkdirs() }
                val file = File(appDir, filename)
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 98, out)
                }
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(file.absolutePath),
                    arrayOf("image/jpeg"),
                    null
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "पंचांग कार्ड गैलरी में सुरक्षित हो गया! 🪔", Toast.LENGTH_LONG).show()
                }
                true
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "कार्ड सहेजने में त्रुटि: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }

    suspend fun sharePoster(context: Context, bitmap: Bitmap) = withContext(Dispatchers.IO) {
        try {
            val cachePath = File(context.cacheDir, "images").apply { if (!exists()) mkdirs() }
            val file = File(cachePath, "panchang_daily_card.jpg")
            FileOutputStream(file).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)
            }
            val contentUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_TEXT, "🚩 आज का शुभ पंचांग 🚩\nहर दिन एक नई शुरुआत है, जब प्रभु का नाम साथ है।")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            withContext(Dispatchers.Main) {
                context.startActivity(Intent.createChooser(shareIntent, "दैनिक पंचांग शेयर करें"))
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "शेयर करने में त्रुटि: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
