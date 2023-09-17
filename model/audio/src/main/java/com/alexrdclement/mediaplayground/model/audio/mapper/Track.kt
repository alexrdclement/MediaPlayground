package com.alexrdclement.mediaplayground.model.audio.mapper

import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.Track

fun SimpleTrack.toTrack(
    simpleAlbum: SimpleAlbum,
) = Track(
    id = id,
    title = name,
    durationMs = durationMs,
    trackNumber = trackNumber,
    previewUrl = previewUrl,
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
    previewUrl = previewUrl,
    artists = artists,
    simpleAlbum = album.toSimpleAlbum(),
)
