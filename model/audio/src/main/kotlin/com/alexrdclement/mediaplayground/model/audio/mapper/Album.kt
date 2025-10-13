package com.alexrdclement.mediaplayground.model.audio.mapper

import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack

fun SimpleAlbum.toAlbum(
    tracks: List<SimpleTrack>,
) = Album(
    id = id,
    title = name,
    artists = artists,
    images = images,
    tracks = tracks,
    source = source,
)

fun Album.toSimpleAlbum() = SimpleAlbum(
    id = id,
    name = title,
    artists = artists,
    images = images,
    source = source,
)
