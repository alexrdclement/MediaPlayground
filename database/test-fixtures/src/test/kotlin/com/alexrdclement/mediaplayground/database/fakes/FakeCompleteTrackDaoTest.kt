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
    private val mediaCollectionDao = FakeMediaCollectionDao()
    private val artistDao = FakeArtistDao()
    private val albumArtistDao = FakeAlbumArtistDao()
    private val albumImageDao = FakeAlbumImageDao()
    private val albumTrackDao = FakeAlbumTrackDao()
    private val imageDao = FakeImageAssetDao()
    private val mediaAssetDao = FakeMediaAssetDao()
    private val trackDao = FakeTrackDao()
    private val clipDao = FakeClipDao()
    private val audioFileDao = FakeAudioAssetDao(mediaAssetDao)
    private val trackClipDao = FakeTrackClipDao()

    private fun makeCompleteTrackDao(coroutineScope: CoroutineScope): FakeCompleteTrackDao {
        return FakeCompleteTrackDao(
            coroutineScope = coroutineScope,
            albumDao = albumDao,
            mediaCollectionDao = mediaCollectionDao,
            artistDao = artistDao,
            albumArtistDao = albumArtistDao,
            albumImageDao = albumImageDao,
            albumTrackDao = albumTrackDao,
            imageDao = imageDao,
            mediaAssetDao = mediaAssetDao,
            trackDao = trackDao,
            clipDao = clipDao,
            audioAssetDao = audioFileDao,
            trackClipDao = trackClipDao,
        )
    }

    private suspend fun stubCompleteTrack(completeTrack: CompleteTrack) {
        mediaCollectionDao.insert(completeTrack.mediaCollection)
        trackDao.insert(completeTrack.track)
        for (albumRef in completeTrack.albumRefs) {
            val simpleAlbum = albumRef.simpleAlbum
            mediaCollectionDao.insert(simpleAlbum.mediaCollection)
            albumDao.insert(simpleAlbum.album)
            for (artist in simpleAlbum.artists) {
                artistDao.insert(artist)
                albumArtistDao.insert(AlbumArtistCrossRef(simpleAlbum.album.id, artist.id))
            }
            for (completeImage in simpleAlbum.images) {
                mediaAssetDao.insert(completeImage.mediaAsset)
                imageDao.insert(completeImage.imageAsset)
                albumImageDao.insert(AlbumImageCrossRef(albumId = simpleAlbum.album.id, imageId = completeImage.imageAsset.id))
            }
            albumTrackDao.insert(albumRef.albumTrackCrossRef)
        }
        for (completeTrackClip in completeTrack.clips) {
            mediaAssetDao.insert(completeTrackClip.completeAudioClip.mediaAsset)
            audioFileDao.insert(completeTrackClip.completeAudioClip.audioAsset)
            clipDao.insert(completeTrackClip.completeAudioClip.clip)
            trackClipDao.insert(TrackClipCrossRef(id = completeTrackClip.trackClipCrossRef.id, trackId = completeTrack.track.id, clipId = completeTrackClip.completeAudioClip.clip.id, startSampleInTrack = completeTrackClip.trackClipCrossRef.startSampleInTrack, createdAt = completeTrackClip.trackClipCrossRef.createdAt, modifiedAt = completeTrackClip.trackClipCrossRef.modifiedAt))
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
