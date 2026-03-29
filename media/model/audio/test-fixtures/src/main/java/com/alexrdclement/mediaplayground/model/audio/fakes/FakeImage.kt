package com.alexrdclement.mediaplayground.model.audio.fakes

import com.alexrdclement.mediaplayground.media.model.audio.Image
import com.alexrdclement.mediaplayground.media.model.audio.ImageId

// URI base path should match that from FakePathProvider's getImagesDir()

val FakeImage1 = Image(
    id = ImageId("image-1"),
    uri = "file:/images/image-1.png",
    heightPx = null,
    widthPx = null,
)

val FakeImage2 = Image(
    id = ImageId("image-2"),
    uri = "file:/images/image-2.png",
    heightPx = null,
    widthPx = null,
)
