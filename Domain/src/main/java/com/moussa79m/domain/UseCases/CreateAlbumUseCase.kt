package com.moussa79m.domain.UseCases

import com.moussa79m.domain.Model.MediaItem
import com.moussa79m.domain.Repo.MediaRepository
import kotlinx.coroutines.flow.Flow

class CreateAlbumUseCase(val repository: MediaRepository) {
   suspend operator fun invoke(albumName: String): Result<String> {
        return repository.createAlbum(albumName)
    }
    }
