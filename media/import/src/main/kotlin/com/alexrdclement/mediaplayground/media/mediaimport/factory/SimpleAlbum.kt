package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toImage
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import kotlinx.io.files.Path
import java.util.UUID

private const val UnknownAlbumName = "Unknown album"

internal suspend fun makeSimpleAlbum(
    mediaMetadata: MediaMetadata,
    simpleArtist: SimpleArtist,
    getImageFilePath: (AlbumId) -> Path,
    getAlbumByTitleAndArtistId: suspend (String, String) -> SimpleAlbum?,
): SimpleAlbum {
    val albumName = mediaMetadata.albumTitle ?: UnknownAlbumName

    val existingAlbum = getAlbumByTitleAndArtistId(albumName, simpleArtist.id)
    if (existingAlbum != null) {
        return existingAlbum
    }

    val albumId = AlbumId(UUID.randomUUID().toString())
    val image = mediaMetadata.toImage(getImageFilePath(albumId))
    return mediaMetadata.toSimpleAlbum(
        id = albumId,
        title = albumName,
        artists = listOf(simpleArtist),
        images = listOfNotNull(image),
    )
}
