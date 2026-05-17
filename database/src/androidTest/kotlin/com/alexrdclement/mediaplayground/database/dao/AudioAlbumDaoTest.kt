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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AudioAlbumDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var mediaCollectionDao: MediaCollectionDao
    private lateinit var completeAlbumDao: CompleteAlbumDao
    private lateinit var trackDao: TrackDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
        mediaCollectionDao = db.mediaCollectionDao()
        completeAlbumDao = db.completeAlbumDao()
        trackDao = db.trackDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun getAlbum_returnsInserted() = runTest {
        val album = FakeAlbum1
        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(album)

        val result = albumDao.getAlbum(album.id)

        assertAlbumEquals(album, result)
    }

    @Test
    fun getAlbum_returnsNullForNonExistent() = runTest {
        val result = albumDao.getAlbum("nonexistent")
        assertNull(result)
    }

    @Test
    fun delete_removesEntity() = runTest {
        val album = FakeAlbum1
        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(album)

        albumDao.delete(album.id)

        val resultAfterDelete = albumDao.getAlbum(album.id)
        assertEquals(null, resultAfterDelete)
    }

    @Test
    fun getAlbumCountFlow_updatesOnInsert() = runTest {
        val album1 = FakeAlbum1
        val album2 = FakeAlbum2

        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(album1)
        val count = completeAlbumDao.getAlbumCountFlow().first()
        assertEquals(1, count)

        mediaCollectionDao.insert(FakeMediaCollection2)
        albumDao.insert(album2)
        val count2 = completeAlbumDao.getAlbumCountFlow().first()
        assertEquals(2, count2)
    }

    @Test
    fun insert_ignoresExistingAlbum() = runTest {
        val album = FakeAlbum1
        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(album)
        albumDao.insert(album.copy(notes = "Updated Notes"))

        val result = albumDao.getAlbum(album.id)
        assertAlbumEquals(album, result)
    }

    @Test
    fun update_updatesAlbum() = runTest {
        val album = FakeAlbum1
        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(album)

        albumDao.update(album.copy(notes = "Updated Notes"))

        val result = albumDao.getAlbum(album.id)
        assertNotNull(result)
        assertEquals("Updated Notes", result.notes)
    }

    @Test
    fun update_doesNotCascadeDeleteTracks() = runTest {
        val album = FakeAlbum1
        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(album)
        trackDao.insert(FakeTrack1)

        albumDao.update(album.copy(notes = "Updated Notes"))

        assertTrackEquals(FakeTrack1, trackDao.getTrack(FakeTrack1.id))
    }

    @Test
    fun getAlbumCountFlow_updatesOnDelete() = runTest {
        val album1 = FakeAlbum1
        val album2 = FakeAlbum2

        mediaCollectionDao.insert(FakeMediaCollection1)
        mediaCollectionDao.insert(FakeMediaCollection2)
        albumDao.insert(album1)
        albumDao.insert(album2)

        albumDao.delete(album1.id)

        val count = completeAlbumDao.getAlbumCountFlow().first()
        assertEquals(1, count)
    }
}
