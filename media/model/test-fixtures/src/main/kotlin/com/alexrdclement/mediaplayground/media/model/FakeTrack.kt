package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.persistentSetOf
import kotlin.time.Instant

val FakeTrack1 = AudioTrack(
    id = FakeSimpleTrack1.id,
    title = FakeSimpleTrack1.name,
    clips = FakeSimpleTrack1.clips,
    notes = null,
    createdAt = Instant.DISTANT_PAST,
    modifiedAt = Instant.DISTANT_PAST,
)

val FakeTrack2 = AudioTrack(
    id = FakeSimpleTrack2.id,
    title = FakeSimpleTrack2.name,
    clips = FakeSimpleTrack2.clips,
    notes = null,
    createdAt = Instant.DISTANT_PAST,
    modifiedAt = Instant.DISTANT_PAST,
)

val FakeTrack3 = AudioTrack(
    id = FakeSimpleTrack3.id,
    title = FakeSimpleTrack3.name,
    clips = FakeSimpleTrack3.clips,
    notes = null,
    createdAt = Instant.DISTANT_PAST,
    modifiedAt = Instant.DISTANT_PAST,
)

val FakeLocalTrack1 = FakeTrack1.copy(
    clips = persistentSetOf(FakeLocalTrackClip1),
)

val FakeLocalTrack2 = FakeTrack2.copy(
    clips = persistentSetOf(FakeLocalTrackClip2),
)

val FakeLocalTrack3 = FakeTrack3.copy(
    clips = persistentSetOf(FakeLocalTrackClip3),
)

val FakeAlbumTrack1 = AlbumTrack(
    track = FakeTrack1,
    albumId = AudioAlbumId(FakeSimpleAlbum1.id.value),
    trackNumber = FakeSimpleTrack1.trackNumber,
)

val FakeAlbumTrack2 = AlbumTrack(
    track = FakeTrack2,
    albumId = AudioAlbumId(FakeSimpleAlbum1.id.value),
    trackNumber = FakeSimpleTrack2.trackNumber,
)

val FakeAlbumTrack3 = AlbumTrack(
    track = FakeTrack3,
    albumId = AudioAlbumId(FakeSimpleAlbum1.id.value),
    trackNumber = FakeSimpleTrack3.trackNumber,
)

val FakeLocalAlbumTrack1 = AlbumTrack(
    track = FakeLocalTrack1,
    albumId = AudioAlbumId(FakeLocalSimpleAlbum1.id.value),
    trackNumber = FakeSimpleTrack1.trackNumber,
)

val FakeLocalAlbumTrack2 = AlbumTrack(
    track = FakeLocalTrack2,
    albumId = AudioAlbumId(FakeLocalSimpleAlbum1.id.value),
    trackNumber = FakeSimpleTrack2.trackNumber,
)

val FakeLocalAlbumTrack3 = AlbumTrack(
    track = FakeLocalTrack3,
    albumId = AudioAlbumId(FakeLocalSimpleAlbum1.id.value),
    trackNumber = FakeSimpleTrack3.trackNumber,
)

val FakeLocalTracks1 = listOf(FakeLocalTrack1, FakeLocalTrack2, FakeLocalTrack3)

val FakeLocalAlbumTracks1 = listOf(FakeLocalAlbumTrack1, FakeLocalAlbumTrack2, FakeLocalAlbumTrack3)

val FakeTracks1 = listOf(
    FakeTrack1,
    FakeTrack2,
    FakeTrack3,
)
