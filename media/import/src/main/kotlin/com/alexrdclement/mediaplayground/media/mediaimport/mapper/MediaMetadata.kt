package com.alexrdclement.mediaplayground.media.mediaimport.mapper

import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Image
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import java.util.UUID

fun MediaMetadata.toSimpleArtist(
    name: String,
): SimpleArtist {
    return SimpleArtist(
        id = UUID.randomUUID().toString(),
        name = name,
    )
}

fun MediaMetadata.toSimpleAlbum(
    title: String,
    artists: List<SimpleArtist>,
    images: List<Image>,
): SimpleAlbum {
    return SimpleAlbum(
        id = AlbumId(UUID.randomUUID().toString()),
        name = title,
        artists = artists,
        images = images,
    )
}

fun MediaMetadata.toImage(): Image? {
    val imageUri = imagePath ?: return null
    return Image(imageUri.toString())
}
