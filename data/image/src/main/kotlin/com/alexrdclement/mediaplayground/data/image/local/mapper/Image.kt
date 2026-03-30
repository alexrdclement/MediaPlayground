package com.alexrdclement.mediaplayground.data.image.local.mapper

import com.alexrdclement.mediaplayground.data.disk.mapper.uriFromFileName
import com.alexrdclement.mediaplayground.media.model.audio.Image
import com.alexrdclement.mediaplayground.media.model.audio.ImageId
import kotlinx.io.files.Path
import com.alexrdclement.mediaplayground.database.model.Image as ImageEntity

fun ImageEntity.toImage(imagesDir: Path): Image {
    val uri = uriFromFileName(imagesDir, fileName)
    require(uri != null) { "Image fileName must be a file name" }
    return Image(
        id = ImageId(id),
        uri = uri,
        notes = notes,
    )
}
