package com.alexrdclement.mediaplayground.media.model.mapper

import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.SimpleTrack
import kotlinx.collections.immutable.PersistentList

fun SimpleAlbum.toAlbum(
    tracks: PersistentList<SimpleTrack>,
) = Album(
    id = id,
    title = name,
    artists = artists,
    images = images,
    tracks = tracks,
    source = source,
    notes = null,
)

fun Album.toSimpleAlbum() = SimpleAlbum(
    id = id,
    name = title,
    artists = artists,
    images = images,
    source = source,
)
