package com.alexrdclement.mediaplayground.model.audio

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@JvmInline
@Serializable
value class AlbumId(override val value: String) : MediaItemId

data class Album(
    override val id: AlbumId,
    override val title: String,
    override val artists: PersistentList<SimpleArtist>,
    override val images: PersistentList<Image>,
    val tracks: PersistentList<SimpleTrack>,
    override val source: Source,
) : MediaItem {
    override val isPlayable: Boolean
        get() = tracks.any { it.uri != null }

    override val duration: Duration
        get() = tracks.sumOf { it.duration.inWholeSeconds }.seconds
}
