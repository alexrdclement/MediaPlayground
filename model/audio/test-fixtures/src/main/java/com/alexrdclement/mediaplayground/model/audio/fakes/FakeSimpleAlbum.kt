package com.alexrdclement.mediaplayground.model.audio.fakes

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.Source
import kotlinx.collections.immutable.persistentListOf

val FakeSimpleAlbum1 = SimpleAlbum(
    id = AlbumId("1"),
    name = "Major Arcana",
    artists = persistentListOf(FakeSimpleArtist1),
    images = persistentListOf(FakeImage1),
    source = FakeSource1,
)

val FakeSimpleAlbum2 = SimpleAlbum(
    id = AlbumId("2"),
    name = "TANGK",
    artists = persistentListOf(FakeSimpleArtist2),
    images = persistentListOf(FakeImage2),
    source = FakeSource1,
)

val FakeLocalSimpleAlbum1 = FakeSimpleAlbum1.copy(
    source = Source.Local,
)

val FakeLocalSimpleAlbum2 = FakeSimpleAlbum2.copy(
    source = Source.Local,
)
