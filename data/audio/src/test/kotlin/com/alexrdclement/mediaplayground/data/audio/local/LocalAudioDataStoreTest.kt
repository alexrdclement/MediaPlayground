package com.alexrdclement.mediaplayground.data.audio.local

import com.alexrdclement.mediaplayground.data.audio.local.fixtures.LocalAudioDataStoreFixture
import com.alexrdclement.mediaplayground.database.dao.AlbumArtistDao
import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.dao.CompleteAlbumDao
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.dao.ImageDao
import com.alexrdclement.mediaplayground.database.dao.SimpleAlbumDao
import com.alexrdclement.mediaplayground.database.dao.TrackDao
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeImage1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeImage2
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeSimpleAlbum1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeSimpleAlbum2
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeSimpleArtist1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeSimpleArtist2
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeTrack1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeTrack2
import com.alexrdclement.mediaplayground.model.audio.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.mapper.toSimpleTrack
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LocalAudioDataStoreTest {

    private lateinit var transactionRunner: DatabaseTransactionRunner
    private lateinit var artistDao: ArtistDao
    private lateinit var albumDao: AlbumDao
    private lateinit var imageDao: ImageDao
    private lateinit var trackDao: TrackDao
    private lateinit var albumArtistDao: AlbumArtistDao
    private lateinit var completeTrackDao: CompleteTrackDao
    private lateinit var completeAlbumDao: CompleteAlbumDao
    private lateinit var simpleAlbumDao: SimpleAlbumDao
    private lateinit var pathProvider: PathProvider
    private lateinit var localAudioDataStore: LocalAudioDataStore

    @BeforeTest
    fun setUp() {
        val fixture = LocalAudioDataStoreFixture()
        transactionRunner = fixture.transactionRunner
        artistDao = fixture.artistDao
        albumDao = fixture.albumDao
        imageDao = fixture.imageDao
        trackDao = fixture.trackDao
        albumArtistDao = fixture.albumArtistDao
        completeTrackDao = fixture.completeTrackDao
        completeAlbumDao = fixture.completeAlbumDao
        simpleAlbumDao = fixture.simpleAlbumDao
        pathProvider = fixture.pathProvider

        localAudioDataStore = LocalAudioDataStore(
            transactionRunner = transactionRunner,
            artistDao = artistDao,
            albumDao = albumDao,
            imageDao = imageDao,
            trackDao = trackDao,
            albumArtistDao = albumArtistDao,
            completeTrackDao = completeTrackDao,
            completeAlbumDao = completeAlbumDao,
            simpleAlbumDao = simpleAlbumDao,
            pathProvider = pathProvider,
        )
    }

    @Test
    fun getTrack_returnsPutTracks() = runTest {
        val artists = listOf(FakeSimpleArtist1)
        val simpleAlbum = FakeSimpleAlbum1.copy(
            artists = artists,
            images = listOf(FakeImage1),
        )
        val track = FakeTrack1.copy(
            artists = artists,
            simpleAlbum = simpleAlbum,
        )

        localAudioDataStore.putTrack(track)

        val actualTrack = localAudioDataStore.getTrack(track.id)

        assertEquals(track, actualTrack)
    }

    @Test
    fun deleteTrack_notOnlyTrackInAlbum_doesNotDeleteAlbum() = runTest {
        val artists = listOf(FakeSimpleArtist1)
        val simpleAlbum = FakeSimpleAlbum1.copy(
            artists = artists,
            images = listOf(FakeImage1),
        )
        val track1 = FakeTrack1.copy(
            artists = artists,
            simpleAlbum = simpleAlbum,
        )
        val track2 = FakeTrack2.copy(
            artists = artists,
            simpleAlbum = simpleAlbum,
        )

        localAudioDataStore.putTrack(track1)
        localAudioDataStore.putTrack(track2)

        localAudioDataStore.deleteTrack(track1)

        val result = localAudioDataStore.getTrack(track1.id)
        assertNull(result)
        val albumResult = localAudioDataStore.getAlbum(simpleAlbum.id)
        assertNotNull(albumResult)
        assertEquals(simpleAlbum, albumResult.toSimpleAlbum())
        assertEquals(listOf(track2.toSimpleTrack()), albumResult.tracks)
    }

    @Test
    fun deleteTrack_onlyTrackOnAlbum_deletesAlbum() = runTest {
        val artists1 = listOf(FakeSimpleArtist1)
        val simpleAlbum = FakeSimpleAlbum1.copy(
            artists = artists1,
            images = listOf(FakeImage1),
        )
        val track1 = FakeTrack1.copy(
            artists = artists1,
            simpleAlbum = simpleAlbum,
        )

        localAudioDataStore.putTrack(track1)

        localAudioDataStore.deleteTrack(track1)

        assertNull(localAudioDataStore.getTrack(track1.id))
        assertNull(localAudioDataStore.getAlbum(simpleAlbum.id))
        assertNull(albumDao.getAlbum(simpleAlbum.id.value))
        assertTrue(trackDao.getTracksForAlbum(simpleAlbum.id.value).isEmpty())
        assertTrue(imageDao.getImagesForAlbum(simpleAlbum.id.value).isEmpty())
    }

    @Test
    fun deleteTrack_notOnlyAlbumWithArtist_doesNotDeleteArtist() = runTest {
        val artists1 = listOf(FakeSimpleArtist1)
        val artists2 = listOf(FakeSimpleArtist1, FakeSimpleArtist2)
        val simpleAlbum1 = FakeSimpleAlbum1.copy(
            artists = artists1,
            images = listOf(FakeImage1),
        )
        val simpleAlbum2 = FakeSimpleAlbum2.copy(
            artists = artists2,
            images = listOf(FakeImage2),
        )
        val track1 = FakeTrack1.copy(
            artists = artists1,
            simpleAlbum = simpleAlbum1,
        )
        val track2 = FakeTrack2.copy(
            artists = artists2,
            simpleAlbum = simpleAlbum2,
        )

        localAudioDataStore.putTrack(track1)
        localAudioDataStore.putTrack(track2)

        localAudioDataStore.deleteTrack(track1)

        for (artist in artists2) {
            assertNotNull(artistDao.getArtist(artist.id))
        }
    }

    @Test
    fun deleteTrack_onlyAlbumWithArtist_deletesArtist() = runTest {
        val artists1 = listOf(FakeSimpleArtist1, FakeSimpleArtist2)
        val simpleAlbum1 = FakeSimpleAlbum1.copy(
            artists = artists1,
            images = listOf(FakeImage1),
        )
        val track1 = FakeTrack1.copy(
            artists = artists1,
            simpleAlbum = simpleAlbum1,
        )

        localAudioDataStore.putTrack(track1)

        localAudioDataStore.deleteTrack(track1)

        for (artist in artists1) {
            assertNull(artistDao.getArtist(artist.id))
        }
    }
}
