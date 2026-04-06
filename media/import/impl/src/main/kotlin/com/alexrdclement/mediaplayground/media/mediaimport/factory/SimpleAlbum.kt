package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.mediaimport.imageExtension
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toImage
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.Source
import kotlinx.collections.immutable.persistentListOf
import kotlinx.io.files.Path
import java.util.UUID

private const val UnknownAlbumName = "Unknown album"

internal suspend fun makeSimpleAlbum(
    mediaMetadata: MediaMetadata.Audio,
    artist: Artist,
    getImageFilePath: (ImageId, extension: String) -> Path,
    getAlbumByTitleAndArtistId: suspend (String, ArtistId) -> SimpleAlbum?,
    source: Source,
): SimpleAlbum {
    val albumName = mediaMetadata.albumTitle ?: UnknownAlbumName

    val existingAlbum = getAlbumByTitleAndArtistId(albumName, artist.id)
    if (existingAlbum != null) {
        return existingAlbum
    }

    val albumId = AlbumId(UUID.randomUUID().toString())
    val imageId = ImageId(UUID.randomUUID().toString())
    val extension = mediaMetadata.embeddedPicture?.imageExtension() ?: "jpg"
    val image = mediaMetadata.toImage(
        id = imageId,
        imageFilePath = getImageFilePath(imageId, extension),
        mimeType = "image/$extension",
        extension = extension,
    )
    return mediaMetadata.toSimpleAlbum(
        id = albumId,
        title = albumName,
        artists = persistentListOf(artist),
        images = image?.let { persistentListOf(image) } ?: persistentListOf(),
        source = source,
    )
}
