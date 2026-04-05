package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ImageId(val value: String)

@Serializable
data class Image(
    val id: ImageId,
    val uri: String,
    val metadata: ImageMetadata? = null,
    val notes: String? = null,
)
