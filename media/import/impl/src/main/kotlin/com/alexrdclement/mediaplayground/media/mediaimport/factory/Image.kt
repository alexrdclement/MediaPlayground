package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.mapper.toImage
import kotlin.time.Clock
import kotlin.time.Instant

internal fun makeImage(
    id: ImageId,
    uri: MediaAssetUri,
    originUri: MediaAssetOriginUri,
    mediaMetadata: MediaMetadata.Image,
    createdAt: Instant = Clock.System.now(),
): Image {
    return mediaMetadata.toImage(
        id = id,
        uri = uri,
        originUri = originUri,
        createdAt = createdAt,
    )
}
