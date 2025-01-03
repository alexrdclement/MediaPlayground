package com.alexrdclement.mediaplayground.model.audio.fakes

import com.alexrdclement.mediaplayground.model.audio.mapper.toTrack

val FakeTrack1 = FakeSimpleTrack1.toTrack(
    simpleAlbum = FakeSimpleAlbum1,
)

val FakeTrack2 = FakeSimpleTrack2.toTrack(
    simpleAlbum = FakeSimpleAlbum1,
)

val FakeTrack3 = FakeSimpleTrack3.toTrack(
    simpleAlbum = FakeSimpleAlbum1,
)

val FakeTracks1 = listOf(
    FakeTrack1,
    FakeTrack2,
    FakeTrack3,
)
