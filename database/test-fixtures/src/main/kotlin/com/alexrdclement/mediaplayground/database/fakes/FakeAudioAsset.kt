package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.AudioAsset

val FakeAudioAsset1 = AudioAsset(
    id = "audio-1",
    durationUs = 217_000_000L,
    sampleRate = 44100,
    channelCount = 2,
    bitRate = 320000,
    bitDepth = 16,
)

val FakeAudioAsset2 = FakeAudioAsset1.copy(
    id = "audio-2",
    durationUs = 166_000_000L,
)

val FakeAudioAsset3 = FakeAudioAsset1.copy(
    id = "audio-3",
    durationUs = 155_000_000L,
)
