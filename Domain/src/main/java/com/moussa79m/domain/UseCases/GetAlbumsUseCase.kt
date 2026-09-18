package com.moussa79m.domain.UseCases

import com.moussa79m.domain.Model.Album
import com.moussa79m.domain.Repo.MediaRepository
import kotlinx.coroutines.flow.Flow

class GetAlbumsUseCase(val repository: MediaRepository) {
    operator fun invoke(): Flow<List<Album>> {
        return repository.getAlbums()
    }
}