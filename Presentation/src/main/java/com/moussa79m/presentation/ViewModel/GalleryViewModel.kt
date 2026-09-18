package com.moussa79m.presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moussa79m.domain.GalleryUseCases
import com.moussa79m.domain.Model.MediaItem
import com.moussa79m.presentation.GalleryFilter
import com.moussa79m.presentation.GalleryStates
import com.moussa79m.presentation.GalleryUiEvents
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class GalleryViewModel(
    private val useCase: GalleryUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryStates())
    val state = _state.asStateFlow()

    private var mediaJob: Job? = null
    private val _uiEvent = MutableSharedFlow<GalleryUiEvents>()
    private val uiEvents = _uiEvent.asSharedFlow()

    init {
        loadMedia(GalleryFilter.All)
        loadAlbums()
    }

    fun loadMedia(filter: GalleryFilter) {
        _state.value = _state.value.copy(
            currentFilter = filter,
            isLoading = true
        )
        mediaJob?.cancel()

        mediaJob = viewModelScope.launch {
            val flow = when (filter) {
                is GalleryFilter.All -> useCase.getAllMedia()
                is GalleryFilter.Images -> useCase.getImagesOnly()
                is GalleryFilter.Videos -> useCase.getVideosOnly()
                is GalleryFilter.Album -> useCase.getMediaByAlbumsUseCase(filter.albumName)
            }
            flow.catch { error ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "حدث خطأ غير متوقع أثناء جلب البيانات"
                )
            }.collect { media ->
                val grouped = groupMediaByDate(false,media)
                _state.value = _state.value.copy(
                    mediaList = media,
                    groupedMedia = grouped,
                    isLoading = false
                )
            }
        }
    }

    private fun loadAlbums(
    ) {
        viewModelScope.launch {
            useCase.getAlbumsUseCase().collect { albums ->
                _state.value = _state.value.copy(albumsList = albums)
            }

        }
    }

    fun deleteMediaItem(uri: String) {
        viewModelScope.launch {
            val result = useCase.deleteMedia(uri)
            result.fold(
                onSuccess = {
                    _uiEvent.emit(GalleryUiEvents.ShowToast("successful delete"))
                },
                onFailure = { error ->
                    if (error is SecurityException) {
                        _uiEvent.emit(GalleryUiEvents.RequestDeletePermission(uri))
                    } else {
                        _uiEvent.emit(
                            GalleryUiEvents.ShowToast(
                                error.message ?: " unExpected error"
                            )
                        )
                    }
                }
            )
        }
    }

    private fun groupMediaByDate(orderByDay: Boolean , mediaList: List<MediaItem>): Map<String, List<MediaItem>> {
        // تنسيق التاريخ (مثال: "25 أغسطس 2026")
        // ممكن تخلي الـ Locale إنجليزي لو حابب (Locale.ENGLISH)

        val dateFormat = if(orderByDay){SimpleDateFormat("dd MMMM yyyy", Locale("ar"))}
        else{
            SimpleDateFormat("MMMM yyyy", Locale("ar"))
        }
        return mediaList.groupBy { mediaItem ->
// لاحظ: dateAdded من الـ MediaStore غالباً بيكون بالثواني، فهنضربه في 1000
            val date = Date(mediaItem.addedDate * 1000)
            dateFormat.format(date)
        }
    }
}