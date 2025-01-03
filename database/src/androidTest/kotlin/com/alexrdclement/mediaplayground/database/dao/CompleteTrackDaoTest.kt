package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeCompleteTrack1
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import com.alexrdclement.mediaplayground.database.model.id
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNull

class CompleteTrackDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var artistDao: ArtistDao
    private lateinit var albumArtistDao: AlbumArtistDao
    private lateinit var imageDao: ImageDao
    private lateinit var trackDao: TrackDao
    private lateinit var completeTrackDao: CompleteTrackDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
        artistDao = db.artistDao()
        albumArtistDao = db.albumArtistDao()
        imageDao = db.imageDao()
        trackDao = db.trackDao()
        completeTrackDao = db.completeTrackDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    private suspend fun insertCompleteTrack(completeTrack: CompleteTrack) {
        val albumArtists = completeTrack.artists.map { artist ->
            AlbumArtistCrossRef(
                albumId = completeTrack.album.id,
                artistId = artist.id,
            )
        }
        albumDao.insert(completeTrack.album)
        artistDao.insert(*completeTrack.artists.toTypedArray())
        albumArtistDao.insert(*albumArtists.toTypedArray())
        imageDao.insert(*completeTrack.images.toTypedArray())
        trackDao.insert(completeTrack.track)
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
