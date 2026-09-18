package com.moussa79m.domain.Repo

import com.moussa79m.domain.Model.Album
import com.moussa79m.domain.Model.MediaItem
import kotlinx.coroutines.flow.Flow

interface
MediaRepository {
     fun getAllMedia(): Flow<List<MediaItem>>
    fun getImages(): Flow<List<MediaItem>>
    fun getVideos(): Flow<List<MediaItem>>
    fun getAlbums(): Flow<List<Album>>
    fun getMediaByAlbum(albumName: String): Flow<List<MediaItem>>

    suspend fun deleteMedia(uri: String): Result<Unit>
   suspend fun createAlbum (albumName: String): Result<String>
   suspend fun copyMedia (sourceUri: String,destinationUri: String): Result<String>
   suspend fun moveMedia(sourceUri: String,destinationUri: String): Result<String>

}