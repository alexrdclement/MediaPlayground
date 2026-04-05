package com.alexrdclement.mediaplayground.media.model.mapper

import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.SimpleTrack
import com.alexrdclement.mediaplayground.media.model.Track

fun SimpleTrack.toTrack(
    simpleAlbum: SimpleAlbum,
) = Track(
    id = id,
    title = name,
    duration = duration,
    trackNumber = trackNumber,
    uri = uri,
    artists = artists,
    simpleAlbum = simpleAlbum,
    source = source,
    notes = null,
)

fun SimpleTrack.toTrack(
    album: Album,
) = Track(
    id = id,
    title = name,
    duration = duration,
    trackNumber = trackNumber,
    uri = uri,
    artists = artists,
    simpleAlbum = album.toSimpleAlbum(),
    source = source,
    notes = null,
)

fun Track.toSimpleTrack() = SimpleTrack(
    id = id,
    name = title,
    duration = duration,
    trackNumber = trackNumber,
    uri = uri,
    artists = artists,
    source = source,
)
