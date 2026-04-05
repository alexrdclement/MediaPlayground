package com.alexrdclement.mediaplayground.data.album

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.AlbumId
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    suspend fun getAlbum(id: AlbumId): Album?
    fun getAlbumFlow(id: AlbumId): Flow<Album?>
    fun getAlbumCountFlow(): Flow<Int>
    fun getAlbumPagingData(config: PagingConfig): Flow<PagingData<Album>>
    suspend fun updateAlbumTitle(id: AlbumId, title: String)
    suspend fun updateAlbumNotes(id: AlbumId, notes: String?)
    suspend fun deleteAlbum(id: AlbumId)
}
