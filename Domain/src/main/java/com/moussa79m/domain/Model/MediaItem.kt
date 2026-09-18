package com.moussa79m.domain.Model

import net.jcip.annotations.Immutable

@Immutable
data class MediaItem (
    val id: Long,
    val uri: String,
    val addedDate: Long,
    val displayName: String,
    val isVideo: Boolean=false,
    val size: Long
)