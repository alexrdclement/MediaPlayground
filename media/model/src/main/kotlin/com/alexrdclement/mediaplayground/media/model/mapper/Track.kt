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
    trackNumber = trackNumber,
    artists = artists,
    clips = clips,
    simpleAlbum = simpleAlbum,
    notes = null,
)

fun SimpleTrack.toTrack(
    album: Album,
) = Track(
    id = id,
    title = name,
    trackNumber = trackNumber,
    artists = artists,
    clips = clips,
    simpleAlbum = album.toSimpleAlbum(),
    notes = null,
)

fun Track.toSimpleTrack() = SimpleTrack(
    id = id,
    name = title,
    trackNumber = trackNumber,
    artists = artists,
    clips = clips,
)
