package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Track
import kotlinx.datetime.Instant

val FakeTrack1 = Track(
    id = "1",
    title = "Track 1",
    albumId = "1",
    durationMs = 1000,
    trackNumber = 1,
    uri = "content://media/external/audio/media/1",
    modifiedDate = Instant.DISTANT_PAST,
)

val FakeTrack2 = FakeTrack1.copy(
    id = "2",
    title = "Track 2",
    trackNumber = 2,
    uri = "content://media/external/audio/media/2",
)

val FakeTrack3 = FakeTrack1.copy(
    id = "3",
    title = "Track 3",
    trackNumber = 3,
    uri = "content://media/external/audio/media/3",
)
