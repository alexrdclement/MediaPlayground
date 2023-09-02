package com.alexrdclement.mediaplayground.model.audio

import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val id: String,
    val name: String,
    val durationMs: Int,
    val trackNumber: Int,
    val previewUrl: String?,
    val artists: List<SimpleArtist>,
    val simpleAlbum: SimpleAlbum,
)
