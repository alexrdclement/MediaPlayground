package com.alexrdclement.mediaplayground.media.mediaimport.mapper

import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Image
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import kotlinx.io.files.Path
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
    id: AlbumId,
    title: String,
    artists: List<SimpleArtist>,
    images: List<Image>,
): SimpleAlbum {
    return SimpleAlbum(
        id = id,
        name = title,
        artists = artists,
        images = images,
    )
}

fun MediaMetadata.toImage(
    imageFilePath: Path,
): Image? {
    if (embeddedPicture == null) {
        return null
    }
    return Image(
        uri = imageFilePath.toString(),
    )
}
