package com.moussa79m.presentation

import com.moussa79m.domain.Model.Album
import com.moussa79m.domain.Model.MediaItem

data class GalleryStates(
    val currentFilter: GalleryFilter= GalleryFilter.All,
    val mediaList : List<MediaItem> = emptyList(),
    val groupedMedia: Map<String, List<MediaItem>> =emptyMap(),
    val albumsList: List<Album> = emptyList(),
    val isLoading : Boolean = true,
    val errorMessage: String? = null
)