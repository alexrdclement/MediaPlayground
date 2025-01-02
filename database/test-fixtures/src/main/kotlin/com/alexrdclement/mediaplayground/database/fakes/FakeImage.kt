package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Image

val FakeImage1 = Image(
    id = "1",
    uri = "content://media/external/images/media/1",
    albumId = "1",
)

val FakeImage2 = FakeImage1.copy(
    id = "2",
    uri = "content://media/external/images/media/2",
    albumId = "2",
)
