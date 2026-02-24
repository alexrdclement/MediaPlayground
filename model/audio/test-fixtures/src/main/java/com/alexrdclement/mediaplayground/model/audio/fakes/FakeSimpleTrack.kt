package com.alexrdclement.mediaplayground.model.audio.fakes

import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration.Companion.seconds

val FakeSimpleTrack1 = SimpleTrack(
    id = TrackId("1"),
    name = "Pioneer Spine",
    artists = persistentListOf(FakeSimpleArtist1),
    duration = 217.seconds,
    trackNumber = 1,
    uri = null,
    source = FakeSource1,
)

val FakeSimpleTrack2 = SimpleTrack(
    id = TrackId("2"),
    name = "Tiger Tank",
    artists = persistentListOf(FakeSimpleArtist1),
    duration = 166.seconds,
    trackNumber = 2,
    uri = null,
    source = FakeSource1,
)

val FakeSimpleTrack3 = SimpleTrack(
    id = TrackId("3"),
    name = "Hitch",
    artists = persistentListOf(FakeSimpleArtist1),
    duration = 155.seconds,
    trackNumber = 3,
    uri = null,
    source = FakeSource1,
)

val FakeSimpleTracks1 = persistentListOf(
    FakeSimpleTrack1,
    FakeSimpleTrack2,
    FakeSimpleTrack3,
)
