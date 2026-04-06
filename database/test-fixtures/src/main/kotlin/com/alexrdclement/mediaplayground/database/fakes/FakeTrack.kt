package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Track
import kotlin.time.Instant

val FakeTrack1 = Track(
    id = "1",
    title = "Track 1",
    albumId = "1",
    trackNumber = 1,
    modifiedDate = Instant.DISTANT_PAST,
    notes = null,
)

val FakeTrack2 = FakeTrack1.copy(
    id = "2",
    title = "Track 2",
    trackNumber = 2,
)

val FakeTrack3 = FakeTrack1.copy(
    id = "3",
    title = "Track 3",
    trackNumber = 3,
)
