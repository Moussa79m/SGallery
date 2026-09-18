package com.moussa79m.domain.UseCases

import com.moussa79m.domain.Model.MediaItem
import com.moussa79m.domain.Repo.MediaRepository
import kotlinx.coroutines.flow.Flow

class DeleteMediaUseCase(val repository: MediaRepository) {
    suspend operator fun invoke(uri: String): Result<Unit> {
        return repository.deleteMedia(uri)
    }
    }
