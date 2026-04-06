package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.data.disk.mapper.fileNameFromUri
import com.alexrdclement.mediaplayground.data.disk.mapper.uriFromFileName
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import kotlinx.io.files.Path
import com.alexrdclement.mediaplayground.database.model.ImageFile as ImageEntity

fun Image.toImageEntity(): ImageEntity {
    val fileName = fileNameFromUri(uri)
    require(fileName != null) { "Image uri must be a file uri" }
    return ImageEntity(
        id = id.value,
        mimeType = mimeType,
        extension = extension,
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
    val uri = uriFromFileName(imagesDir, fileName)
    require(uri != null) { "Image fileName must be a file name" }
    return Image(
        id = ImageId(id),
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
        notes = notes,
    )
}
