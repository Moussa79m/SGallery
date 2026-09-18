package com.moussa79m.domain.UseCases

import com.moussa79m.domain.Model.MediaItem
import com.moussa79m.domain.Repo.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMediaUseCase(val repository: MediaRepository) {
    operator fun invoke(): Flow<List<MediaItem>> {
        return repository.getAllMedia().map {mediaList->
            mediaList.filter { media ->media.size>0 }
        }
    }
}