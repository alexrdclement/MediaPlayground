package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.persistentListOf

val FakeSimpleAlbum1 = SimpleAlbum(
    id = AudioAlbumId("1"),
    name = "Major Arcana",
    artists = persistentListOf(FakeArtist1),
    images = persistentListOf(FakeImage1),
)

val FakeSimpleAlbum2 = SimpleAlbum(
    id = AudioAlbumId("2"),
    name = "TANGK",
    artists = persistentListOf(FakeArtist2),
    images = persistentListOf(FakeImage2),
)

val FakeLocalSimpleAlbum1 = FakeSimpleAlbum1

val FakeLocalSimpleAlbum2 = FakeSimpleAlbum2
