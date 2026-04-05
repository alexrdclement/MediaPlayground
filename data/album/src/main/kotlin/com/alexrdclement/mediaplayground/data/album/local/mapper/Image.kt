package com.alexrdclement.mediaplayground.data.album.local.mapper

import com.alexrdclement.mediaplayground.data.disk.mapper.uriFromFileName
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.ImageMetadata
import kotlinx.io.files.Path
import com.alexrdclement.mediaplayground.database.model.Image as ImageEntity

fun ImageEntity.toImage(imagesDir: Path): Image {
    val uri = uriFromFileName(imagesDir, fileName)
    require(uri != null) { "Image fileName must be a file name" }
    val metadata = ImageMetadata(
        widthPx = widthPx,
        heightPx = heightPx,
        dateTimeOriginal = dateTimeOriginal,
        gpsLatitude = gpsLatitude,
        gpsLongitude = gpsLongitude,
        cameraMake = cameraMake,
        cameraModel = cameraModel,
    ).takeUnless { it == ImageMetadata() }
    return Image(
        id = ImageId(id),
        uri = uri,
        metadata = metadata,
    )
}
