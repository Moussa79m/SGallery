package com.moussa79m.domain

import com.moussa79m.domain.UseCases.CopyMediaUseCase
import com.moussa79m.domain.UseCases.CreateAlbumUseCase
import com.moussa79m.domain.UseCases.DeleteMediaUseCase
//import com.moussa79m.domain.UseCases.FilterCorruptMediaUseCase
import com.moussa79m.domain.UseCases.GetAlbumsUseCase
import com.moussa79m.domain.UseCases.GetImagesOnlyUseCase
import com.moussa79m.domain.UseCases.GetMediaUseCase
import com.moussa79m.domain.UseCases.GetVideosOnlyUseCase
import com.moussa79m.domain.UseCases.MoveMediaUseCase
import com.moussa79m.domain.UseCases.getMediaByAlbum

data class GalleryUseCases (
    val getImagesOnly: GetImagesOnlyUseCase,
    val getVideosOnly: GetVideosOnlyUseCase,
    val getAllMedia: GetMediaUseCase,
    val deleteMedia: DeleteMediaUseCase,
    val getAlbumsUseCase: GetAlbumsUseCase,
    val getMediaByAlbumsUseCase: getMediaByAlbum,
//    val filterCorruptMedia: FilterCorruptMediaUseCase,
    val createAlbumUseCase: CreateAlbumUseCase,
    val copyMediaUseCase: CopyMediaUseCase,
    val moveMediaUseCase: MoveMediaUseCase,
    val deleteMediaUseCase: DeleteMediaUseCase

)