package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.MediaAsset
import com.alexrdclement.mediaplayground.database.model.MediaAssetType
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import kotlin.time.Instant

val FakeMediaAssetRecord1 = MediaAsset(
    id = "audio-1",
    uri = MediaAssetUri.Album(albumId = AudioAlbumId("1"), fileName = "1.mp3"),
    mediaAssetType = MediaAssetType.AUDIO,
    fileName = "1.mp3",
    mimeType = "audio/mpeg",
    extension = "mp3",
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
    originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/audio-1"),
)

val FakeMediaAssetRecord2 = FakeMediaAssetRecord1.copy(
    id = "audio-2",
    uri = MediaAssetUri.Album(albumId = AudioAlbumId("1"), fileName = "2.mp3"),
    fileName = "2.mp3",
)

val FakeMediaAssetRecord3 = FakeMediaAssetRecord1.copy(
    id = "audio-3",
    uri = MediaAssetUri.Album(albumId = AudioAlbumId("1"), fileName = "3.mp3"),
    fileName = "3.mp3",
)

val FakeImageAssetRecord1 = MediaAsset(
    id = "1",
    uri = MediaAssetUri.Shared(fileName = "1.jpg"),
    mediaAssetType = MediaAssetType.IMAGE,
    fileName = "1.jpg",
    mimeType = "image/jpeg",
    extension = "jpg",
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
    originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/image-1"),
)

val FakeImageAssetRecord2 = FakeImageAssetRecord1.copy(
    id = "2",
    uri = MediaAssetUri.Shared(fileName = "2.jpg"),
    fileName = "2.jpg",
)

val FakeImageAssetRecord3 = FakeImageAssetRecord1.copy(
    id = "3",
    uri = MediaAssetUri.Shared(fileName = "3.jpg"),
    fileName = "3.jpg",
)
