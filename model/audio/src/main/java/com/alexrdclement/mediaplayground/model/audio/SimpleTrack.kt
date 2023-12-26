package com.alexrdclement.mediaplayground.model.audio

data class SimpleTrack(
    val id: TrackId,
    val name: String,
    val artists: List<SimpleArtist>,
    val durationMs: Int,
    val trackNumber: Int,
    val previewUrl: String?,
)
