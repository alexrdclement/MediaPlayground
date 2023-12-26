package com.alexrdclement.mediaplayground.model.audio

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class TrackId(override val value: String) : MediaItemId

@Serializable
data class Track(
    override val id: TrackId,
    override val title: String,
    override val artists: List<SimpleArtist>,
    val durationMs: Int,
    val trackNumber: Int,
    val previewUrl: String?,
    val simpleAlbum: SimpleAlbum,
) : MediaItem {
    override val images: List<Image>
        get() = simpleAlbum.images

    override val isPlayable: Boolean
        get() = previewUrl != null
}
