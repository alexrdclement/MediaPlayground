package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Album

val FakeAlbum1 = Album(
    id = "1",
    notes = null,
)

val FakeAlbum2 = FakeAlbum1.copy(
    id = "2",
)
