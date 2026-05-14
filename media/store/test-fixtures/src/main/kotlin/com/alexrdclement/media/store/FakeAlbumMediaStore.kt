package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.store.AlbumMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Instant

class FakeAlbumMediaStore : AlbumMediaStore {

    private val albums = mutableMapOf<AlbumId, SimpleAlbum>()
    val albumImageLinks = mutableMapOf<AlbumId, MutableSet<ImageId>>()

    override suspend fun getAlbum(id: AlbumId): AudioAlbum? {
        val simpleAlbum = albums[id] ?: return null
        return AudioAlbum(
            id = AudioAlbumId(simpleAlbum.id.value),
            title = simpleAlbum.name,
            artists = simpleAlbum.artists,
            images = simpleAlbum.images,
            items = persistentListOf(),
            notes = null,
            createdAt = Instant.DISTANT_PAST,
            modifiedAt = Instant.DISTANT_PAST,
        )
    }

    override suspend fun getAlbumByTitleAndArtistId(
        albumTitle: String,
        artistId: ArtistId,
    ): SimpleAlbum? = albums.values.find { album ->
        album.name == albumTitle && album.artists.any { it.id == artistId }
    }

    // Tracks are stored separately; this fake always returns 0.
    override suspend fun getAlbumTrackCount(id: AlbumId): Int = 0

    override suspend fun addImagesToAlbum(albumId: AlbumId, imageIds: Set<ImageId>) {
        albumImageLinks.getOrPut(albumId) { mutableSetOf() }.addAll(imageIds)
    }

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(album: SimpleAlbum) {
        albums[album.id] = album
    }
}
