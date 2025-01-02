package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum2
import com.alexrdclement.mediaplayground.database.fakes.FakeArtist1
import com.alexrdclement.mediaplayground.database.fakes.FakeArtist2
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AlbumArtistDaoTest {

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
    fun insert_withoutArtist_throws() = runTest {
        val album = FakeAlbum1
        albumDao.insert(album)
        val artist = FakeArtist1

        assertFailsWith<SQLiteConstraintException> {
            albumArtistDao.insert(AlbumArtistCrossRef(album.id, artist.id))
        }
    }

    @Test
    fun insert_withoutAlbum_throws() = runTest {
        val album = FakeAlbum1
        val artist = FakeArtist1
        artistDao.insert(artist)

        assertFailsWith<SQLiteConstraintException> {
            albumArtistDao.insert(AlbumArtistCrossRef(album.id, artist.id))
        }
    }

    @Test
    fun getAlbumArtists_returnsInserted() = runTest {
        val album1 = FakeAlbum1
        albumDao.insert(album1)
        val album2 = FakeAlbum2
        albumDao.insert(album2)
        val artist1 = FakeArtist1
        artistDao.insert(artist1)
        val artist2 = FakeArtist2
        artistDao.insert(artist2)

        val expectedAlbum1ArtistIds = listOf(artist1.id, artist2.id)
        for (artistId in expectedAlbum1ArtistIds) {
            albumArtistDao.insert(AlbumArtistCrossRef(album1.id, artistId))
        }

        val expectedAlbum2ArtistIds = listOf(artist2.id)
        for (artistId in expectedAlbum2ArtistIds) {
            albumArtistDao.insert(AlbumArtistCrossRef(album2.id, artistId))
        }

        val actualAlbum1Artists = albumArtistDao.getAlbumArtists(album1.id).map { it.artistId }
        assertEquals(expectedAlbum1ArtistIds, actualAlbum1Artists)

        val actualAlbum2Artists = albumArtistDao.getAlbumArtists(album2.id).map { it.artistId }
        assertEquals(expectedAlbum2ArtistIds, actualAlbum2Artists)
    }

    @Test
    fun getAlbumArtists_returnsEmptyForNonExistent() = runTest {
        val result = albumArtistDao.getAlbumArtists("nonexistent")
        assertTrue(result.isEmpty())
    }

    @Test
    fun getArtistAlbums_returnsEmptyForNonExistent() = runTest {
        val result = albumArtistDao.getArtistAlbums("nonexistent")
        assertTrue(result.isEmpty())
    }

    @Test
    fun delete_removesEntity() = runTest {
        val album1 = FakeAlbum1
        albumDao.insert(album1)
        val album2 = FakeAlbum2
        albumDao.insert(album2)
        val artist1 = FakeArtist1
        artistDao.insert(artist1)
        val artist2 = FakeArtist2
        artistDao.insert(artist2)

        val deletedAlbumArtist = AlbumArtistCrossRef(album1.id, artist1.id)
        albumArtistDao.insert(deletedAlbumArtist)

        val nonDeletedAlbumArtistId = artist2.id
        val nonDeletedAlbumArtists = listOf(
            AlbumArtistCrossRef(album1.id, nonDeletedAlbumArtistId),
            AlbumArtistCrossRef(album2.id, nonDeletedAlbumArtistId),
        )
        for (albumArtist in nonDeletedAlbumArtists) {
            albumArtistDao.insert(albumArtist)
        }

        albumArtistDao.delete(deletedAlbumArtist)

        val deletedResults = albumArtistDao.getArtistAlbums(deletedAlbumArtist.artistId)
        assertTrue(deletedResults.isEmpty())

        val nonDeletedResults = albumArtistDao.getArtistAlbums(nonDeletedAlbumArtistId)
        assertEquals(nonDeletedAlbumArtists, nonDeletedResults)
    }

    @Test
    fun deleteNonExistent() = runTest {
        artistDao.delete("nonexistent")
    }
}
