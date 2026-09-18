package com.moussa79m.presentation.UI.Components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.moussa79m.domain.Model.MediaItem
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MemoriesBanner(modifier: Modifier,mediaList: List<MediaItem>) {
    if (mediaList.isEmpty()) return
    val memories = remember(mediaList) {
        mediaList.filter { media -> !media.isVideo }.shuffled().take(5)
    }
    if (mediaList.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { memories.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % memories.size
            pagerState.animateScrollToPage(nextPage, animationSpec = tween(durationMillis = 800)
            )}
    }


    HorizontalPager(
        state = pagerState,

        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(bottom = 4.dp)
    ) { page ->
        val mediaItem = memories[page]
        val infiniteTransition = rememberInfiniteTransition(label = "ken_burns")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 10000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "scale_animation"
        )
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(mediaItem.uri)
                    .crossfade(true).build(),
                contentDescription = "Memories",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    })



//            Text(
//                text = "الذكريات",
//                style = MaterialTheme.typography.headlineSmall,
//                color = Color.White,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier
//                    .align(Alignment.BottomStart)
//                    .padding(16.dp)
//            )
        }
    }
}
