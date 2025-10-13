package com.alexrdclement.mediaplayground.data.audio.spotify.mapper

import com.alexrdclement.mediaplayground.model.audio.Source
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlin.time.Duration.Companion.milliseconds
import com.adamratzman.spotify.models.Track as SpotifyTrack

fun SpotifyTrack.toTrack() = Track(
    id = TrackId(this.id),
    title = this.name,
    duration = this.durationMs.milliseconds,
    trackNumber = this.trackNumber,
    uri = this.previewUrl,
    artists = this.artists.map { it.toSimpleArtist() },
    simpleAlbum = this.album.toSimpleAlbum(),
    source = Source.Spotify,
)
