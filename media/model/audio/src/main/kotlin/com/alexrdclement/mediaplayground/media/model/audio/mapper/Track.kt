package com.alexrdclement.mediaplayground.media.model.audio.mapper

import com.alexrdclement.mediaplayground.media.model.audio.Album
import com.alexrdclement.mediaplayground.media.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.media.model.audio.Track

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
