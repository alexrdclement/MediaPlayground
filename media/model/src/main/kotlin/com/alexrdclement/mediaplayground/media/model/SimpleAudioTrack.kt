package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet

data class SimpleAudioTrack(
    val id: TrackId,
    val name: String,
    val artists: PersistentList<Artist>,
    val trackNumber: Int?,
    val clips: PersistentSet<TrackClip<TimeUnit.Samples>>,
) {
    val duration: TimeUnit = clips.maxByOrNull { it.trackOffset.toKotlinDuration() }
        ?.let { it.trackOffset + it.clip.duration }
        ?: TimeUnit.Samples(0L, DEFAULT_SAMPLE_RATE)

    val isPlayable: Boolean
        get() = clips.isNotEmpty()
}
