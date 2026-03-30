package com.alexrdclement.mediaplayground.media.model.image.fakes

import com.alexrdclement.mediaplayground.media.model.image.Image
import com.alexrdclement.mediaplayground.media.model.image.ImageId

// URI base path should match that from FakePathProvider's getImagesDir()

val FakeImage1 = Image(
    id = ImageId("image-1"),
    uri = "file:/images/image-1.png",
)

val FakeImage2 = Image(
    id = ImageId("image-2"),
    uri = "file:/images/image-2.png",
)
