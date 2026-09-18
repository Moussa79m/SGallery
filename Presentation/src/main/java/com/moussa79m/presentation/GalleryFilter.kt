package com.moussa79m.presentation

sealed class GalleryFilter {
object All: GalleryFilter()
    object Images: GalleryFilter()
    object Videos: GalleryFilter()
    data class Album(val albumName: String): GalleryFilter()
}