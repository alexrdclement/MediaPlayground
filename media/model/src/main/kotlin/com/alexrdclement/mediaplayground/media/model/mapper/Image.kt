package com.alexrdclement.mediaplayground.media.model.mapper

import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata

fun MediaMetadata.Image.toImage(
    id: ImageId,
    uri: String,
): Image = Image(
    id = id,
    uri = uri,
    mimeType = mimeType,
    extension = extension,
    widthPx = widthPx,
    heightPx = heightPx,
    dateTimeOriginal = dateTimeOriginal,
    gpsLatitude = gpsLatitude,
    gpsLongitude = gpsLongitude,
    cameraMake = cameraMake,
    cameraModel = cameraModel,
)
