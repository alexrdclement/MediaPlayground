package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.CompleteAlbum

val FakeCompleteAlbum1 = CompleteAlbum(
    simpleAlbum = FakeSimpleAlbum1,
    tracks = listOf(
        FakeTrack1.copy(albumId = FakeSimpleAlbum1.album.id),
        FakeTrack2.copy(albumId = FakeSimpleAlbum1.album.id),
    )
)

val FakeCompleteAlbum2 = FakeCompleteAlbum1.copy(
    simpleAlbum = FakeSimpleAlbum2,
    tracks = listOf(
        FakeTrack3.copy(albumId = FakeSimpleAlbum2.album.id),
    )
)
