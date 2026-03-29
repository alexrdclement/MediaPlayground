package com.alexrdclement.mediaplayground.data.track.local

import com.alexrdclement.mediaplayground.data.track.fixtures.LocalTrackDataStoreFixture
import com.alexrdclement.mediaplayground.media.model.audio.Source
import com.alexrdclement.mediaplayground.media.model.audio.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.media.model.audio.mapper.toSimpleTrack
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeImage1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeImage2
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeLocalSimpleAlbum1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeLocalSimpleAlbum2
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeLocalTrack1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeLocalTrack2
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeSimpleArtist1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeSimpleArtist2
import com.alexrdclement.testing.MainDispatcherRule
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LocalTrackDataStoreTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fixture: LocalTrackDataStoreFixture

    @BeforeTest
    fun setUp() {
        fixture = LocalTrackDataStoreFixture()
    }

    @Test
    fun putTrack_canGetTrack() = runTest {
        val artists = persistentListOf(FakeSimpleArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
            source = Source.Local,
        )
        val track = FakeLocalTrack1.copy(
            artists = artists,
            simpleAlbum = simpleAlbum,
            source = Source.Local,
        )

        fixture.putTrack(track)

        val result = fixture.localTrackDataStore.getTrack(track.id)
        assertEquals(track, result)
    }

    @Test
    fun getTrack_returnsNull_forUnknownId() = runTest {
        val result = fixture.localTrackDataStore.getTrack(FakeLocalTrack1.id)
        assertNull(result)
    }

    @Test
    fun getTrackCountFlow_isZero_initially() = runTest {
        val count = fixture.localTrackDataStore.getTrackCountFlow().first()
        assertEquals(0, count)
    }

    @Test
    fun getTrackCountFlow_incrementsOnPut() = runTest {
        val artists = persistentListOf(FakeSimpleArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
            source = Source.Local,
        )
        val track = FakeLocalTrack1.copy(
            artists = artists,
            simpleAlbum = simpleAlbum,
            source = Source.Local,
        )

        fixture.putTrack(track)

        val count = fixture.localTrackDataStore.getTrackCountFlow().first()
        assertEquals(1, count)
    }

    @Test
    fun updateTrackTitle_updatesTitle() = runTest {
        val artists = persistentListOf(FakeSimpleArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
            source = Source.Local,
        )
        val track = FakeLocalTrack1.copy(
            artists = artists,
            simpleAlbum = simpleAlbum,
            source = Source.Local,
        )

        fixture.putTrack(track)
        fixture.localTrackDataStore.updateTrackTitle(track.id, "New Title")

        val result = fixture.localTrackDataStore.getTrack(track.id)
        assertNotNull(result)
        assertEquals("New Title", result.title)
    }

    @Test
    fun updateTrackNumber_updatesTrackNumber() = runTest {
        val artists = persistentListOf(FakeSimpleArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
            source = Source.Local,
        )
        val track = FakeLocalTrack1.copy(
            artists = artists,
            simpleAlbum = simpleAlbum,
            source = Source.Local,
        )

        fixture.putTrack(track)
        fixture.localTrackDataStore.updateTrackNumber(track.id, 99)

        val result = fixture.localTrackDataStore.getTrack(track.id)
        assertNotNull(result)
        assertEquals(99, result.trackNumber)
    }

    @Test
    fun deleteTrack_notOnlyTrackInAlbum_doesNotDeleteAlbum() = runTest {
        val artists = persistentListOf(FakeSimpleArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val track1 = FakeLocalTrack1.copy(
            artists = artists,
            simpleAlbum = simpleAlbum,
            source = Source.Local,
        )
        val track2 = FakeLocalTrack2.copy(
            artists = artists,
            simpleAlbum = simpleAlbum,
            source = Source.Local,
        )

        fixture.localTrackDataStore.putTrack(track1)
        fixture.localTrackDataStore.putTrack(track2)

        fixture.localTrackDataStore.deleteTrack(track1)

        val result = fixture.localTrackDataStore.getTrack(track1.id)
        assertNull(result)
        val albumResult = fixture.localAlbumDataStore.getAlbum(simpleAlbum.id)
        assertNotNull(albumResult)
        assertEquals(simpleAlbum, albumResult.toSimpleAlbum())
        assertEquals(listOf(track2.toSimpleTrack()), albumResult.tracks)
    }

    @Test
    fun deleteTrack_onlyTrackOnAlbum_deletesAlbum() = runTest {
        val artists1 = persistentListOf(FakeSimpleArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists1,
            images = persistentListOf(FakeImage1),
            source = Source.Local,
        )
        val track1 = FakeLocalTrack1.copy(
            artists = artists1,
            simpleAlbum = simpleAlbum,
            source = Source.Local,
        )

        fixture.localTrackDataStore.putTrack(track1)

        fixture.localTrackDataStore.deleteTrack(track1)

        assertNull(fixture.localTrackDataStore.getTrack(track1.id))
        assertNull(fixture.localAlbumDataStore.getAlbum(simpleAlbum.id))
        assertNull(fixture.albumDao.getAlbum(simpleAlbum.id.value))
        assertTrue(fixture.trackDao.getTracksForAlbum(simpleAlbum.id.value).isEmpty())
        assertTrue(fixture.imageDao.getImagesForAlbum(simpleAlbum.id.value).isEmpty())
    }

    @Test
    fun deleteTrack_notOnlyAlbumWithArtist_doesNotDeleteArtist() = runTest {
        val artists1 = persistentListOf(FakeSimpleArtist1)
        val artists2 = persistentListOf(FakeSimpleArtist1, FakeSimpleArtist2)
        val simpleAlbum1 = FakeLocalSimpleAlbum1.copy(
            artists = artists1,
            images = persistentListOf(FakeImage1),
            source = Source.Local,
        )
        val simpleAlbum2 = FakeLocalSimpleAlbum2.copy(
            artists = artists2,
            images = persistentListOf(FakeImage2),
            source = Source.Local,
        )
        val track1 = FakeLocalTrack1.copy(
            artists = artists1,
            simpleAlbum = simpleAlbum1,
            source = Source.Local,
        )
        val track2 = FakeLocalTrack2.copy(
            artists = artists2,
            simpleAlbum = simpleAlbum2,
            source = Source.Local,
        )

        fixture.localTrackDataStore.putTrack(track1)
        fixture.localTrackDataStore.putTrack(track2)

        fixture.localTrackDataStore.deleteTrack(track1)

        for (artist in artists2) {
            assertNotNull(fixture.artistDao.getArtist(artist.id))
        }
    }

    @Test
    fun deleteTrack_onlyAlbumWithArtist_deletesArtist() = runTest {
        val artists1 = persistentListOf(FakeSimpleArtist1, FakeSimpleArtist2)
        val simpleAlbum1 = FakeLocalSimpleAlbum1.copy(
            artists = artists1,
            images = persistentListOf(FakeImage1),
        )
        val track1 = FakeLocalTrack1.copy(
            artists = artists1,
            simpleAlbum = simpleAlbum1,
        )

        fixture.localTrackDataStore.putTrack(track1)

        fixture.localTrackDataStore.deleteTrack(track1)

        for (artist in artists1) {
            assertNull(fixture.artistDao.getArtist(artist.id))
        }
    }
}
