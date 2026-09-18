package com.moussa79m.domain.UseCases

import com.moussa79m.domain.Model.MediaItem
import com.moussa79m.domain.Repo.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.print.attribute.standard.Destination

class CopyMediaUseCase(val repository: MediaRepository) {
    suspend  operator fun invoke(sourceUri: String,destinationUri: String): Result<String> {
        return repository.copyMedia(sourceUri,destinationUri)
    }
}