package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class AudioTrack(
    override val id: TrackId,
    override val title: String,
    val clips: PersistentSet<TrackClip<TimeUnit.Samples>>,
    override val notes: String?,
    override val createdAt: Instant,
    override val modifiedAt: Instant,
) : Track, AudioItem {
    override val artists: PersistentList<Artist> = clips.flatMap { it.clip.artists }.distinct().toPersistentList()
    override val images: PersistentList<Image> = clips.flatMap { it.clip.images }.toPersistentList()
    override val duration: TimeUnit = clips.duration
    override val isPlayable: Boolean = clips.isNotEmpty()
}
