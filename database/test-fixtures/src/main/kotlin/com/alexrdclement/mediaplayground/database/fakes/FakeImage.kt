package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.Image

val FakeImage1 = Image(
    id = "1",
    fileName = "1.jpg",
    notes = null,
)

val FakeImage2 = FakeImage1.copy(
    id = "2",
    fileName = "2.jpg",
)

val FakeImage3 = FakeImage1.copy(
    id = "3",
    fileName = "3.jpg",
)

val FakeAlbumImage1 = AlbumImageCrossRef(albumId = "1", imageId = "1")
val FakeAlbumImage2 = AlbumImageCrossRef(albumId = "2", imageId = "2")
val FakeAlbumImage3 = AlbumImageCrossRef(albumId = "3", imageId = "3")
