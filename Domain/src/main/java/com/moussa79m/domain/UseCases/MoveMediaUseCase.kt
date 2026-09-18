package com.moussa79m.domain.UseCases

import com.moussa79m.domain.Repo.MediaRepository

class MoveMediaUseCase(private val repository: MediaRepository) {
   suspend operator fun invoke( source: String, destination: String): Result<String>{
        return repository.moveMedia(source,destination)
    }
}