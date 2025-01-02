package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.SimpleAlbum

val FakeSimpleAlbum1 = SimpleAlbum(
    album = FakeAlbum1,
    artists = listOf(FakeArtist1),
    images = listOf(FakeImage1),
)

val FakeSimpleAlbum2 = FakeSimpleAlbum1.copy(
    album = FakeAlbum2,
    artists = listOf(FakeArtist2),
    images = listOf(FakeImage2),
)
