package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class AudioTrack(
    override val id: TrackId,
    override val title: String,
    override val items: PersistentList<TrackClip<TimeUnit.Samples>>,
    override val notes: String?,
    override val createdAt: Instant,
    override val modifiedAt: Instant,
) : Track, AudioItem {
    override val artists: PersistentList<Artist> = items.flatMap { it.clip.artists }.distinct().toPersistentList()
    override val images: PersistentList<Image> = items.flatMap { it.clip.images }.toPersistentList()
    override val duration: TimeUnit = items.lastOrNull()
        ?.let { it.trackOffset + it.clip.duration }
        ?: TimeUnit.Samples(0L, DEFAULT_SAMPLE_RATE)
}
