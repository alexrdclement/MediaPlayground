package com.alexrdclement.mediaplayground.model.audio

data class Album(
    val id: String,
    val name: String,
    val artists: List<SimpleArtist>,
    val images: List<Image>,
    val tracks: List<SimpleTrack>,
)
