package com.alexrdclement.mediaplayground.data.track.local.mapper

import com.alexrdclement.mediaplayground.data.disk.mapper.fileNameFromUri
import com.alexrdclement.mediaplayground.data.disk.mapper.uriFromFileName
import com.alexrdclement.mediaplayground.media.model.audio.Image
import com.alexrdclement.mediaplayground.media.model.audio.ImageId
import kotlinx.io.files.Path
import com.alexrdclement.mediaplayground.database.model.Image as ImageEntity

fun Image.toImageEntity(): ImageEntity {
    val fileName = fileNameFromUri(uri)
    require(fileName != null) { "Image uri must be a file uri" }
    return ImageEntity(
        id = id.value,
        fileName = fileName,
        notes = notes,
    )
}

fun ImageEntity.toImage(imagesDir: Path): Image {
    val uri = uriFromFileName(imagesDir, fileName)
    require(uri != null) { "Image fileName must be a file name" }
    return Image(
        id = ImageId(id),
        uri = uri,
        notes = notes,
    )
}
