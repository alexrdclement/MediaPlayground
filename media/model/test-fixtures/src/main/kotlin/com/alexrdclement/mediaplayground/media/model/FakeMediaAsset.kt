package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.persistentListOf

val FakeMediaAsset1 = MediaAsset(
    id = MediaAssetId("audio-1"),
    uri = null,
    source = Source.Local,
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

val FakeMediaAsset2 = FakeMediaAsset1.copy(
    id = MediaAssetId("audio-2"),
    metadata = (FakeMediaAsset1.metadata as MediaMetadata.Audio).copy(durationUs = 166_000_000L),
)

val FakeMediaAsset3 = FakeMediaAsset1.copy(
    id = MediaAssetId("audio-3"),
    metadata = (FakeMediaAsset1.metadata as MediaMetadata.Audio).copy(durationUs = 155_000_000L),
)

val FakeLocalMediaAsset1 = FakeMediaAsset1.copy(uri = "/local/1.mp3")
val FakeLocalMediaAsset2 = FakeMediaAsset2.copy(uri = "/local/2.mp3")
val FakeLocalMediaAsset3 = FakeMediaAsset3.copy(uri = "/local/3.mp3")
