package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.SimpleAlbum

val FakeSimpleAlbum1 = SimpleAlbum(
    album = FakeAlbum1,
    mediaCollection = FakeMediaCollection1,
    artists = listOf(FakeArtist1),
    images = listOf(FakeCompleteImageAsset1),
)

val FakeSimpleAlbum2 = FakeSimpleAlbum1.copy(
    album = FakeAlbum2,
    mediaCollection = FakeMediaCollection2,
    artists = listOf(FakeArtist2),
    images = listOf(FakeCompleteImageAsset2),
)
