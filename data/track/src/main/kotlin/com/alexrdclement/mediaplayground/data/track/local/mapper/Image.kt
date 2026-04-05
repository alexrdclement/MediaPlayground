package com.alexrdclement.mediaplayground.data.track.local.mapper

import com.alexrdclement.mediaplayground.data.disk.mapper.fileNameFromUri
import com.alexrdclement.mediaplayground.data.disk.mapper.uriFromFileName
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.ImageMetadata
import kotlinx.io.files.Path
import com.alexrdclement.mediaplayground.database.model.Image as ImageEntity

fun Image.toImageEntity(): ImageEntity {
    val fileName = fileNameFromUri(uri)
    require(fileName != null) { "Image uri must be a file uri" }
    return ImageEntity(
        id = id.value,
        fileName = fileName,
        widthPx = metadata?.widthPx,
        heightPx = metadata?.heightPx,
        dateTimeOriginal = metadata?.dateTimeOriginal,
        gpsLatitude = metadata?.gpsLatitude,
        gpsLongitude = metadata?.gpsLongitude,
        cameraMake = metadata?.cameraMake,
        cameraModel = metadata?.cameraModel,
        notes = notes,
    )
}

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
        notes = notes,
    )
}
