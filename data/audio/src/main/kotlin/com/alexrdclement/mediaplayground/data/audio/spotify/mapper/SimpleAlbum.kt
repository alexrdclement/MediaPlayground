package com.alexrdclement.mediaplayground.data.audio.spotify.mapper

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.Source
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import com.adamratzman.spotify.models.SimpleAlbum as SpotifySimpleAlbum

fun SpotifySimpleAlbum.toSimpleAlbum() = SimpleAlbum(
    id = AlbumId(this.id),
    name = this.name,
    artists = this.artists.map { it.toSimpleArtist() }.toPersistentList(),
    images = this.images?.map { it.toImage() }?.toPersistentList() ?: persistentListOf(),
    source = Source.Spotify,
)
