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

class FakeCompleteAudioAlbumDaoTest {

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

    private fun makeCompleteAlbumDao(coroutineScope: CoroutineScope): FakeCompleteAlbumDao {
        return FakeCompleteAlbumDao(
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

    private suspend fun stubCompleteAlbum(completeAlbum: CompleteAlbum) {
        val simpleAlbum = completeAlbum.simpleAlbum
        mediaCollectionDao.insert(simpleAlbum.mediaCollection)
        albumDao.insert(simpleAlbum.album)
        for (artist in simpleAlbum.artists) {
            artistDao.insert(artist)
            albumArtistDao.insert(AlbumArtistCrossRef(completeAlbum.id, artist.id))
        }
        for (completeImage in simpleAlbum.images) {
            mediaAssetDao.insert(completeImage.mediaAsset)
            imageDao.insert(completeImage.imageAsset)
            albumImageDao.insert(AlbumImageCrossRef(albumId = completeAlbum.id, imageId = completeImage.imageAsset.id))
        }
        for (completeTrack in completeAlbum.tracks) {
            mediaCollectionDao.insert(completeTrack.mediaCollection)
            trackDao.insert(completeTrack.track)
            for (albumRef in completeTrack.albumRefs) {
                albumTrackDao.insert(albumRef.albumTrackCrossRef)
            }
            for (completeTrackClip in completeTrack.clips) {
                mediaAssetDao.insert(completeTrackClip.completeAudioClip.mediaAsset)
                audioFileDao.insert(completeTrackClip.completeAudioClip.audioAsset)
                clipDao.insert(completeTrackClip.completeAudioClip.clip)
                trackClipDao.insert(TrackClipCrossRef(completeTrack.track.id, completeTrackClip.completeAudioClip.clip.id, completeTrackClip.trackClipCrossRef.startSampleInTrack))
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
