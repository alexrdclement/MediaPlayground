package com.alexrdclement.mediaplayground.model.audio.fakes

import com.alexrdclement.mediaplayground.model.audio.Source
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

val FakeLocalTrack1 = FakeTrack1.copy(
    simpleAlbum = FakeLocalSimpleAlbum1,
    source = Source.Local,
)

val FakeLocalTrack2 = FakeTrack2.copy(
    simpleAlbum = FakeLocalSimpleAlbum1,
    source = Source.Local,
)

val FakeTracks1 = listOf(
    FakeTrack1,
    FakeTrack2,
    FakeTrack3,
)
