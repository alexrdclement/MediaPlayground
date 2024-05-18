package com.alexrdclement.mediaplayground.data.audio.spotify.mapper

import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.adamratzman.spotify.models.Album as SpotifyAlbum

fun SpotifyAlbum.toAlbum() = Album(
    id = AlbumId(this.id),
    title = this.name,
    artists = this.artists.map { it.toSimpleArtist() },
    images = this.images?.map { it.toImage() } ?: emptyList(),
    tracks = this.tracks.mapNotNull { it?.toSimpleTrack() },
)
