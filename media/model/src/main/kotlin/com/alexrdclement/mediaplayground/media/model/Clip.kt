package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ClipId(override val value: String) : MediaItemId

@Serializable
data class Clip(
    override val id: ClipId,
    override val title: String,
    override val duration: TimeUnit,
    val mediaAsset: MediaAsset,
    val assetOffset: TimeUnit,
) : MediaItem {
    override val images: PersistentList<Image> = when (mediaAsset) {
        is AudioAsset -> mediaAsset.images
        is Image -> persistentListOf(mediaAsset)
    }

    override val isPlayable: Boolean = true
}

val <T : TimeUnit> PersistentSet<TrackClip<T>>.duration: TimeUnit
    get() {
        val lastTrackClip = this.maxByOrNull { it.trackOffset.toKotlinDuration() }
            ?: return TimeUnit.Samples(0L, 44100)
        return lastTrackClip.trackOffset + lastTrackClip.clip.duration
    }
