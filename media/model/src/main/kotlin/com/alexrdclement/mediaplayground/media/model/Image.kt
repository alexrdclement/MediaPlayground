package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ImageId(val value: String)

@Serializable
data class Image(
    val id: ImageId,
    val uri: String,
    val widthPx: Int? = null,
    val heightPx: Int? = null,
    val dateTimeOriginal: String? = null,
    val gpsLatitude: Double? = null,
    val gpsLongitude: Double? = null,
    val cameraMake: String? = null,
    val cameraModel: String? = null,
    val notes: String? = null,
)
