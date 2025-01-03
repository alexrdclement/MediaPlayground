package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeArtist1
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ArtistDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var artistDao: ArtistDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        artistDao = db.artistDao()
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
}
