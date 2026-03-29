package com.alexrdclement.mediaplayground.data.album.local.mapper

import com.alexrdclement.mediaplayground.data.disk.mapper.uriFromFileName
import com.alexrdclement.mediaplayground.media.model.audio.Image
import kotlinx.io.files.Path
import com.alexrdclement.mediaplayground.database.model.Image as ImageEntity

fun ImageEntity.toImage(albumDir: Path): Image {
    val uri = uriFromFileName(albumDir, fileName)
    require(uri != null) { "Image fileName must be a file name" }
    return Image(
        uri = uri,
    )
}
