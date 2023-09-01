package com.alexrdclement.mediaplayground.data.audio.spotify.mapper

import com.alexrdclement.mediaplayground.data.audio.model.SimpleTrack
import com.adamratzman.spotify.models.SimpleTrack as SpotifySimpleTrack

fun SpotifySimpleTrack.toSimpleTrack() = SimpleTrack(
    id = this.id,
    name = this.name,
    durationMs = this.durationMs,
    trackNumber = this.trackNumber,
    previewUrl = this.previewUrl,
)
