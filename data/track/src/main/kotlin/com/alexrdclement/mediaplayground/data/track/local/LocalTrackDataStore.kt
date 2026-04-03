package com.alexrdclement.mediaplayground.data.track.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.track.local.mapper.toAlbumEntity
import com.alexrdclement.mediaplayground.data.track.local.mapper.toArtistEntity
import com.alexrdclement.mediaplayground.data.track.local.mapper.toImageEntity
import com.alexrdclement.mediaplayground.data.track.local.mapper.toTrack
import com.alexrdclement.mediaplayground.data.track.local.mapper.toTrackEntity
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.database.dao.AlbumArtistDao
import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.dao.AlbumImageDao
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.dao.ImageDao
import com.alexrdclement.mediaplayground.database.dao.TrackDao
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.media.model.audio.Track
import com.alexrdclement.mediaplayground.media.model.audio.TrackId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class LocalTrackDataStore @Inject constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val artistDao: ArtistDao,
    private val albumDao: AlbumDao,
    private val imageDao: ImageDao,
    private val albumImageDao: AlbumImageDao,
    private val trackDao: TrackDao,
    private val albumArtistDao: AlbumArtistDao,
    private val completeTrackDao: CompleteTrackDao,
    private val pathProvider: PathProvider,
) {
    suspend fun putTrack(track: Track) {
        transactionRunner.run {
            track.artists.forEach {
                artistDao.insert(it.toArtistEntity())
            }

            albumDao.insert(track.simpleAlbum.toAlbumEntity(source = track.source))

            val albumArtistCrossRef = track.artists.map { artist ->
                AlbumArtistCrossRef(albumId = track.simpleAlbum.id.value, artistId = artist.id)
            }
            albumArtistDao.insert(*albumArtistCrossRef.toTypedArray())

            val images = track.simpleAlbum.images.map { it.toImageEntity() }
            imageDao.insert(*images.toTypedArray())

            val albumImageCrossRefs = track.simpleAlbum.images.map { image ->
                AlbumImageCrossRef(albumId = track.simpleAlbum.id.value, imageId = image.id.value)
            }
            albumImageDao.insert(*albumImageCrossRefs.toTypedArray())

            trackDao.insert(track.toTrackEntity())
        }
    }

    suspend fun deleteTrack(track: Track) {
        transactionRunner.run {
            val album = albumDao.getAlbum(track.simpleAlbum.id.value)
            if (album == null || trackDao.getTracksForAlbum(album.id).size > 1) {
                trackDao.delete(track.id.value)
                return@run
            }

            for (artist in track.artists) {
                albumArtistDao.delete(AlbumArtistCrossRef(album.id, artist.id))
                albumArtistDao.getArtistAlbums(artist.id).let { artistAlbums ->
                    if (artistAlbums.isEmpty()) {
                        artistDao.delete(artist.id)
                    }
                }
            }

            albumImageDao.deleteForAlbum(track.simpleAlbum.id.value)
            for (image in track.images) {
                imageDao.delete(image.id.value)
            }

            albumDao.delete(album.id)

            trackDao.delete(track.id.value)
        }
    }

    fun getTrackCountFlow(): Flow<Int> {
        return trackDao.getTrackCountFlow().distinctUntilChanged()
    }

    fun getTrackPagingData(config: PagingConfig): Flow<PagingData<Track>> {
        return Pager(config = config) {
            completeTrackDao.getTracksPagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.toTrack(
                    albumDir = pathProvider.getAlbumDir(it.album.id),
                    imagesDir = pathProvider.getImagesDir(),
                )
            }
        }
    }

    suspend fun getTrack(trackId: TrackId): Track? {
        val completeTrack = completeTrackDao.getTrack(trackId.value)
        return completeTrack?.toTrack(
            albumDir = pathProvider.getAlbumDir(completeTrack.album.id),
            imagesDir = pathProvider.getImagesDir(),
        )
    }

    fun getTrackFlow(trackId: TrackId): Flow<Track?> {
        return completeTrackDao.getTrackFlow(trackId.value).map { completeTrack ->
            completeTrack?.toTrack(
                albumDir = pathProvider.getAlbumDir(completeTrack.album.id),
                imagesDir = pathProvider.getImagesDir(),
            )
        }
    }

    suspend fun updateTrackTitle(id: TrackId, title: String) {
        val track = trackDao.getTrack(id.value) ?: return
        trackDao.update(track.copy(title = title))
    }

    suspend fun updateTrackNumber(id: TrackId, trackNumber: Int?) {
        val track = trackDao.getTrack(id.value) ?: return
        trackDao.update(track.copy(trackNumber = trackNumber))
    }

    suspend fun updateTrackNotes(id: TrackId, notes: String?) {
        val track = trackDao.getTrack(id.value) ?: return
        trackDao.update(track.copy(notes = notes))
    }
}
