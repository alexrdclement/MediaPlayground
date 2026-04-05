package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageMetadata(
    val widthPx: Int? = null,
    val heightPx: Int? = null,
    val dateTimeOriginal: String? = null,
    val gpsLatitude: Double? = null,
    val gpsLongitude: Double? = null,
    val cameraMake: String? = null,
    val cameraModel: String? = null,
)
