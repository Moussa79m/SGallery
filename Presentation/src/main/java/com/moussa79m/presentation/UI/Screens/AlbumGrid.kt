package com.moussa79m.presentation.UI.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moussa79m.domain.Model.Album
import com.moussa79m.presentation.UI.Components.AlbumItemCard

@Composable
fun AlbumGrid(
    albums: List<Album>,
    onAlbumClick: (String) -> Unit
) {
//    BackHandler() { }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(albums, key = { it.albumId }) { album ->
            AlbumItemCard(
                album = album,
                onClick = { onAlbumClick(album.albumName) }
            )
        }
    }
}