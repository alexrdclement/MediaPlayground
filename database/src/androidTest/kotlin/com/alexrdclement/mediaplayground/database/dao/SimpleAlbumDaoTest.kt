package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeSimpleAlbum1
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNull

class SimpleAlbumDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var artistDao: ArtistDao
    private lateinit var albumArtistDao: AlbumArtistDao
    private lateinit var imageDao: ImageDao
    private lateinit var simpleAlbumDao: SimpleAlbumDao

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
        simpleAlbumDao = db.simpleAlbumDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    private suspend fun insertSimpleAlbum(simpleAlbum: SimpleAlbum) {
        albumDao.insert(simpleAlbum.album)
        for (artists in simpleAlbum.artists) {
            artistDao.insert(artists)
        }
        val albumArtist = simpleAlbum.artists.map { artist ->
            AlbumArtistCrossRef(
                albumId = simpleAlbum.album.id,
                artistId = artist.id,
            )
        }
        albumArtistDao.insert(*albumArtist.toTypedArray())
        for (images in simpleAlbum.images) {
            imageDao.insert(images)
        }
    }

    @Test
    fun getAlbumByTitleAndArtistId_returnsInserted() = runTest {
        val simpleAlbum = FakeSimpleAlbum1
        insertSimpleAlbum(simpleAlbum)

        val result = simpleAlbumDao.getAlbumByTitleAndArtistId(
            title = simpleAlbum.album.title,
            artistId = simpleAlbum.artists.first().id,
        )

        assertSimpleAlbumEquals(simpleAlbum, result)
    }

    @Test
    fun getAlbumByTitleAndArtistId_returnsNullForNonExistent() = runTest {
        val result = simpleAlbumDao.getAlbumByTitleAndArtistId(
            title = "nonexistent",
            artistId = "nonexistent",
        )
        assertNull(result)
    }

    @Test
    fun getAlbumByTitleAndArtistId_returnsNullForNonExistentArtist() = runTest {
        val simpleAlbum = FakeSimpleAlbum1
        insertSimpleAlbum(simpleAlbum)

        val result = simpleAlbumDao.getAlbumByTitleAndArtistId(
            title = simpleAlbum.album.title,
            artistId = "nonexistent",
        )
        assertNull(result)
    }

    @Test
    fun getAlbumByTitleAndArtistId_returnsNullForNonExistentTitle() = runTest {
        val simpleAlbum = FakeSimpleAlbum1
        insertSimpleAlbum(simpleAlbum)

        val result = simpleAlbumDao.getAlbumByTitleAndArtistId(
            title = "nonexistent",
            artistId = simpleAlbum.artists.first().id,
        )
        assertNull(result)
    }
}
