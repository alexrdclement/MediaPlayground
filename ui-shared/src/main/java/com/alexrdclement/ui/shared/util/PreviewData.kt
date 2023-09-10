package com.alexrdclement.ui.shared.util

import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.mapper.toAlbum
import com.alexrdclement.mediaplayground.model.audio.mapper.toTrack


val PreviewSimpleArtist1 = SimpleArtist(
    id = "1",
    name = "Speedy Ortiz",
)

val PreviewSimpleAlbum1 = SimpleAlbum(
    id = "1",
    name = "Major Arcana",
    artists = listOf(PreviewSimpleArtist1),
    images = listOf(),
)

val PreviewSimpleTrack1 = SimpleTrack(
    id = "1",
    name = "Pioneer Spine",
    durationMs = 217000,
    trackNumber = 1,
    previewUrl = null,
)

val PreviewSimpleTrack2 = SimpleTrack(
    id = "2",
    name = "Tiger Tank",
    durationMs = 166000,
    trackNumber = 2,
    previewUrl = null,
)

val PreviewSimpleTrack3 = SimpleTrack(
    id = "3",
    name = "Hitch",
    durationMs = 155000,
    trackNumber = 3,
    previewUrl = null,
)

val PreviewTrack1 = PreviewSimpleTrack1.toTrack(
    artists = listOf(PreviewSimpleArtist1),
    simpleAlbum = PreviewSimpleAlbum1,
)

val PreviewTrack2 = PreviewSimpleTrack2.toTrack(
    artists = listOf(PreviewSimpleArtist1),
    simpleAlbum = PreviewSimpleAlbum1,
)

val PreviewTrack3 = PreviewSimpleTrack3.toTrack(
    artists = listOf(PreviewSimpleArtist1),
    simpleAlbum = PreviewSimpleAlbum1,
)

val PreviewSimpleTracks1 = listOf(
    PreviewSimpleTrack1,
    PreviewSimpleTrack2,
    PreviewSimpleTrack3,
)

val PreviewTracks1 = listOf(
    PreviewTrack1,
    PreviewTrack2,
    PreviewTrack3,
)

val PreviewAlbum1 = PreviewSimpleAlbum1.toAlbum(
    tracks = PreviewSimpleTracks1
)

val PreviewAlbums1 = listOf(
    PreviewAlbum1.copy(id = "1"),
    PreviewAlbum1.copy(id = "2"),
    PreviewAlbum1.copy(id = "3"),
)
