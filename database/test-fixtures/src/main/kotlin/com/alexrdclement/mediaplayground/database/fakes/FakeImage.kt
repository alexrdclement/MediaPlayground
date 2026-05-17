package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteImageAsset
import com.alexrdclement.mediaplayground.database.model.ImageAsset

val FakeImageAsset1 = ImageAsset(
    id = "image-1",
    widthPx = 1024,
    heightPx = 768,
    dateTimeOriginal = null,
    gpsLatitude = null,
    gpsLongitude = null,
    cameraMake = null,
    cameraModel = null,
    notes = null,
)

val FakeImage2 = FakeImageAsset1.copy(id = "image-2")

val FakeImage3 = FakeImageAsset1.copy(id = "image-3")

val FakeCompleteImageAsset1 = CompleteImageAsset(
    imageAsset = FakeImageAsset1,
    mediaItem = FakeImageMediaItem1,
    mediaAsset = FakeImageAssetRecord1,
)

val FakeCompleteImageAsset2 = CompleteImageAsset(
    imageAsset = FakeImage2,
    mediaItem = FakeImageMediaItem2,
    mediaAsset = FakeImageAssetRecord2,
)

val FakeCompleteImageAsset3 = CompleteImageAsset(
    imageAsset = FakeImage3,
    mediaItem = FakeImageMediaItem3,
    mediaAsset = FakeImageAssetRecord3,
)

val FakeAlbumImage1 = AlbumImageCrossRef(albumId = "1", imageId = "image-1")
val FakeAlbumImage2 = AlbumImageCrossRef(albumId = "2", imageId = "image-2")
val FakeAlbumImage3 = AlbumImageCrossRef(albumId = "3", imageId = "image-3")
