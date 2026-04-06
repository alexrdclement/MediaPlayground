package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum

interface AlbumMediaStore {
    suspend fun getAlbum(id: AlbumId): Album?

    suspend fun getAlbumByTitleAndArtistId(albumTitle: String, artistId: ArtistId): SimpleAlbum?

    suspend fun getAlbumTrackCount(id: AlbumId): Int

    context(scope: MediaStoreTransactionScope)
    suspend fun put(album: SimpleAlbum)
}
