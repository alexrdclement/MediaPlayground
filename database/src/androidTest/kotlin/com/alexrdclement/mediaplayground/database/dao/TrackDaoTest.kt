package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum2
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaCollection1
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaCollection2
import com.alexrdclement.mediaplayground.database.fakes.FakeTrack1
import com.alexrdclement.mediaplayground.database.fakes.FakeTrack2
import com.alexrdclement.mediaplayground.database.fakes.FakeTrack3
import com.alexrdclement.mediaplayground.database.model.AlbumTrackCrossRef
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TrackDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var albumTrackDao: AlbumTrackDao
    private lateinit var completeAlbumDao: CompleteAlbumDao
    private lateinit var trackDao: TrackDao
    private lateinit var completeTrackDao: CompleteTrackDao
    private lateinit var mediaCollectionDao: MediaCollectionDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
        albumTrackDao = db.albumTrackDao()
        completeAlbumDao = db.completeAlbumDao()
        trackDao = db.trackDao()
        completeTrackDao = db.completeTrackDao()
        mediaCollectionDao = db.mediaCollectionDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun getTrack_returnsInserted() = runTest {
        trackDao.insert(FakeTrack1)

        val result = trackDao.getTrack(FakeTrack1.id)

        assertTrackEquals(FakeTrack1, result)
    }

    @Test
    fun getTrack_returnsNullForNonExistent() = runTest {
        val result = trackDao.getTrack("nonexistent")
        assertNull(result)
    }

    @Test
    fun getTracksForAlbum_returnsInserted() = runTest {
        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(FakeAlbum1)
        mediaCollectionDao.insert(FakeMediaCollection2)
        albumDao.insert(FakeAlbum2)
        trackDao.insert(FakeTrack1, FakeTrack2, FakeTrack3)
        albumTrackDao.insert(AlbumTrackCrossRef(FakeAlbum1.id, FakeTrack1.id, 1))
        albumTrackDao.insert(AlbumTrackCrossRef(FakeAlbum1.id, FakeTrack2.id, 2))
        albumTrackDao.insert(AlbumTrackCrossRef(FakeAlbum2.id, FakeTrack3.id, 1))

        val album1Tracks = trackDao.getTracksForAlbum(FakeAlbum1.id)
        val album2Tracks = trackDao.getTracksForAlbum(FakeAlbum2.id)

        assertTrackEquals(FakeTrack1, album1Tracks.find { it.id == FakeTrack1.id })
        assertTrackEquals(FakeTrack2, album1Tracks.find { it.id == FakeTrack2.id })
        assertTrackEquals(FakeTrack3, album2Tracks.find { it.id == FakeTrack3.id })
    }

    @Test
    fun delete_removesEntity() = runTest {
        trackDao.insert(FakeTrack1)

        trackDao.delete(FakeTrack1.id)

        assertNull(trackDao.getTrack(FakeTrack1.id))
    }

    @Test
    fun getTrackCountFlow_updatesOnInsert() = runTest {
        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(FakeAlbum1)
        mediaCollectionDao.insert(FakeMediaCollection2)
        albumDao.insert(FakeAlbum2)
        trackDao.insert(FakeTrack1)
        albumTrackDao.insert(AlbumTrackCrossRef(FakeAlbum1.id, FakeTrack1.id, 1))
        val count = completeTrackDao.getTrackCountFlow().first()
        assertEquals(1, count)

        trackDao.insert(FakeTrack2, FakeTrack3)
        albumTrackDao.insert(AlbumTrackCrossRef(FakeAlbum1.id, FakeTrack2.id, 2))
        albumTrackDao.insert(AlbumTrackCrossRef(FakeAlbum2.id, FakeTrack3.id, 1))
        val count2 = completeTrackDao.getTrackCountFlow().first()
        assertEquals(3, count2)
    }

    @Test
    fun insert_ignoresExistingTrack() = runTest {
        trackDao.insert(FakeTrack1)
        trackDao.insert(FakeTrack1.copy(notes = "Updated notes"))

        val result = trackDao.getTrack(FakeTrack1.id)
        assertTrackEquals(FakeTrack1, result)
    }

    @Test
    fun update_updatesTrack() = runTest {
        trackDao.insert(FakeTrack1)

        trackDao.update(FakeTrack1.copy(notes = "Updated notes"))

        val result = trackDao.getTrack(FakeTrack1.id)
        assertNotNull(result)
        assertEquals("Updated notes", result.notes)
    }

    @Test
    fun getTrackCountFlow_updatesOnDelete() = runTest {
        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(FakeAlbum1)
        mediaCollectionDao.insert(FakeMediaCollection2)
        albumDao.insert(FakeAlbum2)
        trackDao.insert(FakeTrack1, FakeTrack2, FakeTrack3)
        albumTrackDao.insert(AlbumTrackCrossRef(FakeAlbum1.id, FakeTrack1.id, 1))
        albumTrackDao.insert(AlbumTrackCrossRef(FakeAlbum1.id, FakeTrack2.id, 2))
        albumTrackDao.insert(AlbumTrackCrossRef(FakeAlbum2.id, FakeTrack3.id, 1))

        trackDao.delete(FakeTrack1.id)

        val count = completeAlbumDao.getAlbumCountFlow().first()
        assertEquals(2, count)
    }
}
