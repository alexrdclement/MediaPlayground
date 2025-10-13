package com.alexrdclement.mediaplayground.model.audio

import kotlinx.collections.immutable.PersistentList
import kotlin.time.Duration

data class SimpleTrack(
    val id: TrackId,
    val name: String,
    val artists: PersistentList<SimpleArtist>,
    val duration: Duration,
    val trackNumber: Int?,
    val uri: String?,
    val source: Source,
)
