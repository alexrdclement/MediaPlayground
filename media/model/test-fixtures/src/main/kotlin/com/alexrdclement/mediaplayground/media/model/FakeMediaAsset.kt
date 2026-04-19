package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Instant

val FakeAudioAsset1 = AudioAsset(
    id = AudioAssetId("audio-1"),
    uri = MediaAssetUri.Shared("audio-1.mp3"),
    originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/audio-1"),
    createdAt = Instant.DISTANT_PAST,
    artists = persistentListOf(FakeArtist1),
    images = persistentListOf(FakeImage1),
    metadata = MediaMetadata.Audio(
        title = null,
        durationUs = 217_000_000L,
        sampleRate = 44100,
        channelCount = 2,
        bitRate = 320000,
        bitDepth = 16,
        trackNumber = null,
        artistName = null,
        albumTitle = null,
        embeddedPicture = null,
        mimeType = "audio/mpeg",
        extension = "mp3",
    ),
)

val FakeAudioAsset2 = FakeAudioAsset1.copy(
    id = AudioAssetId("audio-2"),
    uri = MediaAssetUri.Shared("audio-2.mp3"),
    metadata = FakeAudioAsset1.metadata.copy(durationUs = 166_000_000L),
)

val FakeAudioAsset3 = FakeAudioAsset1.copy(
    id = AudioAssetId("audio-3"),
    uri = MediaAssetUri.Shared("audio-3.mp3"),
    metadata = FakeAudioAsset1.metadata.copy(durationUs = 155_000_000L),
)

val FakeLocalAudioAsset1 = FakeAudioAsset1.copy(uri = MediaAssetUri.Shared("1.mp3"))
val FakeLocalAudioAsset2 = FakeAudioAsset2.copy(uri = MediaAssetUri.Shared("2.mp3"))
val FakeLocalAudioAsset3 = FakeAudioAsset3.copy(uri = MediaAssetUri.Shared("3.mp3"))

// Legacy aliases — prefer FakeAudioAsset* in new code
val FakeMediaAsset1 = FakeAudioAsset1
val FakeMediaAsset2 = FakeAudioAsset2
val FakeMediaAsset3 = FakeAudioAsset3
val FakeLocalMediaAsset1 = FakeLocalAudioAsset1
val FakeLocalMediaAsset2 = FakeLocalAudioAsset2
val FakeLocalMediaAsset3 = FakeLocalAudioAsset3
