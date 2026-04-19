package com.alexrdclement.mediaplayground.data.album

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.data.album.local.LocalAudioAlbumDataStore
import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class AudioAlbumRepositoryImpl @Inject constructor(
    private val localAudioAlbumDataStore: LocalAudioAlbumDataStore,
) : AudioAlbumRepository {
    override suspend fun getAlbum(id: AudioAlbumId): AudioAlbum? =
        localAudioAlbumDataStore.getAlbum(id)

    override fun getAlbumFlow(id: AudioAlbumId): Flow<AudioAlbum?> =
        localAudioAlbumDataStore.getAlbumFlow(id)

    override fun getAlbumCountFlow(): Flow<Int> =
        localAudioAlbumDataStore.getAlbumCountFlow()

    override fun getAlbumPagingData(config: PagingConfig): Flow<PagingData<AudioAlbum>> =
        localAudioAlbumDataStore.getAlbumPagingData(config)

    override suspend fun getAlbumTrackCount(id: AudioAlbumId): Int =
        localAudioAlbumDataStore.getAlbumTrackCount(albumId = id)

    override suspend fun getAlbumByTitleAndArtistId(
        albumTitle: String,
        artistId: ArtistId,
    ): SimpleAlbum? = localAudioAlbumDataStore.getAlbumByTitleAndArtistId(
        albumTitle = albumTitle,
        artistId = artistId,
    )

    override suspend fun put(album: SimpleAlbum) =
        localAudioAlbumDataStore.put(album)

    override suspend fun updateAlbumTitle(id: AudioAlbumId, title: String) =
        localAudioAlbumDataStore.updateAlbumTitle(id, title)

    override suspend fun updateAlbumNotes(id: AudioAlbumId, notes: String?) =
        localAudioAlbumDataStore.updateAlbumNotes(id, notes)

    override suspend fun delete(id: AudioAlbumId) =
        localAudioAlbumDataStore.delete(id)
}
