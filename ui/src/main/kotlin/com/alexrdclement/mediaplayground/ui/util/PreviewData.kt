package com.alexrdclement.mediaplayground.ui.util

import com.alexrdclement.mediaplayground.model.audio.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeAlbum2
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeAlbum3
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeAlbums1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeSimpleAlbum1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeSimpleArtist1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeSimpleTrack1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeSimpleTrack2
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeSimpleTrack3
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeSimpleTracks1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeTrack1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeTrack2
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeTrack3
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeTracks1
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi


val PreviewSimpleArtist1 = FakeSimpleArtist1

val PreviewSimpleAlbum1 = FakeSimpleAlbum1

val PreviewSimpleTrack1 = FakeSimpleTrack1

val PreviewSimpleTrack2 = FakeSimpleTrack2

val PreviewSimpleTrack3 = FakeSimpleTrack3

val PreviewTrack1 = FakeTrack1

val PreviewTrack2 = FakeTrack2

val PreviewTrack3 = FakeTrack3

val PreviewSimpleTracks1 = FakeSimpleTracks1

val PreviewAlbum1 = FakeAlbum1

val PreviewAlbum2 = FakeAlbum2

val PreviewAlbum3 = FakeAlbum3

val PreviewAlbums1 = FakeAlbums1

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

val PreviewTracks1 = FakeTracks1

val PreviewTracksUi1 = listOf(
    PreviewTrackUi1,
    PreviewTrackUi2,
    PreviewTrackUi3,
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
