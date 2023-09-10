package com.alexrdclement.mediaplayground.data.audio.spotify.mapper

import com.alexrdclement.mediaplayground.model.audio.Album
import com.adamratzman.spotify.models.Album as SpotifyAlbum

fun SpotifyAlbum.toAlbum() = Album(
    id = this.id,
    title = this.name,
    artists = this.artists.map { it.toSimpleArtist() },
    images = this.images.map { it.toImage() },
    tracks = this.tracks.mapNotNull { it?.toSimpleTrack() },
)
