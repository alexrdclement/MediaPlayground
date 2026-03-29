package com.alexrdclement.mediaplayground.data.album

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.audio.Album
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    suspend fun getAlbum(id: AlbumId): Album?
    fun getAlbumFlow(id: AlbumId): Flow<Album?>
    fun getAlbumCountFlow(): Flow<Int>
    fun getAlbumPagingData(config: PagingConfig): Flow<PagingData<Album>>
    suspend fun updateAlbumTitle(id: AlbumId, title: String)
}
