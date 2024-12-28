package com.alexrdclement.mediaplayground.data.audio.local

import com.alexrdclement.mediaplayground.data.audio.local.mapper.toAlbumEntity
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toArtistEntity
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toImage
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toImageEntity
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toSimpleArtist
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toTrack
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toTrackEntity
import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.dao.ImageDao
import com.alexrdclement.mediaplayground.database.dao.TrackDao
import com.alexrdclement.mediaplayground.model.audio.Image
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
    private val artistDao: ArtistDao,
    private val albumDao: AlbumDao,
    private val imageDao: ImageDao,
    private val trackDao: TrackDao,
    private val completeTrackDao: CompleteTrackDao,
) {
    fun clearTracks() {
        // TODO
    }

    suspend fun putTrack(track: Track) {
        // TODO: Use transaction
        track.artists.forEach {
            artistDao.insert(it.toArtistEntity())
        }
        albumDao.insert(track.simpleAlbum.toAlbumEntity())
        val images = track.simpleAlbum.images.map { it.toImageEntity(albumId = track.simpleAlbum.id) }
        imageDao.insert(*images.toTypedArray())
        trackDao.insert(track.toTrackEntity())
    }

    suspend fun getArtistByName(name: String): SimpleArtist? {
        return artistDao.getArtistByName(name)?.toSimpleArtist()
    }

    suspend fun getAlbumByTitleAndArtistId(albumTitle: String, artistId: String): SimpleAlbum? {
        val artist = artistDao.getArtist(artistId)?.toSimpleArtist() ?: return null
        val album = albumDao.getAlbumByTitleAndArtistId(albumTitle, artistId) ?: return null
        val images = imageDao.getImagesForAlbum(albumId = album.id).map { it.toImage() }
        return album.toSimpleAlbum(artists = listOf(artist), images = images)
    }

    suspend fun getTracks(): List<Track> {
        return completeTrackDao.getTracks().map { it.toTrack() }
    }

    fun getTracksFlow(): Flow<List<Track>> {
        return completeTrackDao.getTracksFlow().map { trackEntities -> trackEntities.map { it.toTrack() } }
    }

    suspend fun getTrack(trackId: TrackId): Track? {
        return completeTrackDao.getTrack(trackId.value)?.toTrack()
    }
}
