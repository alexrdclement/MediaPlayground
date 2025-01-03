package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.CompleteTrack

val FakeCompleteTrack1 = CompleteTrack(
    track = FakeTrack1.copy(
        albumId = FakeAlbum1.id,
    ),
    album = FakeAlbum1,
    artists = listOf(FakeArtist1),
    images = listOf(FakeImage1),
)

val FakeCompleteTrack2 = FakeCompleteTrack1.copy(
    track = FakeTrack2.copy(
        albumId = FakeAlbum1.id,
    ),
)

val FakeCompleteTrack3 = FakeCompleteTrack1.copy(
    track = FakeTrack3.copy(
        albumId = FakeAlbum2.id,
    ),
    album = FakeAlbum2,
    artists = listOf(FakeArtist2),
    images = listOf(FakeImage2),
)
