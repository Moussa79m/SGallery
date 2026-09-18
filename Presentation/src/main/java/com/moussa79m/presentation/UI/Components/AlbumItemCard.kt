package com.moussa79m.presentation.UI.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.moussa79m.domain.Model.Album

@Composable
fun AlbumItemCard(
    album: Album,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(album.albumCoverUri)
                .crossfade(true).build(),
            contentDescription = album.albumName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
// تدرج لوني (Gradient) من اليسار لليمين عشان يبرز النص زي تصميم سوني
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = .7f), Color.Transparent
                        ),
                        startX = 0f,
                        endX = 600f
                    )
                )
        )
// النصوص (اسم الألبوم وعدد العناصر) محاذاة لليسار

        Column(modifier= Modifier.padding(end = 24.dp).align(Alignment.CenterEnd)) {
            Text(
                text = album.albumName,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                fontWeight = FontWeight.Light,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${album.mediaCount} times ",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                color = Color.White.copy(alpha = .9f)
            )
        }

    }

}

