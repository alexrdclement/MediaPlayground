package com.alexrdclement.mediaplayground.model.audio

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val url: String,
    val heightPx: Int?,
    val widthPx: Int?,
)
