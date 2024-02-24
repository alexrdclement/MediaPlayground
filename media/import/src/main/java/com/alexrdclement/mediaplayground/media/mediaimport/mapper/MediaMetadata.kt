package com.alexrdclement.mediaplayground.media.mediaimport.mapper

import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Image
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import java.util.UUID

fun MediaMetadata.toSimpleArtist(): SimpleArtist {
    return SimpleArtist(
        id = UUID.randomUUID().toString(),
        name = artistName ?: "Unknown artist",
    )
}

fun MediaMetadata.toSimpleAlbum(): SimpleAlbum {
    return SimpleAlbum(
        id = AlbumId(UUID.randomUUID().toString()),
        name = albumTitle ?: "Unknown album",
        artists = listOf(this.toSimpleArtist()),
        images = this.toAlbumArtwork()?.let { listOf(it) } ?: emptyList()
    )
}

fun MediaMetadata.toAlbumArtwork(): Image? {
    val imageUri = imageUri ?: return null
    return Image(imageUri.toString())
}
