package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.MediaCollection
import com.alexrdclement.mediaplayground.database.model.MediaCollectionType

val FakeMediaCollection1 = MediaCollection(
    id = "1",
    mediaCollectionType = MediaCollectionType.ALBUM,
)

val FakeMediaCollection2 = FakeMediaCollection1.copy(id = "2")

val FakeTrackMediaCollection1 = MediaCollection(
    id = "track-1",
    mediaCollectionType = MediaCollectionType.TRACK,
)

val FakeTrackMediaCollection2 = FakeTrackMediaCollection1.copy(id = "track-2")
val FakeTrackMediaCollection3 = FakeTrackMediaCollection1.copy(id = "track-3")
