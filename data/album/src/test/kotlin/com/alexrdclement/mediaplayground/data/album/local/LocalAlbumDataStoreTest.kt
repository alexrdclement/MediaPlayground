package com.alexrdclement.mediaplayground.data.album.local

import com.alexrdclement.mediaplayground.data.album.fixtures.LocalAlbumDataStoreFixture
import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.media.model.mapper.toSimpleTrack
import com.alexrdclement.mediaplayground.media.model.FakeImage1
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum1
import com.alexrdclement.mediaplayground.media.model.FakeLocalTrack1
import com.alexrdclement.mediaplayground.media.model.FakeSimpleArtist1
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

class LocalAlbumDataStoreTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fixture: LocalAlbumDataStoreFixture

    @BeforeTest
    fun setUp() {
        fixture = LocalAlbumDataStoreFixture()
    }

    @Test
    fun getAlbum_returnsNull_forUnknownId() = runTest {
        val result = fixture.localAlbumDataStore.getAlbum(FakeLocalSimpleAlbum1.id)
        assertNull(result)
    }

    @Test
    fun getAlbum_returnsAlbum_afterPutTrack() = runTest {
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

        val result = fixture.localAlbumDataStore.getAlbum(simpleAlbum.id)
        assertNotNull(result)
        assertEquals(simpleAlbum.id, result.id)
        assertEquals(listOf(track.toSimpleTrack()), result.tracks)
    }

    @Test
    fun getAlbumCountFlow_isZero_initially() = runTest {
        val count = fixture.localAlbumDataStore.getAlbumCountFlow().first()
        assertEquals(0, count)
    }

    @Test
    fun getAlbumCountFlow_incrementsOnPutTrack() = runTest {
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

        val count = fixture.localAlbumDataStore.getAlbumCountFlow().first()
        assertEquals(1, count)
    }

    @Test
    fun getAlbumFlow_emitsAlbum_afterPutTrack() = runTest {
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

        val result = fixture.localAlbumDataStore.getAlbumFlow(simpleAlbum.id).first()
        assertNotNull(result)
    }

    @Test
    fun getAlbumByTitleAndArtistId_returnsAlbum() = runTest {
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

        val result = fixture.localAlbumDataStore.getAlbumByTitleAndArtistId(
            albumTitle = simpleAlbum.name,
            artistId = FakeSimpleArtist1.id,
        )
        assertNotNull(result)
        assertEquals(simpleAlbum.id, result.id)
    }

    @Test
    fun getAlbumByTitleAndArtistId_returnsNull_forUnknown() = runTest {
        val result = fixture.localAlbumDataStore.getAlbumByTitleAndArtistId(
            albumTitle = "Unknown Album",
            artistId = "unknown-artist",
        )
        assertNull(result)
    }

    @Test
    fun updateAlbumTitle_updatesTitle() = runTest {
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
        fixture.localAlbumDataStore.updateAlbumTitle(simpleAlbum.id, "New Album Title")

        val result = fixture.localAlbumDataStore.getAlbum(simpleAlbum.id)
        assertNotNull(result)
        assertEquals("New Album Title", result.title)
    }

    @Test
    fun updateAlbumNotes_updatesNotes() = runTest {
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
        fixture.localAlbumDataStore.updateAlbumNotes(simpleAlbum.id, "New notes")

        val result = fixture.localAlbumDataStore.getAlbum(simpleAlbum.id)
        assertNotNull(result)
        assertEquals("New notes", result.notes)
    }
}
