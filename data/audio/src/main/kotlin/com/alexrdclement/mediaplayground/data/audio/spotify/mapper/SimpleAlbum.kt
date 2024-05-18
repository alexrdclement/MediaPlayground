package com.alexrdclement.mediaplayground.data.audio.spotify.mapper

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.adamratzman.spotify.models.SimpleAlbum as SpotifySimpleAlbum

fun SpotifySimpleAlbum.toSimpleAlbum() = SimpleAlbum(
    id = AlbumId(this.id),
    name = this.name,
    artists = this.artists.map { it.toSimpleArtist() },
    images = this.images?.map { it.toImage() } ?: emptyList()
)
