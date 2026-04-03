package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.database.fakes.FakeArtist1
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ArtistDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var artistDao: ArtistDao
    private lateinit var albumArtistDao: AlbumArtistDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
        artistDao = db.artistDao()
        albumArtistDao = db.albumArtistDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun getArtist_returnsInserted() = runTest {
        val artist = FakeArtist1
        artistDao.insert(artist)

        val result = artistDao.getArtist(artist.id)

        assertArtistEquals(artist, result)
    }

    @Test
    fun getArtist_returnsNullForNonExistent() = runTest {
        val result = artistDao.getArtist("nonexistent")
        assertNull(result)
    }

    @Test
    fun getArtistByName_returnsInserted() = runTest {
        val artist = FakeArtist1
        artistDao.insert(artist)

        val result = artistDao.getArtistByName(artist.name!!)

        assertArtistEquals(artist, result)
    }

    @Test
    fun getArtistByName_returnsNullForNonExistent() = runTest {
        val result = artistDao.getArtistByName("nonexistent")
        assertNull(result)
    }

    @Test
    fun delete_removesEntity() = runTest {
        val artist = FakeArtist1
        artistDao.insert(artist)

        artistDao.delete(artist.id)

        val resultAfterDelete = artistDao.getArtist(artist.id)
        assertEquals(null, resultAfterDelete)
    }

    @Test
    fun insert_ignoresExistingArtist() = runTest {
        val artist = FakeArtist1
        artistDao.insert(artist)
        artistDao.insert(artist.copy(name = "Updated Name"))

        val result = artistDao.getArtist(artist.id)
        assertArtistEquals(artist, result)
    }

    @Test
    fun update_updatesArtist() = runTest {
        val artist = FakeArtist1
        artistDao.insert(artist)

        artistDao.update(artist.copy(name = "Updated Name"))

        val result = artistDao.getArtist(artist.id)
        assertNotNull(result)
        assertEquals("Updated Name", result.name)
    }

    @Test
    fun update_doesNotDeleteAlbumArtistCrossRefs() = runTest {
        albumDao.insert(FakeAlbum1)
        artistDao.insert(FakeArtist1)
        albumArtistDao.insert(AlbumArtistCrossRef(FakeAlbum1.id, FakeArtist1.id))

        artistDao.update(FakeArtist1.copy(name = "Updated Name"))

        val crossRefs = albumArtistDao.getAlbumArtists(FakeAlbum1.id)
        assertTrue(crossRefs.isNotEmpty())
        assertEquals(FakeArtist1.id, crossRefs.first().artistId)
    }
}
