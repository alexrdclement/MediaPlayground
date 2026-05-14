package com.alexrdclement.mediaplayground.media.model

import kotlin.time.Instant

val FakeClip1 = Clip(
    id = ClipId("clip-1"),
    title = "Pioneer Spine",
    mediaAsset = FakeMediaAsset1,
    assetOffset = TimeUnit.Samples(0L, 44100),
    duration = TimeUnit.Samples(9569700L, 44100),
    createdAt = Instant.DISTANT_PAST,
    modifiedAt = Instant.DISTANT_PAST,
)

val FakeClip2 = Clip(
    id = ClipId("clip-2"),
    title = "Tiger Tank",
    mediaAsset = FakeMediaAsset2,
    assetOffset = TimeUnit.Samples(0L, 44100),
    duration = TimeUnit.Samples(7320600L, 44100),
    createdAt = Instant.DISTANT_PAST,
    modifiedAt = Instant.DISTANT_PAST,
)

val FakeClip3 = Clip(
    id = ClipId("clip-3"),
    title = "Hitch",
    mediaAsset = FakeMediaAsset3,
    assetOffset = TimeUnit.Samples(0L, 44100),
    duration = TimeUnit.Samples(6835500L, 44100),
    createdAt = Instant.DISTANT_PAST,
    modifiedAt = Instant.DISTANT_PAST,
)

val FakeLocalClip1 = FakeClip1.copy(mediaAsset = FakeLocalMediaAsset1)
val FakeLocalClip2 = FakeClip2.copy(mediaAsset = FakeLocalMediaAsset2)
val FakeLocalClip3 = FakeClip3.copy(mediaAsset = FakeLocalMediaAsset3)

val FakeTrackClip1 = TrackClip(id = TrackClipId("track-clip-1"), clip = FakeClip1, trackOffset = TimeUnit.Samples(0L, 44100), createdAt = Instant.DISTANT_PAST, modifiedAt = Instant.DISTANT_PAST)
val FakeTrackClip2 = TrackClip(id = TrackClipId("track-clip-2"), clip = FakeClip2, trackOffset = TimeUnit.Samples(0L, 44100), createdAt = Instant.DISTANT_PAST, modifiedAt = Instant.DISTANT_PAST)
val FakeTrackClip3 = TrackClip(id = TrackClipId("track-clip-3"), clip = FakeClip3, trackOffset = TimeUnit.Samples(0L, 44100), createdAt = Instant.DISTANT_PAST, modifiedAt = Instant.DISTANT_PAST)
val FakeLocalTrackClip1 = TrackClip(id = TrackClipId("track-clip-local-1"), clip = FakeLocalClip1, trackOffset = TimeUnit.Samples(0L, 44100), createdAt = Instant.DISTANT_PAST, modifiedAt = Instant.DISTANT_PAST)
val FakeLocalTrackClip2 = TrackClip(id = TrackClipId("track-clip-local-2"), clip = FakeLocalClip2, trackOffset = TimeUnit.Samples(0L, 44100), createdAt = Instant.DISTANT_PAST, modifiedAt = Instant.DISTANT_PAST)
val FakeLocalTrackClip3 = TrackClip(id = TrackClipId("track-clip-local-3"), clip = FakeLocalClip3, trackOffset = TimeUnit.Samples(0L, 44100), createdAt = Instant.DISTANT_PAST, modifiedAt = Instant.DISTANT_PAST)
