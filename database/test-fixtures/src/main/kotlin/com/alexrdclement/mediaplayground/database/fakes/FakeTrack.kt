package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Track
import kotlin.time.Instant

val FakeTrack1 = Track(
    id = "1",
    title = "Track 1",
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
    notes = null,
)

val FakeTrack2 = FakeTrack1.copy(
    id = "2",
    title = "Track 2",
)

val FakeTrack3 = FakeTrack1.copy(
    id = "3",
    title = "Track 3",
)
