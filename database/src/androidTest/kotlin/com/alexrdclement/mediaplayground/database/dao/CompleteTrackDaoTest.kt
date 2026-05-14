package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeCompleteTrack1
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef
import com.alexrdclement.mediaplayground.database.model.id
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNull

class CompleteTrackDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var mediaCollectionDao: MediaCollectionDao
    private lateinit var albumTrackDao: AlbumTrackDao
    private lateinit var artistDao: ArtistDao
    private lateinit var albumArtistDao: AlbumArtistDao
    private lateinit var albumImageDao: AlbumImageDao
    private lateinit var imageAssetDao: ImageAssetDao
    private lateinit var trackDao: TrackDao
    private lateinit var audioAssetDao: AudioAssetDao
    private lateinit var clipDao: ClipDao
    private lateinit var trackClipDao: TrackClipDao
    private lateinit var completeTrackDao: CompleteTrackDao
    private lateinit var mediaAssetDao: MediaAssetDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
        mediaCollectionDao = db.mediaCollectionDao()
        albumTrackDao = db.albumTrackDao()
        artistDao = db.artistDao()
        albumArtistDao = db.albumArtistDao()
        albumImageDao = db.albumImageDao()
        imageAssetDao = db.imageAssetDao()
        trackDao = db.trackDao()
        audioAssetDao = db.audioAssetDao()
        clipDao = db.clipDao()
        trackClipDao = db.trackClipDao()
        completeTrackDao = db.completeTrackDao()
        mediaAssetDao = db.mediaAssetDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    private suspend fun insertCompleteTrack(completeTrack: CompleteTrack) {
        mediaCollectionDao.insert(completeTrack.mediaCollection)
        trackDao.insert(completeTrack.track)
        for (albumRef in completeTrack.albumRefs) {
            val simpleAlbum = albumRef.simpleAlbum
            mediaCollectionDao.insert(simpleAlbum.mediaCollection)
            albumDao.insert(simpleAlbum.album)
            artistDao.insert(*simpleAlbum.artists.toTypedArray())
            val albumArtists = simpleAlbum.artists.map { artist ->
                AlbumArtistCrossRef(albumId = simpleAlbum.album.id, artistId = artist.id)
            }
            albumArtistDao.insert(*albumArtists.toTypedArray())
            for (completeImage in simpleAlbum.images) {
                mediaAssetDao.insert(completeImage.mediaAsset)
                imageAssetDao.insert(completeImage.imageAsset)
                albumImageDao.insert(AlbumImageCrossRef(albumId = simpleAlbum.album.id, imageId = completeImage.imageAsset.id))
            }
            albumTrackDao.insert(albumRef.albumTrackCrossRef)
        }
        for (completeTrackClip in completeTrack.clips) {
            mediaAssetDao.insert(completeTrackClip.completeAudioClip.mediaAsset)
            audioAssetDao.insert(completeTrackClip.completeAudioClip.audioAsset)
            clipDao.insert(completeTrackClip.completeAudioClip.clip)
            trackClipDao.insert(
                TrackClipCrossRef(
                    id = completeTrackClip.trackClipCrossRef.id,
                    trackId = completeTrack.track.id,
                    clipId = completeTrackClip.completeAudioClip.clip.id,
                    startSampleInTrack = completeTrackClip.trackClipCrossRef.startSampleInTrack,
                    createdAt = completeTrackClip.trackClipCrossRef.createdAt,
                    modifiedAt = completeTrackClip.trackClipCrossRef.modifiedAt,
                )
            )
        }
    }

    @Test
    fun getTrack_returnsInserted() = runTest {
        val completeTrack = FakeCompleteTrack1
        insertCompleteTrack(completeTrack)

        val result = completeTrackDao.getTrack(completeTrack.id)

        assertCompleteTrackEquals(completeTrack, result)
    }

    @Test
    fun getTrack_returnsNullForNonExistent() = runTest {
        val result = completeTrackDao.getTrack("nonexistent")
        assertNull(result)
    }
}
