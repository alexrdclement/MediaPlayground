package com.alexrdclement.mediaplayground.data.album.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.album.local.mapper.toAlbum
import com.alexrdclement.mediaplayground.data.album.local.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.dao.CompleteAlbumDao
import com.alexrdclement.mediaplayground.database.dao.SimpleAlbumDao
import com.alexrdclement.mediaplayground.database.model.id
import com.alexrdclement.mediaplayground.media.model.audio.Album
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.audio.SimpleAlbum
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class LocalAlbumDataStore @Inject constructor(
    private val albumDao: AlbumDao,
    private val completeAlbumDao: CompleteAlbumDao,
    private val simpleAlbumDao: SimpleAlbumDao,
    private val pathProvider: PathProvider,
) {
    fun getAlbumCountFlow(): Flow<Int> {
        return albumDao.getAlbumCountFlow().distinctUntilChanged()
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
        return completeAlbumDao.getAlbum(albumId.value)
            ?.toAlbum(
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

    suspend fun getAlbumByTitleAndArtistId(albumTitle: String, artistId: String): SimpleAlbum? {
        val simpleAlbumEntity = simpleAlbumDao.getAlbumByTitleAndArtistId(albumTitle, artistId)
            ?: return null
        return simpleAlbumEntity.toSimpleAlbum(
            mediaItemDir = pathProvider.getAlbumDir(simpleAlbumEntity.album.id),
            imagesDir = pathProvider.getImagesDir(),
        )
    }

    suspend fun updateAlbumTitle(id: AlbumId, title: String) {
        val album = albumDao.getAlbum(id.value) ?: return
        albumDao.update(album.copy(title = title))
    }

    suspend fun updateAlbumNotes(id: AlbumId, notes: String?) {
        val album = albumDao.getAlbum(id.value) ?: return
        albumDao.update(album.copy(notes = notes))
    }

    suspend fun deleteAlbum(id: AlbumId) {
        albumDao.delete(id.value)
    }
}
