package com.moussa79m.domain.UseCases

import com.moussa79m.domain.Model.MediaItem
import com.moussa79m.domain.Repo.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class getMediaByAlbum(val repository: MediaRepository) {
    operator fun invoke(albumName: String): Flow<List<MediaItem>>{

        return repository.getMediaByAlbum(albumName).map { media->media.filter { it.size>0L } }
    }
}