package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.Source
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import java.util.UUID

private const val UnknownAlbumName = "Unknown album"

internal suspend fun makeSimpleAlbum(
    mediaMetadata: MediaMetadata.Audio,
    artist: Artist,
    images: PersistentSet<Image>,
    getAlbumByTitleAndArtistId: suspend (String, ArtistId) -> SimpleAlbum?,
    source: Source,
): SimpleAlbum {
    val albumName = mediaMetadata.albumTitle ?: UnknownAlbumName

    val existingAlbum = getAlbumByTitleAndArtistId(albumName, artist.id)
    if (existingAlbum != null) {
        return existingAlbum
    }

    val albumId = AlbumId(UUID.randomUUID().toString())
    return mediaMetadata.toSimpleAlbum(
        id = albumId,
        title = albumName,
        artists = persistentListOf(artist),
        images = images.toPersistentList(),
        source = source,
    )
}
