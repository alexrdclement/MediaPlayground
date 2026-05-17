package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Track

val FakeTrack1 = Track(
    id = FakeTrackMediaCollection1.id,
    notes = null,
)

val FakeTrack2 = FakeTrack1.copy(
    id = FakeTrackMediaCollection2.id,
)

val FakeTrack3 = FakeTrack1.copy(
    id = FakeTrackMediaCollection3.id,
)
