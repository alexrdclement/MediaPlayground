package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

val FakeSimpleTrack1 = SimpleAudioTrack(
    id = TrackId("1"),
    name = "Pioneer Spine",
    artists = persistentListOf(FakeArtist1),
    trackNumber = 1,
    clips = persistentSetOf(FakeTrackClip1),
)

val FakeSimpleTrack2 = SimpleAudioTrack(
    id = TrackId("2"),
    name = "Tiger Tank",
    artists = persistentListOf(FakeArtist1),
    trackNumber = 2,
    clips = persistentSetOf(FakeTrackClip2),
)

val FakeSimpleTrack3 = SimpleAudioTrack(
    id = TrackId("3"),
    name = "Hitch",
    artists = persistentListOf(FakeArtist1),
    trackNumber = 3,
    clips = persistentSetOf(FakeTrackClip3),
)

val FakeSimpleTracks1 = persistentListOf(
    FakeSimpleTrack1,
    FakeSimpleTrack2,
    FakeSimpleTrack3,
)
