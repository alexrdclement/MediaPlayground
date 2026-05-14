package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Artist
import kotlin.time.Instant

val FakeArtist1 = Artist(
    id = "1",
    name = "Artist 1",
    notes = null,
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
)

val FakeArtist2 = FakeArtist1.copy(
    id = "2",
    name = "Artist 2",
)
