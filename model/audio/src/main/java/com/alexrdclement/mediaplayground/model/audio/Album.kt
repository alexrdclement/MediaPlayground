package com.alexrdclement.mediaplayground.model.audio

data class Album(
    override val id: String,
    override val title: String,
    override val artists: List<SimpleArtist>,
    override val images: List<Image>,
    val tracks: List<SimpleTrack>,
) : MediaItem {
    override val isPlayable: Boolean
        get() = tracks.any { it.previewUrl != null }
}
