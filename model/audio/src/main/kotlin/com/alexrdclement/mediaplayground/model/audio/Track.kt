package com.alexrdclement.mediaplayground.model.audio

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@JvmInline
@Serializable
value class TrackId(override val value: String) : MediaItemId

@Serializable
data class Track(
    override val id: TrackId,
    override val title: String,
    override val artists: List<SimpleArtist>,
    override val duration: Duration,
    val trackNumber: Int?,
    val uri: String?,
    val simpleAlbum: SimpleAlbum,
) : MediaItem {
    override val images: List<Image>
        get() = simpleAlbum.images

    override val isPlayable: Boolean
        get() = uri != null
}
