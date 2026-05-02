package com.alexrdclement.mediaplayground.data.album.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.database.dao.CompleteAlbumDao
import com.alexrdclement.mediaplayground.database.dao.SimpleAlbumDao
import com.alexrdclement.mediaplayground.database.mapping.toAlbum
import com.alexrdclement.mediaplayground.database.mapping.toAlbumEntity
import com.alexrdclement.mediaplayground.database.mapping.toMediaCollectionEntity
import com.alexrdclement.mediaplayground.database.mapping.toSimpleAlbum
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.deleteAlbum
import com.alexrdclement.mediaplayground.database.transaction.insertAlbum
import com.alexrdclement.mediaplayground.database.transaction.updateAlbum
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import dev.zacsweers.metro.Inject
import kotlin.time.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class LocalAudioAlbumDataStore @Inject constructor(
    private val simpleAlbumDao: SimpleAlbumDao,
    private val completeAlbumDao: CompleteAlbumDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
) {
    fun getAlbumCountFlow(): Flow<Int> {
        return completeAlbumDao.getAlbumCountFlow().distinctUntilChanged()
    }

    fun getAlbumPagingData(config: PagingConfig): Flow<PagingData<AudioAlbum>> {
        return Pager(config = config) {
            completeAlbumDao.getAlbumsPagingSource()
        }.flow.map { pagingData ->
            pagingData.map { it.toAlbum() }
        }
    }

    suspend fun getAlbum(albumId: AlbumId): AudioAlbum? {
        return completeAlbumDao.getAlbum(albumId.value)?.toAlbum()
    }

    fun getAlbumFlow(albumId: AlbumId): Flow<AudioAlbum?> {
        return completeAlbumDao.getAlbumFlow(albumId.value).map { completeAlbum ->
            completeAlbum?.toAlbum()
        }
    }

    suspend fun getAlbumByTitleAndArtistId(
        albumTitle: String,
        artistId: ArtistId,
    ): SimpleAlbum? {
        val simpleAlbumEntity = simpleAlbumDao.getAlbumByTitleAndArtistId(
            title = albumTitle,
            artistId = artistId.value,
        )
        return simpleAlbumEntity?.toSimpleAlbum()
    }

    suspend fun getAlbumTrackCount(albumId: AlbumId): Int {
        return completeAlbumDao.getAlbum(albumId.value)?.tracks?.count() ?: 0
    }

    suspend fun put(album: SimpleAlbum) = databaseTransactionRunner.run {
        insertAlbum(
            mediaCollection = album.toMediaCollectionEntity(),
            album = album.toAlbumEntity(),
            imageIds = album.images.map { it.id.value }.toSet(),
            artistIds = album.artists.map { it.id.value }.toSet(),
        )
    }

    suspend fun addImagesToAlbum(albumId: AlbumId, imageIds: Set<ImageId>) = databaseTransactionRunner.run {
        val crossRefs = imageIds.map { AlbumImageCrossRef(albumId.value, it.value) }.toTypedArray()
        albumImageDao.insert(*crossRefs)
    }

    suspend fun updateAlbumTitle(id: AlbumId, title: String) {
        databaseTransactionRunner.run {
            val mediaCollection = mediaCollectionDao.getMediaCollection(id.value) ?: return@run
            mediaCollectionDao.update(mediaCollection.copy(title = title, modifiedAt = Clock.System.now()))
        }
    }

    suspend fun updateAlbumNotes(id: AlbumId, notes: String?) {
        databaseTransactionRunner.run {
            val album = albumDao.getAlbum(id.value) ?: return@run
            updateAlbum(album.copy(notes = notes, modifiedAt = Clock.System.now()))
        }
    }

    suspend fun delete(id: AlbumId) = databaseTransactionRunner.run {
        deleteAlbum(id.value)
    }
}
