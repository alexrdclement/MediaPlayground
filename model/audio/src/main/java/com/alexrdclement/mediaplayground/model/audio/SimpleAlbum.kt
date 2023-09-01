package com.alexrdclement.mediaplayground.model.audio

data class SimpleAlbum(
    val id: String,
    val name: String,
    val artists: List<SimpleArtist>,
    val images: List<Image>,
)
