package com.alexrdclement.mediaplayground.data.album

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import kotlinx.coroutines.flow.Flow

interface AudioAlbumRepository {
    suspend fun getAlbum(id: AudioAlbumId): AudioAlbum?
    fun getAlbumFlow(id: AudioAlbumId): Flow<AudioAlbum?>
    fun getAlbumCountFlow(): Flow<Int>
    fun getAlbumPagingData(config: PagingConfig): Flow<PagingData<AudioAlbum>>
    suspend fun getAlbumTrackCount(id: AudioAlbumId): Int
    suspend fun getAlbumByTitleAndArtistId(
        albumTitle: String,
        artistId: ArtistId
    ): SimpleAlbum?
    suspend fun put(album: SimpleAlbum)
    suspend fun updateAlbumTitle(id: AudioAlbumId, title: String)
    suspend fun updateAlbumNotes(id: AudioAlbumId, notes: String?)
    suspend fun delete(id: AudioAlbumId)
}
