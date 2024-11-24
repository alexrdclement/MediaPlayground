package com.alexrdclement.mediaplayground.data.audio.local.mapper

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Image
import com.alexrdclement.mediaplayground.database.model.Image as ImageEntity

fun Image.toImageEntity(albumId: AlbumId): ImageEntity {
    return ImageEntity(
        id = uri.hashCode().toString(),
        uri = uri,
        albumId = albumId.value,
    )
}

fun ImageEntity.toImage(): Image {
    return Image(
        uri = uri,
    )
}
