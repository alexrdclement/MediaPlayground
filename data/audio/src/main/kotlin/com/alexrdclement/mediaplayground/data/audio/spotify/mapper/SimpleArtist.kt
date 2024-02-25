package com.alexrdclement.mediaplayground.data.audio.spotify.mapper

import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.adamratzman.spotify.models.SimpleArtist as SpotifySimpleArtist

fun SpotifySimpleArtist.toSimpleArtist() = SimpleArtist(
    id = this.id,
    name = this.name,
)
