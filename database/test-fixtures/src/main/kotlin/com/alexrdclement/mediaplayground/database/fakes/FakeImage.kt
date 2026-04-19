package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteImageAsset
import com.alexrdclement.mediaplayground.database.model.ImageAsset

val FakeImageAsset1 = ImageAsset(
    id = "1",
    widthPx = 1024,
    heightPx = 768,
    dateTimeOriginal = null,
    gpsLatitude = null,
    gpsLongitude = null,
    cameraMake = null,
    cameraModel = null,
    notes = null,
)

val FakeImage2 = FakeImageAsset1.copy(id = "2")

val FakeImage3 = FakeImageAsset1.copy(id = "3")

val FakeCompleteImageAsset1 = CompleteImageAsset(
    imageAsset = FakeImageAsset1,
    mediaAsset = FakeImageAssetRecord1,
)

val FakeCompleteImageAsset2 = CompleteImageAsset(
    imageAsset = FakeImage2,
    mediaAsset = FakeImageAssetRecord2,
)

val FakeCompleteImageAsset3 = CompleteImageAsset(
    imageAsset = FakeImage3,
    mediaAsset = FakeImageAssetRecord3,
)

val FakeAlbumImage1 = AlbumImageCrossRef(albumId = "1", imageId = "1")
val FakeAlbumImage2 = AlbumImageCrossRef(albumId = "2", imageId = "2")
val FakeAlbumImage3 = AlbumImageCrossRef(albumId = "3", imageId = "3")
