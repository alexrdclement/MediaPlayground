package com.alexrdclement.mediaplayground.model.audio.mapper

import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.Track

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
