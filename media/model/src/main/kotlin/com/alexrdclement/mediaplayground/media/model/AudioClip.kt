package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class AudioClip(
    override val id: ClipId,
    override val title: String,
    val mediaAsset: AudioAsset,
    val assetOffset: TimeUnit.Samples,
    override val duration: TimeUnit.Samples,
    override val createdAt: Instant,
    override val modifiedAt: Instant,
) : Clip {
    val artists: PersistentList<Artist> get() = mediaAsset.artists
    override val images: PersistentList<Image> get() = mediaAsset.images
    override val isPlayable: Boolean = true
}
