package com.example.panchang.util

import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.R
import com.example.panchang.model.DeityType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Custom Application class configuring an optimized Coil ImageLoader with high-performance
 * in-memory and on-disk caching, bitmap pooling, and proactive pre-warming.
 */
class PanchangApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        // Proactively pre-cache and warm deity and sacred motif images
        prewarmDeityImageCache()
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    // Reserve up to 25% of available app memory for instant image retrieval
                    .maxSizePercent(0.25)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    // 50 MB dedicated disk cache for deity assets
                    .maxSizeBytes(50L * 1024 * 1024)
                    .build()
            }
            // Memory & Disk cache policies
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .respectCacheHeaders(false) // Cache regardless of server headers to avoid re-fetching
            .crossfade(true)
            .build()
    }

    private fun prewarmDeityImageCache() {
        val imageLoader = CoilImageLoaderProvider.getImageLoader(this)
        val drawablesToPrewarm = listOf(
            R.drawable.shiva_mahadev,
            R.drawable.hanuman_bhagwan,
            R.drawable.ganesha_bhagwan,
            R.drawable.lakshmi_mata,
            R.drawable.diya_lamp
        )

        CoroutineScope(Dispatchers.IO).launch {
            drawablesToPrewarm.forEach { resId ->
                val request = ImageRequest.Builder(this@PanchangApplication)
                    .data(resId)
                    .memoryCacheKey("deity_res_$resId")
                    .diskCacheKey("deity_res_$resId")
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build()
                imageLoader.enqueue(request)
            }
        }
    }
}

/**
 * Singleton provider ensuring a consistent, preconfigured Coil ImageLoader across the app.
 */
object CoilImageLoaderProvider {
    @Volatile
    private var instance: ImageLoader? = null

    fun getImageLoader(context: Context): ImageLoader {
        return instance ?: synchronized(this) {
            instance ?: ImageLoader.Builder(context.applicationContext)
                .memoryCache {
                    MemoryCache.Builder(context.applicationContext)
                        .maxSizePercent(0.25)
                        .strongReferencesEnabled(true)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(context.applicationContext.cacheDir.resolve("image_cache"))
                        .maxSizeBytes(50L * 1024 * 1024)
                        .build()
                }
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
                .respectCacheHeaders(false)
                .crossfade(true)
                .build().also { instance = it }
        }
    }

    /**
     * Builds an optimized ImageRequest configured with specific memory/disk cache keys
     * and aggressive caching policies.
     */
    fun buildDeityImageRequest(context: Context, data: Any): ImageRequest {
        return ImageRequest.Builder(context)
            .data(data)
            .memoryCacheKey("deity_$data")
            .diskCacheKey("deity_$data")
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .crossfade(150)
            .build()
    }
}
