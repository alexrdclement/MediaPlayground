package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.AudioFile
import com.alexrdclement.mediaplayground.database.model.Source

val FakeAudioFile1 = AudioFile(
    id = "audio-1",
    fileName = "1.mp3",
    source = Source.Local,
    durationUs = 217_000_000L,
    sampleRate = 44100,
    channelCount = 2,
    bitRate = 320000,
    bitDepth = 16,
    mimeType = "audio/mpeg",
    extension = "mp3",
)

val FakeAudioFile2 = FakeAudioFile1.copy(
    id = "audio-2",
    fileName = "2.mp3",
    durationUs = 166_000_000L,
)

val FakeAudioFile3 = FakeAudioFile1.copy(
    id = "audio-3",
    fileName = "3.mp3",
    durationUs = 155_000_000L,
)
