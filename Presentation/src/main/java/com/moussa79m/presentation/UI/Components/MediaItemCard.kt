//package com.moussa79m.presentation.UI.Screens
//
//import androidx.compose.foundation.gestures.awaitFirstDown
//import androidx.compose.foundation.gestures.waitForUpOrCancellation
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.PlayCircleOutline
//import androidx.compose.material3.Icon
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import coil.compose.AsyncImage
//import coil.request.CachePolicy
//import coil.request.ImageRequest
//import com.moussa79m.domain.Model.MediaItem
//
//@Composable
//fun MediaItemCard(
//    mediaItem: MediaItem,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Box(
//        // الـ modifier اللي جي من بره جواه الـ pinchItem
//        modifier = modifier
//            .aspectRatio(1f) // عشان الصور تطلع مربعة ومنتظمة في الشبكة
//            .pointerInput(Unit) {
//                // 🚀 حساس اللمس المخصص عشان الزووم والضغطة يشتغلوا مع بعض بدون مشاكل
//                awaitPointerEventScope {
//                    while (true) {
//                        val down = awaitFirstDown(requireUnconsumed = false)
//                        val up = waitForUpOrCancellation()
//                        if (up != null) {
//                            up.consume()
//                            onClick()
//                        }
//                    }
//                }
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(mediaItem.uri)
//                .size(300) // 🚀 لإنعاش الأداء وتقليل استهلاك الرامات
//                .memoryCachePolicy(CachePolicy.ENABLED) // القراءة المباشرة من الميموري
//                .crossfade(false) // 🚀 قفلناه عشان الكارتة ماتسقطش فريمات أثناء زووم الـ 50 صورة
//                .build(),
//            contentDescription = mediaItem.displayName,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
//
//        // لو حبيت تعرض أيقونة للفيديو
//        if (mediaItem.isVideo) {
//            Icon(
//                imageVector = Icons.Default.PlayCircleOutline,
//                contentDescription = "Video",
//                tint = Color.White.copy(alpha = 0.8f),
//                modifier = Modifier
//                    .size(32.dp)
//                    .padding(4.dp)
//                    .align(Alignment.TopEnd)
//            )
//        }
//    }
//}
/// الكود دا بيروح يجيب ال الصور المصغرة ويعرضها
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.moussa79m.domain.Model.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MediaItemCard(
    mediaItem: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // متغير لحفظ الصورة المصغرة بعد جلبها من النظام
    var thumbnailBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    // الـ LaunchedEffect هيشتغل بس لما الكارت يظهر على الشاشة
    // وهيتلغي تلقائياً لو المستخدم عمل سكرول سريع والكارت اختفى
    LaunchedEffect(mediaItem.uri) {
        withContext(Dispatchers.IO) {
            try {
                // loadThumbnail مدعومة فقط من أندرويد 10 (API 29) وأحدث
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val uri = Uri.parse(mediaItem.uri)
                    // بنطلب صورة مقاس 300x300، النظام هيجيبها من الكاش في جزء من الملي ثانية
                    val bitmap = context.contentResolver.loadThumbnail(uri, Size(300, 300), null)
                    thumbnailBitmap = bitmap.asImageBitmap()
                }
            } catch (e: Exception) {
                // لو النظام فشل في توليد الصورة، بنسيب المتغير null عشان Coil يتدخل
            }
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val up = waitForUpOrCancellation()
                        if (up != null) {
                            up.consume()
                            onClick()
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // لو الـ Thumbnail جاهز من النظام (طلقة)، اعرضه فوراً
        if (thumbnailBitmap != null) {
            Image(
                bitmap = thumbnailBitmap!!,
                contentDescription = mediaItem.displayName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // لو الأندرويد أقدم من 10، أو النظام لسه مولدش الثامبنيل، Coil هيقوم بالواجب
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(mediaItem.uri)
                    .size(300)
                    .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                    .crossfade(false)
                    .build(),
                contentDescription = mediaItem.displayName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (mediaItem.isVideo) {
            Icon(
                imageVector = Icons.Default.PlayCircleOutline,
                contentDescription = "Video",
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .size(32.dp)
                    .padding(4.dp)
                    .align(Alignment.TopEnd)
            )
        }
    }
}