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
    val title: String,
    val duration: TimeUnit,
    val mediaAsset: MediaAsset,
    val assetOffset: TimeUnit,
) : MediaItem {
    val artists: PersistentList<Artist> = when (mediaAsset) {
        is AudioAsset -> mediaAsset.artists
        is Image -> persistentListOf()
    }

    val images: PersistentList<Image> = when (mediaAsset) {
        is AudioAsset -> mediaAsset.images
        is Image -> persistentListOf(mediaAsset)
    }

    val isPlayable: Boolean = true
}

val <T : TimeUnit> PersistentSet<TrackClip<T>>.duration: TimeUnit
    get() {
        val lastTrackClip = this.maxByOrNull { it.trackOffset.toKotlinDuration() }
            ?: return TimeUnit.Samples(0L, 44100)
        return lastTrackClip.trackOffset + lastTrackClip.clip.duration
    }
