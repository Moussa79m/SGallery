package com.moussa79m.domain.Model

import net.jcip.annotations.Immutable

@Immutable
data class Album (
    val albumId: Long,
    val albumName: String,
//    val albumUri: String,
    val albumCoverUri: String,
    val mediaCount: Long
    )