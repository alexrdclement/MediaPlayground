package com.alexrdclement.mediaplayground.data.audio.model

data class SimpleAlbum(
    val id: String,
    val name: String,
    val artists: List<SimpleArtist>,
    val images: List<Image>,
)
