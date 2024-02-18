package com.alexrdclement.mediaplayground.model.audio

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val uri: String,
    val heightPx: Int? = null,
    val widthPx: Int? = null,
)
