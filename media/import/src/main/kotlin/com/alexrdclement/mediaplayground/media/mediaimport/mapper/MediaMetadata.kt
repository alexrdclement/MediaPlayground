package com.alexrdclement.mediaplayground.media.mediaimport.mapper

import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.audio.Image
import com.alexrdclement.mediaplayground.media.model.audio.ImageId
import com.alexrdclement.mediaplayground.media.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.media.model.audio.Source
import kotlinx.collections.immutable.PersistentList
import kotlinx.io.files.Path
import java.util.UUID

fun MediaMetadata.toSimpleArtist(
    name: String,
): SimpleArtist {
    return SimpleArtist(
        id = UUID.randomUUID().toString(),
        name = name,
        notes = null,
    )
}

fun MediaMetadata.toSimpleAlbum(
    id: AlbumId,
    title: String,
    artists: PersistentList<SimpleArtist>,
    images: PersistentList<Image>,
    source: Source,
): SimpleAlbum {
    return SimpleAlbum(
        id = id,
        name = title,
        artists = artists,
        images = images,
        source = source,
    )
}

fun MediaMetadata.toImage(
    id: ImageId,
    imageFilePath: Path,
): Image? {
    if (embeddedPicture == null) {
        return null
    }
    return Image(
        id = id,
        uri = imageFilePath.toString(),
    )
}
