package com.alexrdclement.mediaplayground.media.model

val FakeClip1 = Clip(
    id = ClipId("clip-1"),
    title = "Pioneer Spine",
    mediaAsset = FakeMediaAsset1,
    startFrameInFile = 0L,
    endFrameInFile = 9569700L,
)

val FakeClip2 = Clip(
    id = ClipId("clip-2"),
    title = "Tiger Tank",
    mediaAsset = FakeMediaAsset2,
    startFrameInFile = 0L,
    endFrameInFile = 7320600L,
)

val FakeClip3 = Clip(
    id = ClipId("clip-3"),
    title = "Hitch",
    mediaAsset = FakeMediaAsset3,
    startFrameInFile = 0L,
    endFrameInFile = 6835500L,
)

val FakeLocalClip1 = FakeClip1.copy(mediaAsset = FakeLocalMediaAsset1)
val FakeLocalClip2 = FakeClip2.copy(mediaAsset = FakeLocalMediaAsset2)
val FakeLocalClip3 = FakeClip3.copy(mediaAsset = FakeLocalMediaAsset3)

val FakeTrackClip1 = TrackClip(clip = FakeClip1, startFrameInTrack = 0L)
val FakeTrackClip2 = TrackClip(clip = FakeClip2, startFrameInTrack = 0L)
val FakeTrackClip3 = TrackClip(clip = FakeClip3, startFrameInTrack = 0L)
val FakeLocalTrackClip1 = TrackClip(clip = FakeLocalClip1, startFrameInTrack = 0L)
val FakeLocalTrackClip2 = TrackClip(clip = FakeLocalClip2, startFrameInTrack = 0L)
val FakeLocalTrackClip3 = TrackClip(clip = FakeLocalClip3, startFrameInTrack = 0L)
