package com.moussa79m.presentation.UI.Components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayer(
    uri: String,
    isCurrentPage: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
// 1. تهيئة ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            prepare()
        }
    }
    // 2. إيقاف الفيديو لو المستخدم عمل Swipe لصورة تانية
    LaunchedEffect(isCurrentPage) {
        if (!isCurrentPage) {
            exoPlayer.pause()
        }
    }
    // 3. تحرير الذاكرة (Memory Leak Prevention) لما الشاشة تتقفل
    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }
    // 4. دمج مشغل الأندرويد داخل Compose
    AndroidView(
        factory = {ctx->
            PlayerView(ctx).apply {
                player=exoPlayer
                useController=true//إظهار شريط التحكم (تشغيل/إيقاف/تقديم)
            }
        },
        modifier= Modifier.fillMaxSize()
    )
}