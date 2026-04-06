package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef
import com.alexrdclement.mediaplayground.database.model.id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FakeCompleteAlbumDaoTest {

    private val albumDao = FakeAlbumDao()
    private val artistDao = FakeArtistDao()
    private val albumArtistDao = FakeAlbumArtistDao()
    private val albumImageDao = FakeAlbumImageDao()
    private val imageDao = FakeImageFileDao()
    private val trackDao = FakeTrackDao()
    private val clipDao = FakeClipDao()
    private val audioFileDao = FakeAudioFileDao()
    private val trackClipDao = FakeTrackClipDao()

    private fun makeCompleteAlbumDao(coroutineScope: CoroutineScope): FakeCompleteAlbumDao {
        return FakeCompleteAlbumDao(
            coroutineScope = coroutineScope,
            albumDao = albumDao,
            artistDao = artistDao,
            albumArtistDao = albumArtistDao,
            albumImageDao = albumImageDao,
            imageDao = imageDao,
            trackDao = trackDao,
            clipDao = clipDao,
            audioFileDao = audioFileDao,
            trackClipDao = trackClipDao,
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
            albumImageDao.insert(AlbumImageCrossRef(albumId = completeAlbum.id, imageId = image.id))
        }
        for (completeTrack in completeAlbum.tracks) {
            trackDao.insert(completeTrack.track)
            for (completeTrackClip in completeTrack.clips) {
                audioFileDao.insert(completeTrackClip.completeAudioClip.audioFile)
                clipDao.insert(completeTrackClip.completeAudioClip.clip)
                trackClipDao.insert(TrackClipCrossRef(completeTrack.track.id, completeTrackClip.completeAudioClip.clip.id, completeTrackClip.trackClipCrossRef.startFrameInTrack))
            }
        }
    }

    @Test
    fun getAlbum_returnsCorrectAlbum() = runTest {
        val completeAlbum1 = FakeCompleteAlbum1
        val completeAlbum2 = FakeCompleteAlbum2
        stubCompleteAlbum(completeAlbum1)
        stubCompleteAlbum(completeAlbum2)

        val completeAlbumDao = makeCompleteAlbumDao(coroutineScope = this)
        val actual = completeAlbumDao.getAlbum(completeAlbum1.id)

        assertNotNull(actual)
        assertEquals(completeAlbum1, actual)
    }
}
