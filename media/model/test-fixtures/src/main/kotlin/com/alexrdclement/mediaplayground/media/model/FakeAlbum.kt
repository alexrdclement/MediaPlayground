package com.alexrdclement.mediaplayground.media.model

import com.alexrdclement.mediaplayground.media.model.mapper.toAlbum
import kotlinx.collections.immutable.persistentListOf

val FakeAlbum1 = FakeSimpleAlbum1.toAlbum(
    tracks = persistentListOf(FakeTrack1, FakeTrack2, FakeTrack3),
).copy(id = AudioAlbumId("1"))

val FakeAlbum2 = FakeAlbum1.copy(id = AudioAlbumId("2"))

val FakeAlbum3 = FakeAlbum1.copy(id = AudioAlbumId("3"))

val FakeAlbums1 = listOf(
    FakeAlbum1,
    FakeAlbum2,
    FakeAlbum3,
)
