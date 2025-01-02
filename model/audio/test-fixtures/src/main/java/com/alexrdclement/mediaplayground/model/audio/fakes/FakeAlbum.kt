package com.alexrdclement.mediaplayground.model.audio.fakes

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.mapper.toAlbum

val FakeAlbum1 = FakeSimpleAlbum1.toAlbum(
    tracks = FakeSimpleTracks1
).copy(id = AlbumId("1"))

val FakeAlbum2 = FakeAlbum1.copy(id = AlbumId("2"))

val FakeAlbum3 = FakeAlbum1.copy(id = AlbumId("3"))

val FakeAlbums1 = listOf(
    FakeAlbum1,
    FakeAlbum2,
    FakeAlbum3,
)
