package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class MediaAssetId(val value: String)

@Serializable
data class MediaAsset(
    val id: MediaAssetId,
    val uri: String?,
    val source: Source,
    val artists: PersistentList<Artist>,
    val images: PersistentList<Image>,
    val metadata: MediaMetadata,
)
