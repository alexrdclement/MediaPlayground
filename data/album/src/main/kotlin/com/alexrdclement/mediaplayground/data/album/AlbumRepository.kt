package com.alexrdclement.mediaplayground.data.album

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    suspend fun getAlbum(id: AlbumId): Album?
    fun getAlbumFlow(id: AlbumId): Flow<Album?>
    fun getAlbumCountFlow(): Flow<Int>
    fun getAlbumPagingData(config: PagingConfig): Flow<PagingData<Album>>
    suspend fun getAlbumTrackCount(id: AlbumId): Int
    suspend fun getAlbumByTitleAndArtistId(
        albumTitle: String,
        artistId: ArtistId
    ): SimpleAlbum?
    suspend fun put(album: SimpleAlbum)
    suspend fun updateAlbumTitle(id: AlbumId, title: String)
    suspend fun updateAlbumNotes(id: AlbumId, notes: String?)
    suspend fun delete(id: AlbumId)
}
