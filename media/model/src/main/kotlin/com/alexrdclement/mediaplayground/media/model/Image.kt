package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
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
) : MediaAsset {
    override val title: String get() = uri.toUriString().substringAfterLast('/')
    override val images: PersistentList<Image> get() = persistentListOf(this)
    override val isPlayable: Boolean get() = true
    override val duration: TimeUnit get() = TimeUnit.Frames(frames = 1L, frameRate = 1.0)
}
