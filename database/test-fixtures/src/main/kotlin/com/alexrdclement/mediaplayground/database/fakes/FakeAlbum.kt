package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Album
import kotlin.time.Instant

val FakeAlbum1 = Album(
    id = "1",
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
    notes = null,
)

val FakeAlbum2 = FakeAlbum1.copy(
    id = "2",
)
