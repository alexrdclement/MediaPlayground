package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.mediaimport.imageExtension
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toImage
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.image.ImageId
import com.alexrdclement.mediaplayground.media.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.media.model.audio.Source
import kotlinx.collections.immutable.persistentListOf
import kotlinx.io.files.Path
import java.util.UUID

private const val UnknownAlbumName = "Unknown album"

internal suspend fun makeSimpleAlbum(
    mediaMetadata: MediaMetadata.Audio,
    simpleArtist: SimpleArtist,
    getImageFilePath: (ImageId, extension: String) -> Path,
    getAlbumByTitleAndArtistId: suspend (String, String) -> SimpleAlbum?,
    source: Source,
): SimpleAlbum {
    val albumName = mediaMetadata.albumTitle ?: UnknownAlbumName

    val existingAlbum = getAlbumByTitleAndArtistId(albumName, simpleArtist.id)
    if (existingAlbum != null) {
        return existingAlbum
    }

    val albumId = AlbumId(UUID.randomUUID().toString())
    val imageId = ImageId(UUID.randomUUID().toString())
    val extension = mediaMetadata.embeddedPicture?.imageExtension() ?: "jpg"
    val image = mediaMetadata.toImage(id = imageId, imageFilePath = getImageFilePath(imageId, extension))
    return mediaMetadata.toSimpleAlbum(
        id = albumId,
        title = albumName,
        artists = persistentListOf(simpleArtist),
        images = image?.let { persistentListOf(image) } ?: persistentListOf(),
        source = source,
    )
}
