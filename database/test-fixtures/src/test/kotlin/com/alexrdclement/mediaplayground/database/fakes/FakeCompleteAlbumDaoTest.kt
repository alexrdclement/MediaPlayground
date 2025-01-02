package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum
import com.alexrdclement.mediaplayground.database.model.id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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

    @Test
    fun getAlbum_returnsCorrectAlbum() = runTest {
        stubCompleteAlbum(fakeCompleteAlbum1)
        stubCompleteAlbum(fakeCompleteAlbum2)

        val completeAlbumDao = makeCompleteAlbumDao(coroutineScope = this)
        val actual = completeAlbumDao.getAlbum(fakeCompleteAlbum1.id)

        assertNotNull(actual)
        assertEquals(fakeCompleteAlbum1, actual)
    }
}
