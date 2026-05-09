package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Clip
import kotlin.time.Instant

val FakeClip1 = Clip(
    id = "clip-1",
    title = "Track 1",
    assetId = FakeAudioAsset1.id,
    startSampleInAsset = 0L,
    durationSamples = 9569700L,
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
)

val FakeClip2 = FakeClip1.copy(
    id = "clip-2",
    title = "Track 2",
    assetId = FakeAudioAsset2.id,
    durationSamples = 7320600L,
)

val FakeClip3 = FakeClip1.copy(
    id = "clip-3",
    title = "Track 3",
    assetId = FakeAudioAsset3.id,
    durationSamples = 6835500L,
)
