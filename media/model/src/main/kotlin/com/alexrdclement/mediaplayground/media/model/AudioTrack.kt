package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class AudioTrack(
    override val id: TrackId,
    override val title: String,
    override val artists: PersistentList<Artist>,
    override val trackNumber: Int?,
    val clips: PersistentSet<TrackClip<TimeUnit.Samples>>,
    override val albums: PersistentList<SimpleAlbum>,
    override val notes: String?,
    override val createdAt: Instant,
    override val modifiedAt: Instant,
) : Track, AudioItem {
    override val images: PersistentList<Image> = albums.firstOrNull()?.images ?: persistentListOf()
    override val duration: TimeUnit = clips.duration
    override val isPlayable: Boolean = clips.isNotEmpty()
}
