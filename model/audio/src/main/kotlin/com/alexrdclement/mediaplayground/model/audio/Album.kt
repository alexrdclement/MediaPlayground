package com.alexrdclement.mediaplayground.model.audio

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@JvmInline
@Serializable
value class AlbumId(override val value: String) : MediaItemId

data class Album(
    override val id: AlbumId,
    override val title: String,
    override val artists: List<SimpleArtist>,
    override val images: List<Image>,
    val tracks: List<SimpleTrack>,
) : MediaItem {
    override val isPlayable: Boolean
        get() = tracks.any { it.uri != null }

    override val duration: Duration
        get() = tracks.sumOf { it.duration.inWholeSeconds }.seconds
}
