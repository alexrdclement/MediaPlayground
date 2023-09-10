package com.alexrdclement.mediaplayground.data.audio.spotify.mapper

import com.alexrdclement.mediaplayground.model.audio.Track
import com.adamratzman.spotify.models.Track as SpotifyTrack

fun SpotifyTrack.toTrack() = Track(
    id = this.id,
    title = this.name,
    durationMs = this.durationMs,
    trackNumber = this.trackNumber,
    previewUrl = this.previewUrl,
    artists = this.artists.map { it.toSimpleArtist() },
    simpleAlbum = this.album.toSimpleAlbum(),
)
