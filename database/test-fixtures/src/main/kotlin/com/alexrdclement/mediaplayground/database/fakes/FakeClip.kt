package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.AudioClip
import com.alexrdclement.mediaplayground.database.model.Clip
import kotlin.time.Instant

val FakeClip1 = Clip(
    id = "clip-1",
    title = "Track 1",
    assetId = FakeAudioAsset1.id,
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
)

val FakeClip2 = FakeClip1.copy(
    id = "clip-2",
    title = "Track 2",
    assetId = FakeAudioAsset2.id,
)

val FakeClip3 = FakeClip1.copy(
    id = "clip-3",
    title = "Track 3",
    assetId = FakeAudioAsset3.id,
)

val FakeAudioClip1 = AudioClip(
    id = FakeClip1.id,
    startSampleInAsset = 0L,
    durationSamples = 9569700L,
)

val FakeAudioClip2 = AudioClip(
    id = FakeClip2.id,
    startSampleInAsset = 0L,
    durationSamples = 7320600L,
)

val FakeAudioClip3 = AudioClip(
    id = FakeClip3.id,
    startSampleInAsset = 0L,
    durationSamples = 6835500L,
)
