package com.alexrdclement.mediaplayground.media.model.mapper

import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import kotlin.time.Instant

fun MediaMetadata.Image.toImage(
    id: ImageId,
    uri: MediaAssetUri,
    originUri: MediaAssetOriginUri,
    createdAt: Instant,
): Image = Image(
    id = id,
    uri = uri,
    originUri = originUri,
    createdAt = createdAt,
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
