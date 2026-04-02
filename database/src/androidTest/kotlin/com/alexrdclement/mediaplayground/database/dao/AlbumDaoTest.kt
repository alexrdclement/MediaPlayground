package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum2
import com.alexrdclement.mediaplayground.database.fakes.FakeTrack1
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AlbumDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var trackDao: TrackDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
        trackDao = db.trackDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun getAlbum_returnsInserted() = runTest {
        val album = FakeAlbum1
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
        albumDao.insert(album)

        albumDao.delete(album.id)

        val resultAfterDelete = albumDao.getAlbum(album.id)
        assertEquals(null, resultAfterDelete)
    }

    @Test
    fun getAlbumCountFlow_updatesOnInsert() = runTest {
        val album1 = FakeAlbum1
        val album2 = FakeAlbum2

        albumDao.insert(album1)
        val count = albumDao.getAlbumCountFlow().first()
        assertEquals(1, count)

        albumDao.insert(album2)
        val count2 = albumDao.getAlbumCountFlow().first()
        assertEquals(2, count2)
    }

    @Test
    fun insert_ignoresExistingAlbum() = runTest {
        val album = FakeAlbum1
        albumDao.insert(album)
        albumDao.insert(album.copy(title = "Updated Title"))

        val result = albumDao.getAlbum(album.id)
        assertAlbumEquals(album, result)
    }

    @Test
    fun update_updatesAlbum() = runTest {
        val album = FakeAlbum1
        albumDao.insert(album)

        albumDao.update(album.copy(title = "Updated Title"))

        val result = albumDao.getAlbum(album.id)
        assertNotNull(result)
        assertEquals("Updated Title", result.title)
    }

    @Test
    fun update_doesNotCascadeDeleteTracks() = runTest {
        val album = FakeAlbum1
        albumDao.insert(album)
        val track = FakeTrack1.copy(albumId = album.id)
        trackDao.insert(track)

        albumDao.update(album.copy(title = "Updated Title"))

        assertTrackEquals(track, trackDao.getTrack(track.id))
    }

    @Test
    fun getAlbumCountFlow_updatesOnDelete() = runTest {
        val album1 = FakeAlbum1
        val album2 = FakeAlbum2

        albumDao.insert(album1)
        albumDao.insert(album2)

        albumDao.delete(album1.id)

        val count = albumDao.getAlbumCountFlow().first()
        assertEquals(1, count)
    }
}
