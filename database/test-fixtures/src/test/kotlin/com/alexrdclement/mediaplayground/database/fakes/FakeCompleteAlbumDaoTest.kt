package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.Artist
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum
import com.alexrdclement.mediaplayground.database.model.id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FakeCompleteAlbumDaoTest {

    private val albumDao =  FakeAlbumDao()
    private val artistDao =  FakeArtistDao()
    private val albumArtistDao =  FakeAlbumArtistDao()
    private val imageDao =  FakeImageDao()
    private val trackDao =  FakeTrackDao()

    // Two albums by different artists, first with two tracks, second with one.
    private val fakeCompleteAlbum1 = FakeCompleteAlbum1
    private val fakeCompleteAlbum2 = FakeCompleteAlbum2

    private fun makeCompleteAlbumDao(coroutineScope: CoroutineScope): FakeCompleteAlbumDao {
        return FakeCompleteAlbumDao(
            coroutineScope = coroutineScope,
            albumDao = albumDao,
            artistDao = artistDao,
            albumArtistDao = albumArtistDao,
            imageDao = imageDao,
            trackDao = trackDao,
        )
    }

    private suspend fun stubCompleteAlbum(completeAlbum: CompleteAlbum) {
        albumDao.insert(completeAlbum.simpleAlbum.album)
        for (artist in completeAlbum.simpleAlbum.artists) {
            artistDao.insert(artist)
            albumArtistDao.insert(AlbumArtistCrossRef(completeAlbum.id, artist.id))
        }
        for (image in completeAlbum.simpleAlbum.images) {
            imageDao.insert(image)
        }
        for (track in completeAlbum.tracks) {
            trackDao.insert(track)
        }
    }

    private suspend fun verifyDeleted(
        completeAlbum: CompleteAlbum,
        deletedArtists: List<Artist> = completeAlbum.simpleAlbum.artists,
    ) {
        assertNull(albumDao.getAlbum(completeAlbum.id))
        for (artist in deletedArtists) {
            assertNull(artistDao.getArtist(artist.id))
            assertTrue(albumArtistDao.getArtistAlbums(artist.id).isEmpty())
        }
        for (image in completeAlbum.simpleAlbum.images) {
            assertNull(imageDao.getImage(image.id))
        }
        for (track in completeAlbum.tracks) {
            assertNull(trackDao.getTrack(track.id))
        }
    }

    @Test
    fun getAlbum_returnsCorrectAlbum() = runTest {
        stubCompleteAlbum(fakeCompleteAlbum1)
        stubCompleteAlbum(fakeCompleteAlbum2)

        val completeAlbumDao = makeCompleteAlbumDao(coroutineScope = this)
        val actual = completeAlbumDao.getAlbum(fakeCompleteAlbum1.id)

        assertNotNull(actual)
        assertEquals(fakeCompleteAlbum1, actual)
    }

    @Test
    fun delete_deleteIntendedAlbum() = runTest {
        val deletedAlbum = fakeCompleteAlbum1
        val notDeletedAlbum = fakeCompleteAlbum2
        stubCompleteAlbum(deletedAlbum)
        stubCompleteAlbum(notDeletedAlbum)

        val completeAlbumDao = makeCompleteAlbumDao(coroutineScope = this)
        completeAlbumDao.delete(deletedAlbum.id)

        val actualDeleted = completeAlbumDao.getAlbum(deletedAlbum.id)
        assertNull(actualDeleted)
        verifyDeleted(deletedAlbum)
        val actualNotDeleted = completeAlbumDao.getAlbum(notDeletedAlbum.id)
        assertNotNull(actualNotDeleted)
        assertEquals(notDeletedAlbum, actualNotDeleted)
    }

    @Test
    fun delete_doesNotDeleteArtistIfStillReferenced() = runTest {
        val deletedAlbum = fakeCompleteAlbum1
        val notDeletedAlbum = fakeCompleteAlbum2.copy(
            simpleAlbum = fakeCompleteAlbum2.simpleAlbum.copy(
                artists = fakeCompleteAlbum1.simpleAlbum.artists,
            ),
        )
        stubCompleteAlbum(deletedAlbum)
        stubCompleteAlbum(notDeletedAlbum)

        val completeAlbumDao = makeCompleteAlbumDao(coroutineScope = this)
        completeAlbumDao.delete(deletedAlbum.id)

        assertNull(completeAlbumDao.getAlbum(deletedAlbum.id))
        verifyDeleted(deletedAlbum, deletedArtists = emptyList())
        val actualNotDeleted = completeAlbumDao.getAlbum(notDeletedAlbum.id)
        assertNotNull(actualNotDeleted)
        assertEquals(notDeletedAlbum, actualNotDeleted)
    }

    @Test
    fun deleteAll_deletesAll() = runTest {
        val albums = listOf(fakeCompleteAlbum1, fakeCompleteAlbum2)
        for (album in albums) {
            stubCompleteAlbum(album)
        }

        val completeAlbumDao = makeCompleteAlbumDao(coroutineScope = this)
        completeAlbumDao.deleteAll()

        for (album in albums) {
            assertNull(completeAlbumDao.getAlbum(album.id))
            verifyDeleted(album)
        }
    }
}
