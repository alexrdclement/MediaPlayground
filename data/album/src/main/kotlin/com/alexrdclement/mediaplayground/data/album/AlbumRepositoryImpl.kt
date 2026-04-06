package com.alexrdclement.mediaplayground.data.album

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.data.album.local.LocalAlbumDataStore
import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class AlbumRepositoryImpl @Inject constructor(
    private val localAlbumDataStore: LocalAlbumDataStore,
) : AlbumRepository {
    override suspend fun getAlbum(id: AlbumId): Album? =
        localAlbumDataStore.getAlbum(id)

    override fun getAlbumFlow(id: AlbumId): Flow<Album?> =
        localAlbumDataStore.getAlbumFlow(id)

    override fun getAlbumCountFlow(): Flow<Int> =
        localAlbumDataStore.getAlbumCountFlow()

    override fun getAlbumPagingData(config: PagingConfig): Flow<PagingData<Album>> =
        localAlbumDataStore.getAlbumPagingData(config)

    override suspend fun getAlbumTrackCount(id: AlbumId): Int =
        localAlbumDataStore.getAlbumTrackCount(albumId = id)

    override suspend fun getAlbumByTitleAndArtistId(
        albumTitle: String,
        artistId: ArtistId,
    ): SimpleAlbum? = localAlbumDataStore.getAlbumByTitleAndArtistId(
        albumTitle = albumTitle,
        artistId = artistId,
    )

    override suspend fun put(album: SimpleAlbum) =
        localAlbumDataStore.put(album)

    override suspend fun updateAlbumTitle(id: AlbumId, title: String) =
        localAlbumDataStore.updateAlbumTitle(id, title)

    override suspend fun updateAlbumNotes(id: AlbumId, notes: String?) =
        localAlbumDataStore.updateAlbumNotes(id, notes)

    override suspend fun delete(id: AlbumId) =
        localAlbumDataStore.deleteAlbum(id)
}
