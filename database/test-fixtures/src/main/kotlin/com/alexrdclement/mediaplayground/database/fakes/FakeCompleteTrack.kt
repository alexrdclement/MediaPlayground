package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.CompleteAudioClip
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import com.alexrdclement.mediaplayground.database.model.CompleteTrackClip
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef

val FakeCompleteAudioClip1 = CompleteAudioClip(
    clip = FakeClip1,
    audioFile = FakeAudioFile1,
)

val FakeCompleteAudioClip2 = CompleteAudioClip(
    clip = FakeClip2,
    audioFile = FakeAudioFile2,
)

val FakeCompleteAudioClip3 = CompleteAudioClip(
    clip = FakeClip3,
    audioFile = FakeAudioFile3,
)

val FakeCompleteTrackClip1 = CompleteTrackClip(
    trackClipCrossRef = TrackClipCrossRef(
        trackId = FakeTrack1.id,
        clipId = FakeClip1.id,
        startFrameInTrack = 0L,
    ),
    completeAudioClip = FakeCompleteAudioClip1,
)

val FakeCompleteTrackClip2 = CompleteTrackClip(
    trackClipCrossRef = TrackClipCrossRef(
        trackId = FakeTrack2.id,
        clipId = FakeClip2.id,
        startFrameInTrack = 0L,
    ),
    completeAudioClip = FakeCompleteAudioClip2,
)

val FakeCompleteTrackClip3 = CompleteTrackClip(
    trackClipCrossRef = TrackClipCrossRef(
        trackId = FakeTrack3.id,
        clipId = FakeClip3.id,
        startFrameInTrack = 0L,
    ),
    completeAudioClip = FakeCompleteAudioClip3,
)

val FakeCompleteTrack1 = CompleteTrack(
    track = FakeTrack1.copy(
        albumId = FakeAlbum1.id,
    ),
    album = FakeAlbum1,
    artists = listOf(FakeArtist1),
    images = listOf(FakeImageFile1),
    clips = listOf(FakeCompleteTrackClip1),
)

val FakeCompleteTrack2 = FakeCompleteTrack1.copy(
    track = FakeTrack2.copy(
        albumId = FakeAlbum1.id,
    ),
    clips = listOf(FakeCompleteTrackClip2),
)

val FakeCompleteTrack3 = FakeCompleteTrack1.copy(
    track = FakeTrack3.copy(
        albumId = FakeAlbum2.id,
    ),
    album = FakeAlbum2,
    artists = listOf(FakeArtist2),
    images = listOf(FakeImage2),
    clips = listOf(FakeCompleteTrackClip3),
)
