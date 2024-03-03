package com.alexrdclement.mediaplayground.media.mediaimport.model

import kotlinx.io.files.Path

data class MediaMetadata(
    val title: String?,
    val durationMs: Long?,
    val trackNumber: Int?,
    val artistName: String?,
    val albumTitle: String?,
    val imagePath: Path?,
)
