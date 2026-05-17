package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum2
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaCollection1
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaCollection2
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MediaCollectionDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var mediaCollectionDao: MediaCollectionDao
    private lateinit var albumDao: AlbumDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        mediaCollectionDao = db.mediaCollectionDao()
        albumDao = db.albumDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun getMediaCollection_returnsInserted() = runTest {
        mediaCollectionDao.insert(FakeMediaCollection1)

        val result = mediaCollectionDao.getMediaCollection(FakeMediaCollection1.id)

        assertMediaCollectionEquals(FakeMediaCollection1, result)
    }

    @Test
    fun getMediaCollection_returnsNullForNonExistent() = runTest {
        val result = mediaCollectionDao.getMediaCollection("nonexistent")
        assertNull(result)
    }

    @Test
    fun insert_ignoresExisting() = runTest {
        mediaCollectionDao.insert(FakeMediaCollection1)
        mediaCollectionDao.insert(FakeMediaCollection1.copy(title = "Updated Title"))

        val result = mediaCollectionDao.getMediaCollection(FakeMediaCollection1.id)
        assertMediaCollectionEquals(FakeMediaCollection1, result)
    }

    @Test
    fun update_updatesMediaCollection() = runTest {
        mediaCollectionDao.insert(FakeMediaCollection1)

        mediaCollectionDao.update(FakeMediaCollection1.copy(title = "Updated Title"))

        val result = mediaCollectionDao.getMediaCollection(FakeMediaCollection1.id)
        assertNotNull(result)
        assertEquals("Updated Title", result.title)
    }

    @Test
    fun delete_removesEntity() = runTest {
        mediaCollectionDao.insert(FakeMediaCollection1)

        mediaCollectionDao.delete(FakeMediaCollection1.id)

        assertNull(mediaCollectionDao.getMediaCollection(FakeMediaCollection1.id))
    }

    @Test
    fun delete_cascadesToAlbum() = runTest {
        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(FakeAlbum1)

        mediaCollectionDao.delete(FakeMediaCollection1.id)

        assertNull(albumDao.getAlbum(FakeAlbum1.id))
    }

    @Test
    fun delete_onlyCascadesToOwnAlbum() = runTest {
        mediaCollectionDao.insert(FakeMediaCollection1)
        mediaCollectionDao.insert(FakeMediaCollection2)
        albumDao.insert(FakeAlbum1)
        albumDao.insert(FakeAlbum2)

        mediaCollectionDao.delete(FakeMediaCollection1.id)

        assertNull(albumDao.getAlbum(FakeAlbum1.id))
        assertNotNull(albumDao.getAlbum(FakeAlbum2.id))
    }
}
