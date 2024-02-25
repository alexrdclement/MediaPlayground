package com.alexrdclement.mediaplayground.ui.shared.util

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.audio.mapper.toAlbum
import com.alexrdclement.mediaplayground.model.audio.mapper.toTrack
import com.alexrdclement.mediaplayground.ui.shared.model.MediaItemUi


val PreviewSimpleArtist1 = SimpleArtist(
    id = "1",
    name = "Speedy Ortiz",
)

val PreviewSimpleAlbum1 = SimpleAlbum(
    id = AlbumId("1"),
    name = "Major Arcana",
    artists = listOf(PreviewSimpleArtist1),
    images = listOf(),
)

val PreviewSimpleTrack1 = SimpleTrack(
    id = TrackId("1"),
    name = "Pioneer Spine",
    artists = listOf(PreviewSimpleArtist1),
    durationMs = 217000,
    trackNumber = 1,
    uri = null,
)

val PreviewSimpleTrack2 = SimpleTrack(
    id = TrackId("2"),
    name = "Tiger Tank",
    artists = listOf(PreviewSimpleArtist1),
    durationMs = 166000,
    trackNumber = 2,
    uri = null,
)

val PreviewSimpleTrack3 = SimpleTrack(
    id = TrackId("3"),
    name = "Hitch",
    artists = listOf(PreviewSimpleArtist1),
    durationMs = 155000,
    trackNumber = 3,
    uri = null,
)

val PreviewTrack1 = PreviewSimpleTrack1.toTrack(
    simpleAlbum = PreviewSimpleAlbum1,
)

val PreviewTrack2 = PreviewSimpleTrack2.toTrack(
    simpleAlbum = PreviewSimpleAlbum1,
)

val PreviewTrack3 = PreviewSimpleTrack3.toTrack(
    simpleAlbum = PreviewSimpleAlbum1,
)

val PreviewTrackUi1 = MediaItemUi(
    mediaItem = PreviewTrack1,
    isPlaying = false
)

val PreviewTrackUi2 = MediaItemUi(
    mediaItem = PreviewTrack2,
    isPlaying = false
)

val PreviewTrackUi3 = MediaItemUi(
    mediaItem = PreviewTrack3,
    isPlaying = false
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

val PreviewTracksUi1 = listOf(
    PreviewTrackUi1,
    PreviewTrackUi2,
    PreviewTrackUi3,
)

val PreviewAlbum1 = PreviewSimpleAlbum1.toAlbum(
    tracks = PreviewSimpleTracks1
).copy(id = AlbumId("1"))

val PreviewAlbum2 = PreviewAlbum1.copy(id = AlbumId("2"))

val PreviewAlbum3 = PreviewAlbum1.copy(id = AlbumId("3"))

val PreviewAlbums1 = listOf(
    PreviewAlbum1,
    PreviewAlbum2,
    PreviewAlbum3,
)

val PreviewAlbumUi1 = MediaItemUi(
    mediaItem = PreviewAlbum1,
    isPlaying = false,
)

val PreviewAlbumUi2 = MediaItemUi(
    mediaItem = PreviewAlbum2,
    isPlaying = false,
)

val PreviewAlbumUi3 = MediaItemUi(
    mediaItem = PreviewAlbum3,
    isPlaying = false,
)

val PreviewAlbumsUi1 = listOf(
    PreviewAlbumUi1,
    PreviewAlbumUi2,
    PreviewAlbumUi3,
)
