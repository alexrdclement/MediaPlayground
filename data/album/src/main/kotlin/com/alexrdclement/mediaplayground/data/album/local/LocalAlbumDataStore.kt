package com.alexrdclement.mediaplayground.data.album.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.database.dao.CompleteAlbumDao
import com.alexrdclement.mediaplayground.database.dao.SimpleAlbumDao
import com.alexrdclement.mediaplayground.database.mapping.toAlbum
import com.alexrdclement.mediaplayground.database.mapping.toAlbumEntity
import com.alexrdclement.mediaplayground.database.mapping.toSimpleAlbum
import com.alexrdclement.mediaplayground.database.model.id
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.deleteAlbum
import com.alexrdclement.mediaplayground.database.transaction.insertAlbum
import com.alexrdclement.mediaplayground.database.transaction.updateAlbum
import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class LocalAlbumDataStore @Inject constructor(
    private val simpleAlbumDao: SimpleAlbumDao,
    private val completeAlbumDao: CompleteAlbumDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    private val pathProvider: PathProvider,
) {
    fun getAlbumCountFlow(): Flow<Int> {
        return completeAlbumDao.getAlbumCountFlow().distinctUntilChanged()
    }

    fun getAlbumPagingData(config: PagingConfig): Flow<PagingData<Album>> {
        return Pager(config = config) {
            completeAlbumDao.getAlbumsPagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.toAlbum(
                    mediaItemDir = pathProvider.getAlbumDir(it.id),
                    imagesDir = pathProvider.getImagesDir(),
                )
            }
        }
    }

    suspend fun getAlbum(albumId: AlbumId): Album? {
        return completeAlbumDao.getAlbum(albumId.value)?.toAlbum(
            mediaItemDir = pathProvider.getAlbumDir(albumId.value),
            imagesDir = pathProvider.getImagesDir(),
        )
    }

    fun getAlbumFlow(albumId: AlbumId): Flow<Album?> {
        return completeAlbumDao.getAlbumFlow(albumId.value).map { completeAlbum ->
            completeAlbum?.toAlbum(
                mediaItemDir = pathProvider.getAlbumDir(albumId.value),
                imagesDir = pathProvider.getImagesDir(),
            )
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
        return simpleAlbumEntity?.toSimpleAlbum(
            mediaItemDir = pathProvider.getAlbumDir(simpleAlbumEntity.album.id),
            imagesDir = pathProvider.getImagesDir(),
        )
    }

    suspend fun getAlbumTrackCount(albumId: AlbumId): Int {
        return completeAlbumDao.getAlbum(albumId.value)?.tracks?.count() ?: 0
    }

    suspend fun put(album: SimpleAlbum) = databaseTransactionRunner.run {
        insertAlbum(
            album = album.toAlbumEntity(),
            imageIds = album.images.map { it.id.value }.toSet(),
            artistIds = album.artists.map { it.id.value }.toSet(),
        )
    }

    suspend fun updateAlbumTitle(id: AlbumId, title: String) {
        databaseTransactionRunner.run {
            val album = albumDao.getAlbum(id.value) ?: return@run
            albumDao.update(album.copy(title = title))
        }
    }

    suspend fun updateAlbumNotes(id: AlbumId, notes: String?) {
        databaseTransactionRunner.run {
            val album = albumDao.getAlbum(id.value) ?: return@run
            updateAlbum(album.copy(notes = notes))
        }
    }

    suspend fun deleteAlbum(id: AlbumId) = databaseTransactionRunner.run {
        deleteAlbum(id.value)
    }

    suspend fun delete(albumId: AlbumId) = databaseTransactionRunner.run {
        deleteAlbum(albumId.value)
    }
}
