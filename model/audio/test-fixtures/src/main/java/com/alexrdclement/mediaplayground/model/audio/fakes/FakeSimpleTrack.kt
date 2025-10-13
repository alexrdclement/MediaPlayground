package com.alexrdclement.mediaplayground.model.audio.fakes

import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlin.time.Duration.Companion.seconds

val FakeSimpleTrack1 = SimpleTrack(
    id = TrackId("1"),
    name = "Pioneer Spine",
    artists = listOf(FakeSimpleArtist1),
    duration = 217.seconds,
    trackNumber = 1,
    uri = null,
    source = FakeSource1,
)

val FakeSimpleTrack2 = SimpleTrack(
    id = TrackId("2"),
    name = "Tiger Tank",
    artists = listOf(FakeSimpleArtist1),
    duration = 166.seconds,
    trackNumber = 2,
    uri = null,
    source = FakeSource2,
)

val FakeSimpleTrack3 = SimpleTrack(
    id = TrackId("3"),
    name = "Hitch",
    artists = listOf(FakeSimpleArtist1),
    duration = 155.seconds,
    trackNumber = 3,
    uri = null,
    source = FakeSource1,
)

val FakeSimpleTracks1 = listOf(
    FakeSimpleTrack1,
    FakeSimpleTrack2,
    FakeSimpleTrack3,
)
