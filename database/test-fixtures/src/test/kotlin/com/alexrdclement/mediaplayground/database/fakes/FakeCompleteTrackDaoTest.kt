package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.Artist
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import com.alexrdclement.mediaplayground.database.model.Image
import com.alexrdclement.mediaplayground.database.model.id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FakeCompleteTrackDaoTest {

    private val albumDao =  FakeAlbumDao()
    private val artistDao =  FakeArtistDao()
    private val albumArtistDao =  FakeAlbumArtistDao()
    private val imageDao =  FakeImageDao()
    private val trackDao =  FakeTrackDao()

    // Track 1 and 2 on same album, track 3 from another
    private val fakeCompleteTrack1 = FakeCompleteTrack1
    private val fakeCompleteTrack2 = FakeCompleteTrack2
    private val fakeCompleteTrack3 = FakeCompleteTrack3

    private fun makeCompleteTrackDao(coroutineScope: CoroutineScope): FakeCompleteTrackDao {
        return FakeCompleteTrackDao(
            coroutineScope = coroutineScope,
            albumDao = albumDao,
            artistDao = artistDao,
            albumArtistDao = albumArtistDao,
            imageDao = imageDao,
            trackDao = trackDao,
        )
    }

    private suspend fun stubCompleteTrack(completeTrack: CompleteTrack) {
        albumDao.insert(completeTrack.album)
        for (artist in completeTrack.artists) {
            artistDao.insert(artist)
            albumArtistDao.insert(AlbumArtistCrossRef(completeTrack.album.id, artist.id))
        }
        for (image in completeTrack.images) {
            imageDao.insert(image)
        }
        trackDao.insert(completeTrack.track)
    }

    private suspend fun verifyDeleted(
        completeTrack: CompleteTrack,
        isAlbumDeleted: Boolean,
        deletedArtists: List<Artist> = if (isAlbumDeleted) completeTrack.artists else emptyList(),
        deletedImages: List<Image> = if (isAlbumDeleted) completeTrack.images else emptyList(),
    ) {
        if (isAlbumDeleted) {
            assertNull(albumDao.getAlbum(completeTrack.album.id))
        }
        for (artist in deletedArtists) {
            assertNull(artistDao.getArtist(artist.id))
            assertTrue(albumArtistDao.getArtistAlbums(artist.id).isEmpty())
        }
        for (image in deletedImages) {
            assertNull(imageDao.getImage(image.id))
        }
        assertNull(trackDao.getTrack(completeTrack.id))
    }

    @Test
    fun getTrack_returnsCorrectTrack() = runTest {
        stubCompleteTrack(fakeCompleteTrack1)
        stubCompleteTrack(fakeCompleteTrack2)

        val completeTrackDao = makeCompleteTrackDao(coroutineScope = this)
        val actual = completeTrackDao.getTrack(fakeCompleteTrack1.id)

        assertNotNull(actual)
        assertEquals(fakeCompleteTrack1, actual)
    }

    @Test
    fun delete_deleteIntendedTrack() = runTest {
        val deletedTrack = fakeCompleteTrack1
        val notDeletedTrack = fakeCompleteTrack2
        stubCompleteTrack(deletedTrack)
        stubCompleteTrack(notDeletedTrack)

        val completeTrackDao = makeCompleteTrackDao(coroutineScope = this)
        completeTrackDao.delete(deletedTrack.id)

        assertNull(completeTrackDao.getTrack(deletedTrack.id))
        verifyDeleted(deletedTrack, isAlbumDeleted = false)
        val actualNotDeleted = completeTrackDao.getTrack(notDeletedTrack.id)
        assertNotNull(actualNotDeleted)
        assertEquals(notDeletedTrack, actualNotDeleted)
    }

    @Test
    fun delete_deletesAlbumIfOnlyTrack() = runTest {
        val deletedTrack = fakeCompleteTrack1
        val notDeletedTrack = fakeCompleteTrack3
        stubCompleteTrack(deletedTrack)
        stubCompleteTrack(notDeletedTrack)

        val completeTrackDao = makeCompleteTrackDao(coroutineScope = this)
        completeTrackDao.delete(deletedTrack.id)

        assertNull(completeTrackDao.getTrack(deletedTrack.id))
        verifyDeleted(deletedTrack, isAlbumDeleted = true)
        val actualNotDeleted = completeTrackDao.getTrack(notDeletedTrack.id)
        assertNotNull(actualNotDeleted)
        assertEquals(notDeletedTrack, actualNotDeleted)
    }

    @Test
    fun delete_doesNotDeleteArtistIfStillReferenced() = runTest {
        val deletedTrack = fakeCompleteTrack1
        val notDeletedTrack = fakeCompleteTrack3.copy(
            artists = fakeCompleteTrack3.artists + fakeCompleteTrack1.artists,
        )
        stubCompleteTrack(deletedTrack)
        stubCompleteTrack(notDeletedTrack)

        val completeTrackDao = makeCompleteTrackDao(coroutineScope = this)
        completeTrackDao.delete(deletedTrack.id)

        assertNull(completeTrackDao.getTrack(deletedTrack.id))
        verifyDeleted(deletedTrack, isAlbumDeleted = false, deletedArtists = emptyList())
        val actualNotDeleted = completeTrackDao.getTrack(notDeletedTrack.id)
        assertNotNull(actualNotDeleted)
        assertEquals(notDeletedTrack, actualNotDeleted)
    }

    @Test
    fun deleteAll_deletesAll() = runTest {
        val tracks = listOf(fakeCompleteTrack1, fakeCompleteTrack2, fakeCompleteTrack3)
        for (track in tracks) {
            stubCompleteTrack(track)
        }

        val completeTrackDao = makeCompleteTrackDao(coroutineScope = this)
        completeTrackDao.deleteAll()

        for (track in tracks) {
            assertNull(completeTrackDao.getTrack(track.id))
            verifyDeleted(track, isAlbumDeleted = true)
        }
    }
}
