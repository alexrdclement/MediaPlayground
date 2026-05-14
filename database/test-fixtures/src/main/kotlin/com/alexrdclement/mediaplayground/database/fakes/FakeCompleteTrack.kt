package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.AlbumTrackCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteAlbumRef
import com.alexrdclement.mediaplayground.database.model.CompleteAudioClip
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import com.alexrdclement.mediaplayground.database.model.CompleteTrackClip
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef
import kotlin.time.Instant

val FakeCompleteAudioClip1 = CompleteAudioClip(
    clip = FakeClip1,
    audioClip = FakeAudioClip1,
    audioAsset = FakeAudioAsset1,
    mediaAsset = FakeMediaAssetRecord1,
    artists = emptyList(),
    images = emptyList(),
)

val FakeCompleteAudioClip2 = CompleteAudioClip(
    clip = FakeClip2,
    audioClip = FakeAudioClip2,
    audioAsset = FakeAudioAsset2,
    mediaAsset = FakeMediaAssetRecord2,
    artists = emptyList(),
    images = emptyList(),
)

val FakeCompleteAudioClip3 = CompleteAudioClip(
    clip = FakeClip3,
    audioClip = FakeAudioClip3,
    audioAsset = FakeAudioAsset3,
    mediaAsset = FakeMediaAssetRecord3,
    artists = emptyList(),
    images = emptyList(),
)

val FakeCompleteTrackClip1 = CompleteTrackClip(
    trackClipCrossRef = TrackClipCrossRef(
        id = "track-clip-1",
        trackId = FakeTrack1.id,
        clipId = FakeClip1.id,
        startSampleInTrack = 0L,
        createdAt = Instant.fromEpochMilliseconds(0L),
        modifiedAt = Instant.fromEpochMilliseconds(0L),
    ),
    completeAudioClip = FakeCompleteAudioClip1,
)

val FakeCompleteTrackClip2 = CompleteTrackClip(
    trackClipCrossRef = TrackClipCrossRef(
        id = "track-clip-2",
        trackId = FakeTrack2.id,
        clipId = FakeClip2.id,
        startSampleInTrack = 0L,
        createdAt = Instant.fromEpochMilliseconds(0L),
        modifiedAt = Instant.fromEpochMilliseconds(0L),
    ),
    completeAudioClip = FakeCompleteAudioClip2,
)

val FakeCompleteTrackClip3 = CompleteTrackClip(
    trackClipCrossRef = TrackClipCrossRef(
        id = "track-clip-3",
        trackId = FakeTrack3.id,
        clipId = FakeClip3.id,
        startSampleInTrack = 0L,
        createdAt = Instant.fromEpochMilliseconds(0L),
        modifiedAt = Instant.fromEpochMilliseconds(0L),
    ),
    completeAudioClip = FakeCompleteAudioClip3,
)

val FakeAlbumTrackCrossRef1 = AlbumTrackCrossRef(
    albumId = FakeAlbum1.id,
    trackId = FakeTrack1.id,
    trackNumber = 1,
)

val FakeAlbumTrackCrossRef2 = AlbumTrackCrossRef(
    albumId = FakeAlbum1.id,
    trackId = FakeTrack2.id,
    trackNumber = 2,
)

val FakeAlbumTrackCrossRef3 = AlbumTrackCrossRef(
    albumId = FakeAlbum2.id,
    trackId = FakeTrack3.id,
    trackNumber = 1,
)

val FakeCompleteTrack1 = CompleteTrack(
    track = FakeTrack1,
    mediaCollection = FakeTrackMediaCollection1,
    albumRefs = listOf(
        CompleteAlbumRef(
            albumTrackCrossRef = FakeAlbumTrackCrossRef1,
            simpleAlbum = FakeSimpleAlbum1,
        )
    ),
    clips = listOf(FakeCompleteTrackClip1),
)

val FakeCompleteTrack2 = CompleteTrack(
    track = FakeTrack2,
    mediaCollection = FakeTrackMediaCollection2,
    albumRefs = listOf(
        CompleteAlbumRef(
            albumTrackCrossRef = FakeAlbumTrackCrossRef2,
            simpleAlbum = FakeSimpleAlbum1,
        )
    ),
    clips = listOf(FakeCompleteTrackClip2),
)

val FakeCompleteTrack3 = CompleteTrack(
    track = FakeTrack3,
    mediaCollection = FakeTrackMediaCollection3,
    albumRefs = listOf(
        CompleteAlbumRef(
            albumTrackCrossRef = FakeAlbumTrackCrossRef3,
            simpleAlbum = FakeSimpleAlbum2,
        )
    ),
    clips = listOf(FakeCompleteTrackClip3),
)
