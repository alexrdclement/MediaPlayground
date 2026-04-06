package com.alexrdclement.mediaplayground.ui.util

import com.alexrdclement.mediaplayground.media.model.FakeAlbum1
import com.alexrdclement.mediaplayground.media.model.FakeAlbum2
import com.alexrdclement.mediaplayground.media.model.FakeAlbum3
import com.alexrdclement.mediaplayground.media.model.FakeAlbums1
import com.alexrdclement.mediaplayground.media.model.FakeArtist1
import com.alexrdclement.mediaplayground.media.model.FakeSimpleAlbum1
import com.alexrdclement.mediaplayground.media.model.FakeSimpleTrack1
import com.alexrdclement.mediaplayground.media.model.FakeSimpleTrack2
import com.alexrdclement.mediaplayground.media.model.FakeSimpleTrack3
import com.alexrdclement.mediaplayground.media.model.FakeSimpleTracks1
import com.alexrdclement.mediaplayground.media.model.FakeTrack1
import com.alexrdclement.mediaplayground.media.model.FakeTrack2
import com.alexrdclement.mediaplayground.media.model.FakeTrack3
import com.alexrdclement.mediaplayground.media.model.FakeTracks1
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi


val PreviewArtist1 = FakeArtist1

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

val PreviewTrackUi1 = MediaItemUi.from(
    mediaItem = PreviewTrack1,
    loadedMediaItem = null,
    isPlaying = false
)

val PreviewTrackUi2 = MediaItemUi.from(
    mediaItem = PreviewTrack2,
    loadedMediaItem = null,
    isPlaying = false
)

val PreviewTrackUi3 = MediaItemUi.from(
    mediaItem = PreviewTrack3,
    loadedMediaItem = null,
    isPlaying = false
)

val PreviewTracks1 = FakeTracks1

val PreviewTracksUi1 = listOf(
    PreviewTrackUi1,
    PreviewTrackUi2,
    PreviewTrackUi3,
)

val PreviewAlbumUi1 = MediaItemUi.from(
    mediaItem = PreviewAlbum1,
    loadedMediaItem = null,
    isPlaying = false,
)

val PreviewAlbumUi2 = MediaItemUi.from(
    mediaItem = PreviewAlbum2,
    loadedMediaItem = null,
    isPlaying = false,
)

val PreviewAlbumUi3 = MediaItemUi.from(
    mediaItem = PreviewAlbum3,
    loadedMediaItem = null,
    isPlaying = false,
)

val PreviewAlbumsUi1 = listOf(
    PreviewAlbumUi1,
    PreviewAlbumUi2,
    PreviewAlbumUi3,
)

val PreviewImage1 = Image(
    id = ImageId("image-1"),
    mimeType = "image/png",
    extension = "png",
    uri = "file:/images/image-1.png",
)
