package com.example.panchang.ui.components

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.Toast
import androidx.core.content.FileProvider
import coil.request.ImageRequest
import com.example.R
import com.example.panchang.model.CardAspectRatio
import com.example.panchang.model.CardThemePalette
import com.example.panchang.model.DeityType
import com.example.panchang.util.CoilImageLoaderProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object GreetingCardExporter {

    suspend fun generateCardBitmap(
        context: Context,
        categoryHindi: String, // e.g. "दैनिक सुविचार" or "शुभ पर्व मंगलकामना"
        titleText: String,     // e.g. "शिव कृपा एवं आत्मिक शांति" or "महाशिवरात्रि की हार्दिक शुभकामनाएं"
        sanskritShloka: String,
        bodyText: String,      // quote or wishes
        sacredMantra: String,
        deityType: DeityType,
        themePalette: CardThemePalette,
        aspectRatio: CardAspectRatio,
        senderSignature: String,
        dateAndTithi: String
    ): Bitmap = withContext(Dispatchers.Default) {
        val width = aspectRatio.pixelWidth
        val height = aspectRatio.pixelHeight

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 1. Theme Gradient Background
        val bgPaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                intArrayOf(
                    themePalette.topGradientArgb,
                    android.graphics.Color.rgb(
                        (android.graphics.Color.red(themePalette.topGradientArgb) * 0.7f).toInt(),
                        (android.graphics.Color.green(themePalette.topGradientArgb) * 0.7f).toInt(),
                        (android.graphics.Color.blue(themePalette.topGradientArgb) * 0.7f).toInt()
                    ),
                    themePalette.bottomGradientArgb
                ),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // 2. Dual Ornate Gold Borders
        val outerBorder = Paint().apply {
            isAntiAlias = true
            color = themePalette.accentArgb
            style = Paint.Style.STROKE
            strokeWidth = 6f
        }
        val innerBorder = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.argb(170, 255, 235, 150)
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
        }
        canvas.drawRect(24f, 24f, width - 24f, height - 24f, outerBorder)
        canvas.drawRect(36f, 36f, width - 36f, height - 36f, innerBorder)

        // Corner Flourishes
        val flourishPaint = Paint().apply {
            isAntiAlias = true
            color = themePalette.accentArgb
            style = Paint.Style.FILL
        }
        drawCorner(canvas, 24f, 24f, flourishPaint, 1f, 1f)
        drawCorner(canvas, width - 24f, 24f, flourishPaint, -1f, 1f)
        drawCorner(canvas, 24f, height - 24f, flourishPaint, 1f, -1f)
        drawCorner(canvas, width - 24f, height - 24f, flourishPaint, -1f, -1f)

        val centerX = width / 2f
        var currentY = 56f

        // 3. Top Category Crest Pill (e.g. "॥ ॐ ॥  दैनिक सुविचार")
        val crestText = "॥ ॐ ॥  $categoryHindi  ॥ ॐ ॥"
        val crestPaint = Paint().apply {
            isAntiAlias = true
            color = themePalette.accentArgb
            textSize = 28f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
            setShadowLayer(4f, 0f, 2f, android.graphics.Color.argb(180, 0, 0, 0))
        }
        canvas.drawText(crestText, centerX, currentY + 30f, crestPaint)
        currentY += 56f

        // 4. Deity Portrait loaded via Coil
        val deityRes = getDeityDrawableResource(deityType)
        val deityBitmap = loadBitmapWithCoil(context, deityRes)
        val diyaBitmap = loadBitmapWithCoil(context, R.drawable.diya_lamp)

        val isStoryFormat = aspectRatio == CardAspectRatio.STORY_9_16
        val isSquareFormat = aspectRatio == CardAspectRatio.SQUARE_1_1

        val deityFrameSize = when {
            isStoryFormat -> 380f
            isSquareFormat -> 260f
            else -> 320f
        }

        val deityTop = currentY + 10f
        val deityRect = RectF(
            centerX - (deityFrameSize / 2f),
            deityTop,
            centerX + (deityFrameSize / 2f),
            deityTop + deityFrameSize
        )

        // Draw Deity Circular/Rounded Glow & Frame
        val glowPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.argb(60, 255, 215, 0)
            style = Paint.Style.FILL
        }
        canvas.drawCircle(centerX, deityTop + (deityFrameSize / 2f), (deityFrameSize / 2f) + 12f, glowPaint)

        if (deityBitmap != null) {
            val saveCount = canvas.save()
            val clipPath = Path().apply {
                addRoundRect(deityRect, 28f, 28f, Path.Direction.CW)
            }
            canvas.clipPath(clipPath)

            // Scale and center crop
            val scale = Math.max(deityRect.width() / deityBitmap.width.toFloat(), deityRect.height() / deityBitmap.height.toFloat())
            val scaledW = deityBitmap.width * scale
            val scaledH = deityBitmap.height * scale
            val dx = deityRect.left + (deityRect.width() - scaledW) / 2f
            val dy = deityRect.top + (deityRect.height() - scaledH) / 2f

            val matrix = Matrix().apply {
                postScale(scale, scale)
                postTranslate(dx, dy)
            }
            canvas.drawBitmap(deityBitmap, matrix, Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG))
            canvas.restoreToCount(saveCount)
        }

        // Gold stroke for Deity frame
        val frameBorderPaint = Paint().apply {
            isAntiAlias = true
            color = themePalette.accentArgb
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }
        canvas.drawRoundRect(deityRect, 28f, 28f, frameBorderPaint)

        // Flanking Diya Lamps
        if (diyaBitmap != null) {
            val diyaSize = if (isSquareFormat) 80f else 105f
            val diyaY = deityTop + (deityFrameSize / 2f) - (diyaSize / 2f)
            val leftDiyaX = deityRect.left - diyaSize - 35f
            val rightDiyaX = deityRect.right + 35f

            if (leftDiyaX > 50f) {
                drawScaledBitmap(canvas, diyaBitmap, leftDiyaX, diyaY, diyaSize, diyaSize)
            }
            if (rightDiyaX + diyaSize < width - 50f) {
                drawScaledBitmap(canvas, diyaBitmap, rightDiyaX, diyaY, diyaSize, diyaSize)
            }
        }

        currentY = deityTop + deityFrameSize + 32f

        // 5. Title Text (Greeting or Quote Title)
        val titlePaint = TextPaint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 248, 225)
            textSize = if (isSquareFormat) 36f else 44f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            setShadowLayer(8f, 0f, 3f, android.graphics.Color.argb(200, 218, 165, 32))
        }
        val titleLayout = createStaticLayout(titleText, titlePaint, width - 120, Layout.Alignment.ALIGN_CENTER)
        canvas.save()
        canvas.translate(centerX - (titleLayout.width / 2f), currentY)
        titleLayout.draw(canvas)
        canvas.restore()
        currentY += titleLayout.height + (if (isStoryFormat) 30f else 20f)

        // Golden Divider line
        val divPaint = Paint().apply {
            isAntiAlias = true
            color = themePalette.accentArgb
            strokeWidth = 2.5f
        }
        canvas.drawLine(centerX - 180f, currentY, centerX + 180f, currentY, divPaint)
        canvas.drawCircle(centerX, currentY, 5f, flourishPaint)
        currentY += (if (isStoryFormat) 30f else 18f)

        // 6. Sanskrit Shloka Box (if present)
        if (sanskritShloka.isNotBlank()) {
            val shlokaPaint = TextPaint().apply {
                isAntiAlias = true
                color = android.graphics.Color.rgb(255, 236, 179)
                textSize = if (isSquareFormat) 26f else 30f
                typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC)
            }
            val shlokaLayout = createStaticLayout(sanskritShloka, shlokaPaint, width - 150, Layout.Alignment.ALIGN_CENTER)

            val boxPadding = 16f
            val shlokaBoxRect = RectF(
                centerX - (shlokaLayout.width / 2f) - boxPadding,
                currentY - 4f,
                centerX + (shlokaLayout.width / 2f) + boxPadding,
                currentY + shlokaLayout.height + boxPadding
            )
            val boxBg = Paint().apply {
                isAntiAlias = true
                color = android.graphics.Color.argb(85, 0, 0, 0)
                style = Paint.Style.FILL
            }
            val boxBorder = Paint().apply {
                isAntiAlias = true
                color = android.graphics.Color.argb(120, 255, 215, 0)
                style = Paint.Style.STROKE
                strokeWidth = 1.5f
            }
            canvas.drawRoundRect(shlokaBoxRect, 14f, 14f, boxBg)
            canvas.drawRoundRect(shlokaBoxRect, 14f, 14f, boxBorder)

            canvas.save()
            canvas.translate(centerX - (shlokaLayout.width / 2f), currentY + 4f)
            shlokaLayout.draw(canvas)
            canvas.restore()
            currentY += shlokaLayout.height + (if (isStoryFormat) 38f else 24f)
        }

        // 7. Body Text (Hindi Quote / Festival Wishes)
        val bodyPaint = TextPaint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(255, 255, 255)
            textSize = if (isSquareFormat) 29f else 34f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        val bodyLayout = createStaticLayout(bodyText, bodyPaint, width - 130, Layout.Alignment.ALIGN_CENTER)
        canvas.save()
        canvas.translate(centerX - (bodyLayout.width / 2f), currentY)
        bodyLayout.draw(canvas)
        canvas.restore()
        currentY += bodyLayout.height + (if (isStoryFormat) 40f else 26f)

        // 8. Sacred Mantra Pill Badge
        if (sacredMantra.isNotBlank()) {
            val mantraText = "॥  $sacredMantra  ॥"
            val mantraPaint = Paint().apply {
                isAntiAlias = true
                color = android.graphics.Color.rgb(255, 249, 210)
                textSize = if (isSquareFormat) 28f else 33f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
                setShadowLayer(4f, 0f, 2f, android.graphics.Color.rgb(212, 175, 55))
            }
            val textWidth = mantraPaint.measureText(mantraText)
            val pillRect = RectF(centerX - (textWidth / 2f) - 32f, currentY, centerX + (textWidth / 2f) + 32f, currentY + 60f)

            val pillBg = Paint().apply {
                isAntiAlias = true
                color = android.graphics.Color.argb(160, 50, 8, 5)
                style = Paint.Style.FILL
            }
            val pillStroke = Paint().apply {
                isAntiAlias = true
                color = themePalette.accentArgb
                style = Paint.Style.STROKE
                strokeWidth = 2.5f
            }
            canvas.drawRoundRect(pillRect, 30f, 30f, pillBg)
            canvas.drawRoundRect(pillRect, 30f, 30f, pillStroke)
            canvas.drawText(mantraText, centerX, currentY + 41f, mantraPaint)
            currentY += 76f
        }

        // 9. Footer: Date & Tithi + Custom Sender Signature
        val footerY = height - 78f

        // Date & Tithi
        val datePaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.rgb(240, 220, 160)
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("शुभ तिथि: $dateAndTithi", centerX, footerY, datePaint)

        // Sender Signature
        val signatureText = if (senderSignature.isNotBlank()) {
            "सप्रेम प्रेषक: $senderSignature  |  हिंदू पंचांग"
        } else {
            "हिंदू पंचांग  •  समस्त परिवार को मंगलकामनाएं"
        }
        val sigPaint = Paint().apply {
            isAntiAlias = true
            color = themePalette.accentArgb
            textSize = 25f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(signatureText, centerX, footerY + 36f, sigPaint)

        bitmap
    }

    private suspend fun loadBitmapWithCoil(context: Context, drawableResId: Int): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val imageLoader = CoilImageLoaderProvider.getImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(drawableResId)
                .allowHardware(false) // Crucial: must be software bitmap to draw on software Canvas
                .build()
            val result = imageLoader.execute(request)
            (result.drawable as? BitmapDrawable)?.bitmap
        } catch (e: Exception) {
            try {
                BitmapFactory.decodeResource(context.resources, drawableResId)
            } catch (ex: Exception) {
                null
            }
        }
    }

    private fun drawScaledBitmap(canvas: Canvas, bmp: Bitmap, x: Float, y: Float, targetW: Float, targetH: Float) {
        val destRect = RectF(x, y, x + targetW, y + targetH)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(bmp, null, destRect, paint)
    }

    private fun createStaticLayout(text: String, paint: TextPaint, width: Int, align: Layout.Alignment): StaticLayout {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
                .setAlignment(align)
                .setLineSpacing(10f, 1.15f)
                .setIncludePad(true)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(text, paint, width, align, 1.15f, 10f, true)
        }
    }

    private fun drawCorner(canvas: Canvas, cx: Float, cy: Float, paint: Paint, dirX: Float, dirY: Float) {
        val path = Path().apply {
            moveTo(cx, cy)
            lineTo(cx + dirX * 36f, cy)
            lineTo(cx, cy + dirY * 36f)
            close()
        }
        canvas.drawPath(path, paint)
    }

    suspend fun saveCardToGallery(context: Context, bitmap: Bitmap, title: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val filename = "greeting_${System.currentTimeMillis()}.jpg"
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
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 96, stream)
                    }
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, contentValues, null, null)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "शुभकामना कार्ड गैलरी में सहेज लिया गया! 🎴", Toast.LENGTH_LONG).show()
                    }
                    true
                } else false
            } else {
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val appDir = File(dir, "HinduPanchang").apply { if (!exists()) mkdirs() }
                val file = File(appDir, filename)
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 96, out)
                }
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(file.absolutePath),
                    arrayOf("image/jpeg"),
                    null
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "शुभकामना कार्ड गैलरी में सहेज लिया गया! 🎴", Toast.LENGTH_LONG).show()
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

    suspend fun shareCard(context: Context, bitmap: Bitmap, caption: String) = withContext(Dispatchers.IO) {
        try {
            val cachePath = File(context.cacheDir, "images").apply { if (!exists()) mkdirs() }
            val file = File(cachePath, "devotional_greeting_card.jpg")
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
                putExtra(Intent.EXTRA_TEXT, caption)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            withContext(Dispatchers.Main) {
                context.startActivity(Intent.createChooser(shareIntent, "कार्ड साझा करें"))
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "शेयर करने में त्रुटि: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
