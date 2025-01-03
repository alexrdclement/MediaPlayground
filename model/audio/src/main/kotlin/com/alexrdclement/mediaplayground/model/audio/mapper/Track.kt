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
    durationMs = durationMs,
    trackNumber = trackNumber,
    uri = uri,
    artists = artists,
    simpleAlbum = simpleAlbum,
)

fun SimpleTrack.toTrack(
    album: Album,
) = Track(
    id = id,
    title = name,
    durationMs = durationMs,
    trackNumber = trackNumber,
    uri = uri,
    artists = artists,
    simpleAlbum = album.toSimpleAlbum(),
)

fun Track.toSimpleTrack() = SimpleTrack(
    id = id,
    name = title,
    durationMs = durationMs,
    trackNumber = trackNumber,
    uri = uri,
    artists = artists,
)
