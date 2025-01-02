package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum2
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AlbumDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun get_returnsInserted() = runTest {
        val album = FakeAlbum1
        albumDao.insert(album)

        val result = albumDao.getAlbum(album.id)

        assertAlbumEquals(album, result)
    }

    @Test
    fun get_returnsNullForNonExistent() = runTest {
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
    fun deleteNonExistent() = runTest {
        albumDao.delete("nonexistent")
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
