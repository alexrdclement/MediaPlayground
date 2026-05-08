package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Track
import kotlin.time.Instant

val FakeTrack1 = Track(
    id = FakeTrackMediaCollection1.id,
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
    notes = null,
)

val FakeTrack2 = FakeTrack1.copy(
    id = FakeTrackMediaCollection2.id,
)

val FakeTrack3 = FakeTrack1.copy(
    id = FakeTrackMediaCollection3.id,
)
