package com.alexrdclement.mediaplayground.media.mediaimport.model

data class MediaMetadata(
    val id: Long?,
    val title: String?,
    val durationMs: Long,
    val trackNumber: Int?,
    val artistId: Long?,
    val artistName: String?,
    val albumId: Long?,
    val albumTitle: String?,
)
