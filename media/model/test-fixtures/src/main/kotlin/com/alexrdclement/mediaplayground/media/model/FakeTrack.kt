package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.persistentSetOf

val FakeTrack1 = AudioTrack(
    id = FakeSimpleTrack1.id,
    title = FakeSimpleTrack1.name,
    trackNumber = FakeSimpleTrack1.trackNumber,
    artists = FakeSimpleTrack1.artists,
    clips = FakeSimpleTrack1.clips,
    simpleAlbum = FakeSimpleAlbum1,
    notes = null,
)

val FakeTrack2 = AudioTrack(
    id = FakeSimpleTrack2.id,
    title = FakeSimpleTrack2.name,
    trackNumber = FakeSimpleTrack2.trackNumber,
    artists = FakeSimpleTrack2.artists,
    clips = FakeSimpleTrack2.clips,
    simpleAlbum = FakeSimpleAlbum1,
    notes = null,
)

val FakeTrack3 = AudioTrack(
    id = FakeSimpleTrack3.id,
    title = FakeSimpleTrack3.name,
    trackNumber = FakeSimpleTrack3.trackNumber,
    artists = FakeSimpleTrack3.artists,
    clips = FakeSimpleTrack3.clips,
    simpleAlbum = FakeSimpleAlbum1,
    notes = null,
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
