package com.alexrdclement.mediaplayground.model.audio

import kotlin.time.Duration

data class SimpleTrack(
    val id: TrackId,
    val name: String,
    val artists: List<SimpleArtist>,
    val duration: Duration,
    val trackNumber: Int?,
    val uri: String?,
)
