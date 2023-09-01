package com.alexrdclement.ui.shared.util

import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.Track


val PreviewSimpleArtist = SimpleArtist(
    id = "1",
    name = "Speedy Ortiz",
)

val PreviewSimpleAlbum = SimpleAlbum(
    id = "1",
    name = "Major Arcana",
    artists = listOf(PreviewSimpleArtist),
    images = listOf(),
)

val PreviewTrack1 = Track(
    id = "1",
    name = "Pioneer Spine",
    durationMs = 217000,
    trackNumber = 1,
    previewUrl = null,
    artists = listOf(PreviewSimpleArtist),
    simpleAlbum = PreviewSimpleAlbum,
)

val PreviewTrack2 = Track(
    id = "2",
    name = "Tiger Tank",
    durationMs = 166000,
    trackNumber = 2,
    previewUrl = null,
    artists = listOf(PreviewSimpleArtist),
    simpleAlbum = PreviewSimpleAlbum,
)

val PreviewTrack3 = Track(
    id = "3",
    name = "Hitch",
    durationMs = 155000,
    trackNumber = 3,
    previewUrl = null,
    artists = listOf(PreviewSimpleArtist),
    simpleAlbum = PreviewSimpleAlbum,
)

val PreviewTracks = listOf(
    PreviewTrack1,
    PreviewTrack2,
    PreviewTrack3,
)
