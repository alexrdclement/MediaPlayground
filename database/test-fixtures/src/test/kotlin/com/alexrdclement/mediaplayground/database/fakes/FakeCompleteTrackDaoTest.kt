package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef
import com.alexrdclement.mediaplayground.database.model.id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FakeCompleteTrackDaoTest {

    private val albumDao = FakeAlbumDao()
    private val artistDao = FakeArtistDao()
    private val albumArtistDao = FakeAlbumArtistDao()
    private val albumImageDao = FakeAlbumImageDao()
    private val imageDao = FakeImageFileDao()
    private val trackDao = FakeTrackDao()
    private val clipDao = FakeClipDao()
    private val audioFileDao = FakeAudioFileDao()
    private val trackClipDao = FakeTrackClipDao()

    private fun makeCompleteTrackDao(coroutineScope: CoroutineScope): FakeCompleteTrackDao {
        return FakeCompleteTrackDao(
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

    private suspend fun stubCompleteTrack(completeTrack: CompleteTrack) {
        albumDao.insert(completeTrack.album)
        for (artist in completeTrack.artists) {
            artistDao.insert(artist)
            albumArtistDao.insert(AlbumArtistCrossRef(completeTrack.album.id, artist.id))
        }
        for (image in completeTrack.images) {
            imageDao.insert(image)
            albumImageDao.insert(AlbumImageCrossRef(albumId = completeTrack.album.id, imageId = image.id))
        }
        trackDao.insert(completeTrack.track)
        for (completeTrackClip in completeTrack.clips) {
            audioFileDao.insert(completeTrackClip.completeAudioClip.audioFile)
            clipDao.insert(completeTrackClip.completeAudioClip.clip)
            trackClipDao.insert(TrackClipCrossRef(completeTrack.track.id, completeTrackClip.completeAudioClip.clip.id, completeTrackClip.trackClipCrossRef.startFrameInTrack))
        }
    }

    @Test
    fun getTrack_returnsCorrectTrack() = runTest {
        val completeTrack1 = FakeCompleteTrack1
        val completeTrack2 = FakeCompleteTrack2

        stubCompleteTrack(completeTrack1)
        stubCompleteTrack(completeTrack2)

        val completeTrackDao = makeCompleteTrackDao(coroutineScope = this)
        val actual = completeTrackDao.getTrack(completeTrack1.id)

        assertNotNull(actual)
        assertEquals(completeTrack1, actual)
    }
}
