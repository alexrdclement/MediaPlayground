package com.alexrdclement.mediaplayground.model.audio.fakes

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum

val FakeSimpleAlbum1 = SimpleAlbum(
    id = AlbumId("1"),
    name = "Major Arcana",
    artists = listOf(FakeSimpleArtist1),
    images = listOf(FakeImage1),
)

val FakeSimpleAlbum2 = SimpleAlbum(
    id = AlbumId("2"),
    name = "TANGK",
    artists = listOf(FakeSimpleArtist2),
    images = listOf(FakeImage2),
)
