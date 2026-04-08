package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.store.AlbumMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import kotlinx.collections.immutable.persistentListOf

class FakeAlbumMediaStore : AlbumMediaStore {

    private val albums = mutableMapOf<AlbumId, SimpleAlbum>()

    override suspend fun getAlbum(id: AlbumId): Album? {
        val simpleAlbum = albums[id] ?: return null
        return Album(
            id = simpleAlbum.id,
            title = simpleAlbum.name,
            artists = simpleAlbum.artists,
            images = simpleAlbum.images,
            tracks = persistentListOf(),
            source = simpleAlbum.source,
            notes = null,
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

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(album: SimpleAlbum) {
        albums[album.id] = album
    }
}
