package com.moussa79m.presentation

sealed class GalleryUiEvents {
    data class ShowToast(val message: String): GalleryUiEvents()
    data class RequestDeletePermission(val uriString: String): GalleryUiEvents()
}