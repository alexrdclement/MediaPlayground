package com.alexrdclement.mediaplayground.data.audio.model

data class Album(
    val id: String,
    val name: String,
    val artists: List<SimpleArtist>,
    val images: List<Image>,
    val tracks: List<SimpleTrack>,
)
