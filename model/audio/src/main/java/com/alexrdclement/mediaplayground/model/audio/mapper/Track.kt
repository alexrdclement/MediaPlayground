package com.alexrdclement.mediaplayground.model.audio.mapper

import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.Track

fun SimpleTrack.toTrack(
    artists: List<SimpleArtist>,
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
