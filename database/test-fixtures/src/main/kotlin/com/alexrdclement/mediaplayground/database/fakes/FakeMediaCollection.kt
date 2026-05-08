package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.MediaCollection
import com.alexrdclement.mediaplayground.database.model.MediaCollectionType
import kotlin.time.Instant

val FakeMediaCollection1 = MediaCollection(
    id = "1",
    title = "Album 1",
    mediaCollectionType = MediaCollectionType.ALBUM,
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
)

val FakeMediaCollection2 = FakeMediaCollection1.copy(
    id = "2",
    title = "Album 2",
)

val FakeTrackMediaCollection1 = MediaCollection(
    id = "track-1",
    title = "Track 1",
    mediaCollectionType = MediaCollectionType.TRACK,
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
)

val FakeTrackMediaCollection2 = FakeTrackMediaCollection1.copy(
    id = "track-2",
    title = "Track 2",
)

val FakeTrackMediaCollection3 = FakeTrackMediaCollection1.copy(
    id = "track-3",
    title = "Track 3",
)
