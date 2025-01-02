package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Image

val FakeImage1 = Image(
    id = "1",
    fileName = "1.jpg",
    albumId = "1",
)

val FakeImage2 = FakeImage1.copy(
    id = "2",
    fileName = "2.jpg",
    albumId = "2",
)

val FakeImage3 = FakeImage1.copy(
    id = "3",
    fileName = "3.jpg",
    albumId = "1",
)
