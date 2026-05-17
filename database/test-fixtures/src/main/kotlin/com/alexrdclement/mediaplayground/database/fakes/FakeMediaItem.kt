package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.MediaItem
import com.alexrdclement.mediaplayground.database.model.MediaItemType
import kotlin.time.Instant

val FakeAlbumMediaItem1 = MediaItem(
    id = "1",
    itemType = MediaItemType.COLLECTION,
    title = "Album 1",
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
)

val FakeAlbumMediaItem2 = FakeAlbumMediaItem1.copy(
    id = "2",
    title = "Album 2",
)

val FakeTrackMediaItem1 = MediaItem(
    id = "track-1",
    itemType = MediaItemType.COLLECTION,
    title = "Track 1",
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
)

val FakeTrackMediaItem2 = FakeTrackMediaItem1.copy(id = "track-2", title = "Track 2")
val FakeTrackMediaItem3 = FakeTrackMediaItem1.copy(id = "track-3", title = "Track 3")

val FakeClipMediaItem1 = MediaItem(
    id = "clip-1",
    itemType = MediaItemType.CLIP,
    title = "Track 1",
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
)

val FakeClipMediaItem2 = FakeClipMediaItem1.copy(id = "clip-2", title = "Track 2")
val FakeClipMediaItem3 = FakeClipMediaItem1.copy(id = "clip-3", title = "Track 3")

val FakeAudioAssetMediaItem1 = MediaItem(
    id = "audio-1",
    itemType = MediaItemType.ASSET,
    title = "1.mp3",
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
)

val FakeAudioAssetMediaItem2 = FakeAudioAssetMediaItem1.copy(id = "audio-2", title = "2.mp3")
val FakeAudioAssetMediaItem3 = FakeAudioAssetMediaItem1.copy(id = "audio-3", title = "3.mp3")

val FakeImageMediaItem1 = MediaItem(
    id = "image-1",
    itemType = MediaItemType.ASSET,
    title = "1.jpg",
    createdAt = Instant.fromEpochMilliseconds(0),
    modifiedAt = Instant.fromEpochMilliseconds(0),
)

val FakeImageMediaItem2 = FakeImageMediaItem1.copy(id = "image-2", title = "2.jpg")
val FakeImageMediaItem3 = FakeImageMediaItem1.copy(id = "image-3", title = "3.jpg")
