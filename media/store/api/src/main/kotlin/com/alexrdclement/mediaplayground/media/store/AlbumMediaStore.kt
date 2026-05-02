package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum

interface AlbumMediaStore {
    suspend fun getAlbum(id: AlbumId): AudioAlbum?

    suspend fun getAlbumByTitleAndArtistId(albumTitle: String, artistId: ArtistId): SimpleAlbum?

    suspend fun getAlbumTrackCount(id: AlbumId): Int

    suspend fun addImagesToAlbum(albumId: AlbumId, imageIds: Set<ImageId>)

    context(scope: MediaStoreTransactionScope)
    suspend fun put(album: SimpleAlbum)
}
