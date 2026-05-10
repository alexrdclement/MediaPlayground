package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
sealed interface MediaAssetId : MediaItemId {
    override val value: String
}

@Serializable
sealed interface MediaAsset : MediaItem {
    override val id: MediaAssetId
    val uri: MediaAssetUri
    val originUri: MediaAssetOriginUri
    val createdAt: Instant
    val modifiedAt: Instant
}
