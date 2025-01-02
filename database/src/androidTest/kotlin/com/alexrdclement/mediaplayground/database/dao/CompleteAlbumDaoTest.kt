package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeCompleteAlbum1
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum
import com.alexrdclement.mediaplayground.database.model.id
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNull

class CompleteAlbumDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var artistDao: ArtistDao
    private lateinit var albumArtistDao: AlbumArtistDao
    private lateinit var imageDao: ImageDao
    private lateinit var trackDao: TrackDao
    private lateinit var completeAlbumDao: CompleteAlbumDao

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
        completeAlbumDao = db.completeAlbumDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    private suspend fun insertCompleteAlbum(completeAlbum: CompleteAlbum) {
        val simpleAlbum = completeAlbum.simpleAlbum
        val albumArtists = simpleAlbum.artists.map { artist ->
            AlbumArtistCrossRef(
                albumId = simpleAlbum.album.id,
                artistId = artist.id,
            )
        }
        albumDao.insert(simpleAlbum.album)
        artistDao.insert(*simpleAlbum.artists.toTypedArray())
        albumArtistDao.insert(*albumArtists.toTypedArray())
        imageDao.insert(*simpleAlbum.images.toTypedArray())
        trackDao.insert(*completeAlbum.tracks.toTypedArray())
    }

    @Test
    fun getAlbum_returnsInserted() = runTest {
        val completeAlbum = FakeCompleteAlbum1
        insertCompleteAlbum(completeAlbum)

        val result = completeAlbumDao.getAlbum(completeAlbum.id)

        assertCompleteAlbumEquals(completeAlbum, result)
    }

    @Test
    fun getAlbum_returnsNullForNonExistent() = runTest {
        val result = completeAlbumDao.getAlbum("nonexistent")
        assertNull(result)
    }
}
