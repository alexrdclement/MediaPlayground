package com.alexrdclement.mediaplayground.media.mediaimport.mapper

import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.Source
import kotlinx.collections.immutable.PersistentList
import kotlinx.io.files.Path
import java.util.UUID

fun MediaMetadata.Image.toImage(
    id: ImageId,
    uri: Path,
): Image {
    return Image(
        id = id,
        uri = uri.toString(),
        mimeType = mimeType,
        extension = extension,
        widthPx = widthPx,
        heightPx = heightPx,
        dateTimeOriginal = dateTimeOriginal,
        gpsLatitude = gpsLatitude,
        gpsLongitude = gpsLongitude,
        cameraMake = cameraMake,
        cameraModel = cameraModel,
    )
}

fun MediaMetadata.Audio.toArtist(
    name: String,
): Artist {
    return Artist(
        id = ArtistId(UUID.randomUUID().toString()),
        name = name,
        notes = null,
    )
}

fun MediaMetadata.Audio.toSimpleAlbum(
    id: AlbumId,
    title: String,
    artists: PersistentList<Artist>,
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

fun MediaMetadata.Audio.toImage(
    id: ImageId,
    imageFilePath: Path,
    mimeType: String,
    extension: String,
): Image? {
    if (embeddedPicture == null) {
        return null
    }
    return Image(
        id = id,
        uri = imageFilePath.toString(),
        mimeType = mimeType,
        extension = extension,
    )
}
