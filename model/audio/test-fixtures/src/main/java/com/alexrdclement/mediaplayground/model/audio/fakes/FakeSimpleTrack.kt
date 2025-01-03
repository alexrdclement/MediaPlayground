package com.alexrdclement.mediaplayground.model.audio.fakes

import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.TrackId

val FakeSimpleTrack1 = SimpleTrack(
    id = TrackId("1"),
    name = "Pioneer Spine",
    artists = listOf(FakeSimpleArtist1),
    durationMs = 217000,
    trackNumber = 1,
    uri = null,
)

val FakeSimpleTrack2 = SimpleTrack(
    id = TrackId("2"),
    name = "Tiger Tank",
    artists = listOf(FakeSimpleArtist1),
    durationMs = 166000,
    trackNumber = 2,
    uri = null,
)

val FakeSimpleTrack3 = SimpleTrack(
    id = TrackId("3"),
    name = "Hitch",
    artists = listOf(FakeSimpleArtist1),
    durationMs = 155000,
    trackNumber = 3,
    uri = null,
)

val FakeSimpleTracks1 = listOf(
    FakeSimpleTrack1,
    FakeSimpleTrack2,
    FakeSimpleTrack3,
)
