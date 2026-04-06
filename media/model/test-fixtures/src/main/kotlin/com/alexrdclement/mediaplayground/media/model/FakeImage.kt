package com.alexrdclement.mediaplayground.media.model

// URI base path should match that from FakePathProvider's getImagesDir()

val FakeImage1 = Image(
    id = ImageId("image-1"),
    mimeType = "image/png",
    extension = "png",
    uri = "file:/images/image-1.png",
)

val FakeImage2 = Image(
    id = ImageId("image-2"),
    mimeType = "image/png",
    extension = "png",
    uri = "file:/images/image-2.png",
)
