package com.alexrdclement.mediaplayground.data.album.local

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.audio.Album
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.audio.SimpleAlbum
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class LocalAlbumRepositoryImpl @Inject constructor(
    private val localAlbumDataStore: LocalAlbumDataStore,
) : LocalAlbumRepository {

    override suspend fun getAlbum(id: AlbumId): Album? =
        localAlbumDataStore.getAlbum(id)

    override fun getAlbumFlow(id: AlbumId): Flow<Album?> =
        localAlbumDataStore.getAlbumFlow(id)

    override fun getAlbumCountFlow(): Flow<Int> =
        localAlbumDataStore.getAlbumCountFlow()

    override fun getAlbumPagingData(config: PagingConfig): Flow<PagingData<Album>> =
        localAlbumDataStore.getAlbumPagingData(config)

    override suspend fun getAlbumByTitleAndArtistId(albumTitle: String, artistId: String): SimpleAlbum? =
        localAlbumDataStore.getAlbumByTitleAndArtistId(albumTitle, artistId)

    override suspend fun updateAlbumTitle(id: AlbumId, title: String) =
        localAlbumDataStore.updateAlbumTitle(id, title)

    override suspend fun updateAlbumNotes(id: AlbumId, notes: String?) =
        localAlbumDataStore.updateAlbumNotes(id, notes)

    override suspend fun deleteAlbum(id: AlbumId) =
        localAlbumDataStore.deleteAlbum(id)
}
