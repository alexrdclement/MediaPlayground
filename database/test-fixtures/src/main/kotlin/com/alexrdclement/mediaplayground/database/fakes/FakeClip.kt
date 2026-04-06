package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Clip

val FakeClip1 = Clip(
    id = "clip-1",
    title = "Track 1",
    assetId = FakeAudioFile1.id,
    startFrameInFile = 0L,
    endFrameInFile = 9569700L,
)

val FakeClip2 = FakeClip1.copy(
    id = "clip-2",
    title = "Track 2",
    assetId = FakeAudioFile2.id,
    endFrameInFile = 7320600L,
)

val FakeClip3 = FakeClip1.copy(
    id = "clip-3",
    title = "Track 3",
    assetId = FakeAudioFile3.id,
    endFrameInFile = 6835500L,
)
