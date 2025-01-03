package com.alexrdclement.mediaplayground.data.audio.local.mapper

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Image
import kotlinx.io.files.Path
import com.alexrdclement.mediaplayground.database.model.Image as ImageEntity

fun Image.toImageEntity(albumId: AlbumId): ImageEntity {
    val fileName = fileNameFromUri(uri)
    require(fileName != null) { "Image uri must be a file uri" }
    return ImageEntity(
        id = uri.hashCode().toString(),
        fileName = fileName,
        albumId = albumId.value,
    )
}

fun ImageEntity.toImage(albumDir: Path): Image {
    val uri = uriFromFileName(albumDir, fileName)
    require(uri != null) { "Image fileName must be a file name" }
    return Image(
        uri = uri,
    )
}
