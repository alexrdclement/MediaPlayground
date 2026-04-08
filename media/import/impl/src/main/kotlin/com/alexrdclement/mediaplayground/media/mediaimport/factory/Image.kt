package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toImage
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import kotlinx.io.files.Path

internal fun makeImage(
    id: ImageId,
    mediaMetadata: MediaMetadata.Image,
    filePath: Path,
): Image {
    return mediaMetadata.toImage(
        id = id,
        uri = filePath,
    )
}
