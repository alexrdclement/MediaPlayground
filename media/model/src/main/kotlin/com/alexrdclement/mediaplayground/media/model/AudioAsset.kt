package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@JvmInline
@Serializable
value class AudioAssetId(override val value: String) : MediaAssetId

@Serializable
data class AudioAsset(
    override val id: AudioAssetId,
    override val uri: MediaAssetUri,
    override val originUri: MediaAssetOriginUri,
    override val createdAt: Instant,
    val artists: PersistentList<Artist>,
    val images: PersistentList<Image>,
    val metadata: MediaMetadata.Audio,
) : MediaAsset
