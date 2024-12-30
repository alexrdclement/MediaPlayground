package com.alexrdclement.mediaplayground.data.audio.local

import com.alexrdclement.mediaplayground.data.audio.local.mapper.toAlbum
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toAlbumEntity
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toArtistEntity
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toImage
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toImageEntity
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toSimpleArtist
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toTrack
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toTrackEntity
import com.alexrdclement.mediaplayground.database.dao.AlbumArtistDao
import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.dao.CompleteAlbumDao
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.dao.ImageDao
import com.alexrdclement.mediaplayground.database.dao.TrackDao
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalAudioDataStore @Inject constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val artistDao: ArtistDao,
    private val albumDao: AlbumDao,
    private val imageDao: ImageDao,
    private val trackDao: TrackDao,
    private val albumArtistDao: AlbumArtistDao,
    private val completeTrackDao: CompleteTrackDao,
    private val completeAlbumDao: CompleteAlbumDao,
) {
    fun clearTracks() {
        // TODO
    }

    suspend fun putTrack(track: Track) {
        transactionRunner.run {
            track.artists.forEach {
                artistDao.insert(it.toArtistEntity())
            }

            albumDao.insert(track.simpleAlbum.toAlbumEntity())

            val albumArtistCrossRef = track.artists.map { artist ->
                AlbumArtistCrossRef(albumId = track.simpleAlbum.id.value, artistId = artist.id)
            }
            albumArtistDao.insert(*albumArtistCrossRef.toTypedArray())

            val images = track.simpleAlbum.images
                .map { it.toImageEntity(albumId = track.simpleAlbum.id) }
            imageDao.insert(*images.toTypedArray())

            trackDao.insert(track.toTrackEntity())
        }
    }

    suspend fun getArtistByName(name: String): SimpleArtist? {
        return artistDao.getArtistByName(name)?.toSimpleArtist()
    }

    suspend fun getAlbumByTitleAndArtistId(albumTitle: String, artistId: String): SimpleAlbum? {
        val albumWithArtists = albumArtistDao.getAlbumByTitleAndArtistId(albumTitle, artistId) ?: return null
        val album = albumWithArtists.album
        val images = imageDao.getImagesForAlbum(albumId = album.id).map { it.toImage() }
        val simpleArtists = albumWithArtists.artists.map { it.toSimpleArtist() }
        return album.toSimpleAlbum(artists = simpleArtists, images = images)
    }

    suspend fun getTracks(): List<Track> {
        return completeTrackDao.getTracks().map { it.toTrack() }
    }

    fun getTracksFlow(): Flow<List<Track>> {
        return completeTrackDao.getTracksFlow()
            .map { trackEntities -> trackEntities.map { it.toTrack() } }
    }

    suspend fun getTrack(trackId: TrackId): Track? {
        return completeTrackDao.getTrack(trackId.value)?.toTrack()
    }

    suspend fun getAlbums(): List<Album> {
        return completeAlbumDao.getAlbums().map { it.toAlbum() }
    }

    fun getAlbumsFlow(): Flow<List<Album>> {
        return completeAlbumDao.getAlbumsFlow().map {
            albumFlows -> albumFlows.map { it.toAlbum() }
        }
    }

    suspend fun getAlbum(albumId: AlbumId): Album? {
        return completeAlbumDao.getAlbum(albumId.value)?.toAlbum()
    }
}
