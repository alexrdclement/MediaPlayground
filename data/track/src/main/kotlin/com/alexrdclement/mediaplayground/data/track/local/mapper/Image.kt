package com.alexrdclement.mediaplayground.data.track.local.mapper

import com.alexrdclement.mediaplayground.data.disk.mapper.fileNameFromUri
import com.alexrdclement.mediaplayground.data.disk.mapper.uriFromFileName
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import kotlinx.io.files.Path
import com.alexrdclement.mediaplayground.database.model.Image as ImageEntity

fun Image.toImageEntity(): ImageEntity {
    val fileName = requireNotNull(fileNameFromUri(uri)) { "Image uri must be a file uri" }
    return ImageEntity(
        id = id.value,
        fileName = fileName,
        widthPx = widthPx,
        heightPx = heightPx,
        dateTimeOriginal = dateTimeOriginal,
        gpsLatitude = gpsLatitude,
        gpsLongitude = gpsLongitude,
        cameraMake = cameraMake,
        cameraModel = cameraModel,
        notes = notes,
    )
}

fun ImageEntity.toImage(imagesDir: Path): Image {
    val uri = requireNotNull(uriFromFileName(mediaItemDir = imagesDir, fileName = fileName)) {
        "Image fileName must be a file name"
    }
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
        notes = notes,
    )
}
