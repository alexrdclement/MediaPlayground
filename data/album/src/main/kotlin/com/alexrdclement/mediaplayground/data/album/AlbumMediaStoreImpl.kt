package com.alexrdclement.mediaplayground.data.album

import com.alexrdclement.mediaplayground.data.album.local.LocalAlbumDataStore
import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.store.AlbumMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import dev.zacsweers.metro.Inject

class AlbumMediaStoreImpl @Inject constructor(
    private val localAlbumDataStore: LocalAlbumDataStore,
): AlbumMediaStore {

    override suspend fun getAlbum(id: AlbumId): Album? =
        localAlbumDataStore.getAlbum(id)

    override suspend fun getAlbumTrackCount(id: AlbumId): Int =
        localAlbumDataStore.getAlbumTrackCount(id)

    override suspend fun getAlbumByTitleAndArtistId(
        albumTitle: String,
        artistId: ArtistId,
    ): SimpleAlbum? {
        return localAlbumDataStore.getAlbumByTitleAndArtistId(
            albumTitle = albumTitle,
            artistId = artistId,
        )
    }

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(album: SimpleAlbum) {
        localAlbumDataStore.put(album)
    }
}
