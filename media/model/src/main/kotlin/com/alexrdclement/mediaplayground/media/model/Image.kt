package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@JvmInline
@Serializable
value class ImageId(override val value: String) : MediaAssetId

@Serializable
data class Image(
    override val id: ImageId,
    override val uri: MediaAssetUri,
    override val originUri: MediaAssetOriginUri,
    override val createdAt: Instant,
    override val modifiedAt: Instant,
    val mimeType: String,
    val extension: String,
    val widthPx: Int,
    val heightPx: Int,
    val dateTimeOriginal: String? = null,
    val gpsLatitude: Double? = null,
    val gpsLongitude: Double? = null,
    val cameraMake: String? = null,
    val cameraModel: String? = null,
    val notes: String? = null,
) : MediaAsset
