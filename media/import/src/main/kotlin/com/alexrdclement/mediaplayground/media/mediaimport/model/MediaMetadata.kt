package com.alexrdclement.mediaplayground.media.mediaimport.model

import android.net.Uri

data class MediaMetadata(
    val title: String?,
    val durationMs: Long?,
    val trackNumber: Int?,
    val artistName: String?,
    val albumTitle: String?,
    val imageUri: Uri?,
)
