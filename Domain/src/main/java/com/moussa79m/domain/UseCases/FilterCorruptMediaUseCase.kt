//package com.moussa79m.domain.UseCases
//
//import com.moussa79m.domain.Repo.MediaRepository
//
//class FilterCorruptMediaUseCase(val repository: MediaRepository) {
//    operator fun invoke(): Flow<List<MediaItem>>{
//        return repository.getAllMedia().map {imageList->
//            imageList.filter { image ->image.size>0 }
//        }
//    }
//}