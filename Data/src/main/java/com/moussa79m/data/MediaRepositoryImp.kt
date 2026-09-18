package com.moussa79m.data

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import com.moussa79m.domain.Model.Album
import com.moussa79m.domain.Model.MediaItem
import com.moussa79m.domain.Repo.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.IOException

class MediaRepositoryImp(val context: Context) : MediaRepository {

    override fun getAllMedia(): Flow<List<MediaItem>> = flow {
        val mediaList = mutableListOf<MediaItem>()
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Files.getContentUri("external")
        }
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
        )
        val selection = "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR " +
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) AND " +
                "${MediaStore.Files.FileColumns.SIZE}>?"

        val selectionArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
            "0"
        )
        val sortedOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
        context.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortedOrder

        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
            val typeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val mediaType = cursor.getInt(typeCol)
                val addedDate = cursor.getLong(dateCol)
                val displayName = cursor.getString(nameCol) ?: "Unknown"
                val size = cursor.getLong(sizeCol)
                val isVideo = mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO

                val baseUri = if (isVideo) {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                val contentUri = ContentUris.withAppendedId(baseUri, id).toString()
                mediaList.add(
                    MediaItem(
                        id = id,
                        uri = contentUri,
                        addedDate = addedDate,
                        displayName = displayName,
                        size = size,
                        isVideo = isVideo
                    )
                )
            }
        }
        emit(mediaList)

    }.flowOn(Dispatchers.IO)


    //IMAGES
    override fun getImages(): Flow<List<MediaItem>> = flow {
        val imageList = mutableListOf<MediaItem>()
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Files.getContentUri("external")
        }
        // 2. الأعمدة (Projection): بنطلب البيانات اللي تهمنا بس عشان نوفر استهلاك الذاكرة
        //زى select فى sql
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.MEDIA_TYPE
        )
        val selection = "${MediaStore.Files.FileColumns.SIZE} > ? AND ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"
        // 4. قيم الفلترة (SelectionArgs)
        // بنعوض عن علامة الاستفهام بصفر، يعني (SIZE > 0)
        val selectionArgs = arrayOf("0", MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())

        val sortedOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
        context.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortedOrder
        )?.use { cursor ->
            val idColumn =   cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(collection, id).toString()
                val name = cursor.getString(nameColumn)
                val dateAdded = cursor.getLong(dataColumn)
                val size = cursor.getLong(sizeColumn)


                imageList.add(
                    MediaItem(
                        id = id,
                        uri = contentUri,
                        displayName = name,
                        addedDate = dateAdded,
                        size = size,
                        isVideo = false
                    )
                )

            }
        }
        emit(imageList)

    }.flowOn(Dispatchers.IO)// بننقل الشغلانة دي كلها للـ Background Thread

    //VIDEOS
    override fun getVideos(): Flow<List<MediaItem>> = flow {
        val videoList = mutableListOf<MediaItem>()
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Files.getContentUri("external")
        }
        // 2. الأعمدة (Projection): بنطلب البيانات اللي تهمنا بس عشان نوفر استهلاك الذاكرة
        //زى select فى sql
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.MEDIA_TYPE
        )
        val selection = "${MediaStore.Files.FileColumns.SIZE}>? AND ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"
        // 4. قيم الفلترة (SelectionArgs)
        // بنعوض عن علامة الاستفهام بصفر، يعني (SIZE > 0)
        val selectionArgs = arrayOf("0", MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())

        val sortedOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
        context.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortedOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(collection, id).toString()
                val name = cursor.getString(nameColumn)
                val dateAdded = cursor.getLong(dataColumn)
                val size = cursor.getLong(sizeColumn)


                videoList.add(
                    MediaItem(
                        id = id,
                        uri = contentUri,
                        displayName = name,
                        addedDate = dateAdded,
                        size = size,
                        isVideo = true
                    )
                )

            }
        }
        emit(videoList)

    }.flowOn(Dispatchers.IO)

    override fun getAlbums(): Flow<List<Album>> = flow {
        val albumMap = LinkedHashMap<String, Album>()
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Files.getContentUri("external")
        }
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Files.FileColumns.MEDIA_TYPE
        )
        val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR " +
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? "
        val selectionArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
        context.contentResolver.query(
            collection, projection, selection, selectionArgs, sortOrder
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val bucketNameCol =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)
            val typeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
            while (cursor.moveToNext()) {

                val id = cursor.getLong(idCol)
                val bucketName = cursor.getString(bucketNameCol) ?: "Internal Storage"
                val mediaType = cursor.getInt(typeCol)
                val baseUri = if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                val contentUri = ContentUris.withAppendedId(baseUri, id).toString()
                val existingAlbum = albumMap[bucketName]
                if (existingAlbum == null) {
                    albumMap[bucketName] = Album(
                        albumId = id,
                        albumName = bucketName,
                        albumCoverUri = contentUri,
                        mediaCount = 1
                    )
                } else {
                    albumMap[bucketName] = existingAlbum.copy(
                        mediaCount = existingAlbum.mediaCount + 1
                    )
                }
            }

        }
        emit(albumMap.values.toList())

    }.flowOn(Dispatchers.IO)

    override fun getMediaByAlbum(albumName: String): Flow<List<MediaItem>> = flow {

        val mediaList = mutableListOf<MediaItem>()
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Files.getContentUri("external")
        }
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.MEDIA_TYPE

        )
        val selection =
            "${MediaStore.Files.FileColumns.SIZE}>? AND ${MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME}=?" +
                    "AND (${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?)".trimIndent()
        val selectionArgs = arrayOf(
            "0", albumName, MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

        context.contentResolver.query(collection, projection, selection, selectionArgs, sortOrder)
            ?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val nameCol =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
                val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
                val typeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)

                while (cursor.moveToNext()) {

                    val id = cursor.getLong(idCol)
                    val contentUri = ContentUris.withAppendedId(collection, id).toString()
                    val name = cursor.getString(nameCol)
                    val mediaType = cursor.getInt(typeCol)
                    val date = cursor.getLong(dateCol)
                    val size = cursor.getLong(sizeCol)
                    val isVideo = mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO

                    mediaList.add(
                        MediaItem(
                            id = id,
                            uri = contentUri,
                            displayName = name,
                            addedDate = date,
                            size = size,
                            isVideo = isVideo
                        )
                    )
                }
            }
        emit(mediaList)

    }.flowOn(Dispatchers.IO)

    override suspend fun deleteMedia(uriString: String): Result<Unit> {

        return try {
            val uri = uriString.toUri()
            val deletedRows = context.contentResolver.delete(uri, null, null)
            if (deletedRows > 0) {
                Result.success(Unit)
            } else Result.failure(Exception("file you want to delete not found"))
        } catch (e: SecurityException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createAlbum(albumName: String): Result<String> {
        return try {
            val picturersDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val newAlbumDir = File(picturersDir, albumName)

            if (!newAlbumDir.exists()) {
                val isCreated = newAlbumDir.mkdirs()
                if (isCreated) {
                    Result.success(albumName)
                } else {
                    Result.failure(Exception("فشل النظام في إنشاء المجلد"))
                }
            } else {
                Result.success(albumName)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    ///////////        sourceUri: String,
    override suspend fun copyMedia(
        sourceUri: String,
        destinationUri: String
    ): Result<String> {
        return try {
            val sourceUri = sourceUri.toUri()
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "copy_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$destinationUri")
                }
            }
            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            val newUri = context.contentResolver.insert(collection, contentValues)
                ?: throw Exception("فشل في إنشاء ملف جديد")
            context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                context.contentResolver.openOutputStream(newUri)?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Result.success(newUri.toString())
        } catch (e: SecurityException) {
            Result.failure(Exception("ليس لديك صلاحية قراءة هذا الملف"))
        } catch (e: IOException) {
            // لو المساحة مليانة أو حصل قطع أثناء النسخ
            Result.failure(Exception(" :فشل في نسخ البيانات، قد تكون المساحة ممتلئة${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun moveMedia(
        sourceUri: String,
        destinationUri: String
    ): Result<String> {
        val copyResult = copyMedia(sourceUri, destinationUri)
        return copyResult.fold(
            onSuccess = { newUriString ->
                val deleteResult = deleteMedia(sourceUri)
                deleteResult.fold(
                    onSuccess = { Result.success(newUriString) }, onFailure = { exception ->
                        try {
                            context.contentResolver.delete(newUriString.toUri(), null, null)
                        } catch (rollbackException: Exception) {
                        }
                        Result.failure(exception)
                    }
                )
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }
}