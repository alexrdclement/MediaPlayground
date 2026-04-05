package com.alexrdclement.mediaplayground.data.album.local.mapper

import com.alexrdclement.mediaplayground.data.disk.mapper.uriFromFileName
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import kotlinx.io.files.Path
import com.alexrdclement.mediaplayground.database.model.Image as ImageEntity

fun ImageEntity.toImage(imagesDir: Path): Image {
    val uri = uriFromFileName(imagesDir, fileName)
    require(uri != null) { "Image fileName must be a file name" }
    return Image(
        id = ImageId(id),
        uri = uri,
        widthPx = widthPx,
        heightPx = heightPx,
        dateTimeOriginal = dateTimeOriginal,
        gpsLatitude = gpsLatitude,
        gpsLongitude = gpsLongitude,
        cameraMake = cameraMake,
        cameraModel = cameraModel,
    )
}
