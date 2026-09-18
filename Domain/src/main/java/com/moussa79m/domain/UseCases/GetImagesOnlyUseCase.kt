package com.moussa79m.domain.UseCases

import com.moussa79m.domain.Model.MediaItem
import com.moussa79m.domain.Repo.MediaRepository
import kotlinx.coroutines.flow.Flow

class GetImagesOnlyUseCase(val repository: MediaRepository) {
    operator fun invoke(): Flow<List<MediaItem>> {
        return repository.getImages()
        }
    }
