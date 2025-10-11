package com.alexrdclement.mediaplayground.data.audio.spotify.mapper

import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlin.time.Duration.Companion.milliseconds
import com.adamratzman.spotify.models.SimpleTrack as SpotifySimpleTrack

fun SpotifySimpleTrack.toSimpleTrack() = SimpleTrack(
    id = TrackId(this.id),
    name = this.name,
    artists = this.artists.map { it.toSimpleArtist() },
    duration = this.durationMs.milliseconds,
    trackNumber = this.trackNumber,
    uri = this.previewUrl,
)
