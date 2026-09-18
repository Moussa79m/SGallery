package com.moussa79m.sgallary

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.VideoFrameDecoder
import coil.memory.MemoryCache
import coil.request.CachePolicy


class SGalleryApp: Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(VideoFrameDecoder.Factory())
//                add(MediaStoreThumbnailFetcher.Factory(this@SGalleryApp))
            }.logger(coil.util.DebugLogger())

            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25) // 25% من الرام المسموح بيها للتطبيق
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCache {
                coil.disk.DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02) // نسبة من مساحة التخزين
                    .build()
            }
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .crossfade(false) // شيلها لو عايز crossfade، بس هي بتكلف أداء زيادة وقت الـ scroll السريع
            .build()
    }

}