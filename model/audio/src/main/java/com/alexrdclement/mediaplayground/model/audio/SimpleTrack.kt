package com.alexrdclement.mediaplayground.model.audio

data class SimpleTrack(
    val id: String,
    val name: String,
    val durationMs: Int,
    val trackNumber: Int,
    val previewUrl: String?,
)
