package com.alexrdclement.mediaplayground.model.audio

data class Track(
    val id: String,
    val name: String,
    val durationMs: Int,
    val trackNumber: Int,
    val previewUrl: String?,
    val artists: List<SimpleArtist>,
    val simpleAlbum: SimpleAlbum,
)
