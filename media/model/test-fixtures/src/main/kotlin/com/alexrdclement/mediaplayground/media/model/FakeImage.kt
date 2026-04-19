package com.alexrdclement.mediaplayground.media.model

val FakeImage1 = Image(
    id = ImageId("image-1"),
    uri = MediaAssetUri.Shared("image-1.png"),
    originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/image-1"),
    createdAt = kotlin.time.Instant.DISTANT_PAST,
    mimeType = "image/png",
    extension = "png",
    widthPx = 1024,
    heightPx = 768,
)

val FakeImage2 = Image(
    id = ImageId("image-2"),
    uri = MediaAssetUri.Shared("image-2.png"),
    originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/image-2"),
    createdAt = kotlin.time.Instant.DISTANT_PAST,
    mimeType = "image/png",
    extension = "png",
    widthPx = 1024,
    heightPx = 768,
)
