//// presentation/.../util/MediaStoreThumbnailFetcher.kt
//package com.moussa79m.presentation.util
//
//import android.content.Context
//import android.graphics.drawable.BitmapDrawable
//import android.net.Uri
//import android.os.Build
//import android.util.Size
//import androidx.annotation.RequiresApi
//import coil.decode.DataSource
//import coil.fetch.DrawableResult
//import coil.fetch.FetchResult
//import coil.fetch.Fetcher
//import coil.request.Options
//
//class MediaStoreThumbnailFetcher(
//    private val context: Context,
//    private val uri: Uri,
//    private val requestedSize: Int
//) : Fetcher {
//    @RequiresApi(Build.VERSION_CODES.Q)
//    override suspend fun fetch(): FetchResult {
//        val bitmap = context.contentResolver.loadThumbnail(
//            uri,
//            Size(requestedSize, requestedSize),
//            null
//        )
//        return DrawableResult(
//            drawable = BitmapDrawable(context.resources, bitmap),
//            isSampled = true,
//            dataSource = DataSource.DISK
//        )
//    }
//
//    class Factory(private val context: Context) : Fetcher.Factory<Uri> {
//        override fun create(data: Uri, options: Options, imageLoader: coil.ImageLoader): Fetcher? {
//            if (!data.toString().contains("media/external")) return null
//            val sizePx = options.size.width.let {
//                (it as? coil.size.Dimension.Pixels)?.px ?: 300
//            }
//            return MediaStoreThumbnailFetcher(context, data, sizePx)
//        }
//    }
//}
package com.moussa79m.core.image

import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import coil.ImageLoader
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.request.Options
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaStoreThumbnailFetcher(
    private val uri: Uri,
    private val options: Options,
    private val contentResolver: ContentResolver
) : Fetcher {

    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun fetch(): FetchResult {
        // الحجم المطلوب من الـ ImageRequest.size() اللي انت مبعته
        val width = options.size.width.toString().filter { it.isDigit() }.toIntOrNull() ?: 512
        val height = options.size.height.toString().filter { it.isDigit() }.toIntOrNull() ?: 512

        val bitmap = withContext(Dispatchers.IO) {
            // ده بيطلب الـ thumbnail الجاهز من نظام أندرويد نفسه بدل فك تشفير الصورة كاملة
            contentResolver.loadThumbnail(uri, Size(width, height), null)
        }

        return DrawableResult(
            drawable = BitmapDrawable(options.context.resources, bitmap),
            isSampled = true,
            dataSource = DataSource.DISK
        )
    }

    class Factory(private val context: Context) : Fetcher.Factory<Uri> {
        override fun create(data: Uri, options: Options, imageLoader: ImageLoader): Fetcher? {
            // بس URIs بتاعة MediaStore، وبس لو minSdk 29+ (Android 10)
            if (data.authority != MediaStore.AUTHORITY) return null
            return MediaStoreThumbnailFetcher(data, options, context.contentResolver)
        }
    }
}