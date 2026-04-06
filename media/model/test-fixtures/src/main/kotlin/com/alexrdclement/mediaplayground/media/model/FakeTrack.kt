package com.alexrdclement.mediaplayground.media.model

import com.alexrdclement.mediaplayground.media.model.mapper.toTrack
import kotlinx.collections.immutable.persistentSetOf

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
    clips = persistentSetOf(FakeLocalTrackClip1),
)

val FakeLocalTrack2 = FakeTrack2.copy(
    simpleAlbum = FakeLocalSimpleAlbum1,
    clips = persistentSetOf(FakeLocalTrackClip2),
)

val FakeLocalTrack3 = FakeTrack3.copy(
    simpleAlbum = FakeLocalSimpleAlbum1,
    clips = persistentSetOf(FakeLocalTrackClip3),
)

val FakeLocalTracks1 = listOf(FakeLocalTrack1, FakeLocalTrack2, FakeLocalTrack3)

val FakeTracks1 = listOf(
    FakeTrack1,
    FakeTrack2,
    FakeTrack3,
)
