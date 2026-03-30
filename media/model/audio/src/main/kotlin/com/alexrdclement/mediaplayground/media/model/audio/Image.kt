package com.alexrdclement.mediaplayground.media.model.audio

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ImageId(val value: String)

@Serializable
data class Image(
    val id: ImageId,
    val uri: String,
    val heightPx: Int? = null,
    val widthPx: Int? = null,
    val notes: String? = null,
)
