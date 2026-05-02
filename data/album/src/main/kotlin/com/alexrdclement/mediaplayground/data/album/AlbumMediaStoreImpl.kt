package com.alexrdclement.mediaplayground.data.album

import com.alexrdclement.mediaplayground.data.album.local.LocalAudioAlbumDataStore
import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.store.AlbumMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import dev.zacsweers.metro.Inject

class AlbumMediaStoreImpl @Inject constructor(
    private val localAudioAlbumDataStore: LocalAudioAlbumDataStore,
): AlbumMediaStore {

    override suspend fun getAlbum(id: AlbumId): AudioAlbum? =
        localAudioAlbumDataStore.getAlbum(id)

    override suspend fun getAlbumTrackCount(id: AlbumId): Int =
        localAudioAlbumDataStore.getAlbumTrackCount(id)

    override suspend fun getAlbumByTitleAndArtistId(
        albumTitle: String,
        artistId: ArtistId,
    ): SimpleAlbum? {
        return localAudioAlbumDataStore.getAlbumByTitleAndArtistId(
            albumTitle = albumTitle,
            artistId = artistId,
        )
    }

    override suspend fun addImagesToAlbum(albumId: AlbumId, imageIds: Set<ImageId>) {
        localAudioAlbumDataStore.addImagesToAlbum(albumId, imageIds)
    }

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(album: SimpleAlbum) {
        localAudioAlbumDataStore.put(album)
    }
}
