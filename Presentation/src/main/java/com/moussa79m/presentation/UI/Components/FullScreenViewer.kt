package com.moussa79m.presentation.UI.Components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.moussa79m.domain.Model.MediaItem

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FullScreenViewer(
    mediaList: List<MediaItem>,
    onDismiss: () -> Unit,
    initialIndex: Int
) {
    val pagerState = rememberPagerState(initialPage = initialIndex, pageCount = { mediaList.size })
    val currentMediaItem = mediaList[pagerState.currentPage]

    // 1. تغيير val إلى var عشان نقدر نغير قيمتها
    var showAppBar by remember { mutableStateOf(true) }

    BackHandler { onDismiss() }
    var isZoomed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // الـ TopAppBar اتشال من هنا عشان يترسم في الآخر

        HorizontalPager(
            modifier = Modifier.fillMaxSize(), // شيلنا الـ clickable من هنا
            state = pagerState,
            userScrollEnabled = !isZoomed
        ) { page ->
            val mediaItem = mediaList[page]
            val isCurrentPage = pagerState.currentPage == page

            if (mediaItem.isVideo) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        // تفعيل الضغطة لإخفاء الشريط في حالة الفيديو كمان
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { showAppBar = !showAppBar })
                        },
                    contentAlignment = Alignment.Center
                ) {
                    VideoPlayer(uri = mediaItem.uri, isCurrentPage = isCurrentPage)

//                    Icon(
//                        imageVector = Icons.Default.PlayCircleOutline,
//                        contentDescription = "play video",
//                        tint = Color.White.copy(alpha = .8f),
//                        modifier = Modifier.size(80.dp)
//                    )
                }
            } else {
                var scale by remember { mutableFloatStateOf(1f) }
                var offset by remember { mutableStateOf(Offset.Zero) }

                LaunchedEffect(scale) {
                    isZoomed = scale > 1f
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    if (scale > 1f) {
                                        scale = 1f
                                        offset = Offset.Zero
                                    } else {
                                        scale = 3f
                                    }
                                },
                                // 2. السحر هنا: دمجنا الضغطة الواحدة مع الضغطتين في نفس المكان
                                onTap = {
                                    showAppBar = !showAppBar
                                }
                            )
                        }
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                // ... (كود الـ Pan والـ Zoom بتاعك زي ما هو بدون تعديل) ...
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val pointers = event.changes.size

                                    if (pointers > 1) {
                                        val zoomChange = event.calculateZoom()
                                        val panChange = event.calculatePan()

                                        scale = (scale * zoomChange).coerceIn(1f, 5f)
                                        if (scale > 1f) {
                                            val maxX = (size.width * (scale - 1)) / 2
                                            val maxY = (size.height * (scale - 1)) / 2
                                            offset = Offset(
                                                x = (offset.x + panChange.x).coerceIn(-maxX, maxX),
                                                y = (offset.y + panChange.y).coerceIn(-maxY, maxY)
                                            )
                                        } else {
                                            offset = Offset.Zero
                                        }
                                        event.changes.forEach { if (it.positionChanged()) it.consume() }
                                    } else if (pointers == 1 && scale > 1f) {
                                        val panChange = event.calculatePan()
                                        val maxX = (size.width * (scale - 1)) / 2
                                        val maxY = (size.height * (scale - 1)) / 2

                                        offset = Offset(
                                            x = (offset.x + panChange.x).coerceIn(-maxX, maxX),
                                            y = (offset.y + panChange.y).coerceIn(-maxY, maxY)
                                        )
                                        event.changes.forEach { if (it.positionChanged()) it.consume() }
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(mediaItem.uri)
                            .crossfade(true).build(),
                        contentDescription = mediaItem.displayName,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                translationX = offset.x
                                translationY = offset.y
                            })
                }
            }
        }

        // 3. وضعنا الـ TopAppBar في نهاية الـ Box عشان يترسم كطبقة علوية فوق الصور
        if (showAppBar) {
            TopAppBar(
                title = {
                    Text(
                        text = currentMediaItem.displayName,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.ArrowBack, contentDescription = "back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = .4f)
                ),
                modifier = Modifier.align(Alignment.TopStart)
            )
        }
    }
}