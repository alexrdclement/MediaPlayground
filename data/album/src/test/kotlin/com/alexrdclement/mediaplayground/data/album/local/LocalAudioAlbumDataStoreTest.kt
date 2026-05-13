package com.alexrdclement.mediaplayground.data.album.local

import com.alexrdclement.mediaplayground.data.album.fixtures.LocalAlbumDataStoreFixture
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.FakeArtist1
import com.alexrdclement.mediaplayground.media.model.FakeArtist2
import com.alexrdclement.mediaplayground.media.model.FakeImage1
import com.alexrdclement.mediaplayground.media.model.FakeImage2
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum1
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum2
import com.alexrdclement.mediaplayground.media.model.FakeLocalAlbumTrack1
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

class LocalAudioAlbumDataStoreTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fixture: LocalAlbumDataStoreFixture

    @BeforeTest
    fun setUp() {
        fixture = LocalAlbumDataStoreFixture()
    }

    @Test
    fun getAlbum_returnsNull_forUnknownId() = runTest {
        val result = fixture.localAudioAlbumDataStore.getAlbum(FakeLocalSimpleAlbum1.id)
        assertNull(result)
    }

    @Test
    fun getAlbum_returnsAlbum_afterPutTrack() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(
            artists = artists,
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack, simpleAlbum)

        val result = fixture.localAudioAlbumDataStore.getAlbum(simpleAlbum.id)
        assertNotNull(result)
        assertEquals(simpleAlbum.id, result.id)
        assertEquals(1, result.items.size)
        assertEquals(albumTrack.track.id, result.items[0].track.id)
        assertEquals(albumTrack.track.clips.size, result.items[0].track.clips.size)
    }

    @Test
    fun getAlbumCountFlow_isZero_initially() = runTest {
        val count = fixture.localAudioAlbumDataStore.getAlbumCountFlow().first()
        assertEquals(0, count)
    }

    @Test
    fun getAlbumCountFlow_incrementsOnPutTrack() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(
            artists = artists,
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack, simpleAlbum)

        val count = fixture.localAudioAlbumDataStore.getAlbumCountFlow().first()
        assertEquals(1, count)
    }

    @Test
    fun getAlbumFlow_emitsAlbum_afterPutTrack() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(
            artists = artists,
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack, simpleAlbum)

        val result = fixture.localAudioAlbumDataStore.getAlbumFlow(simpleAlbum.id).first()
        assertNotNull(result)
    }

    @Test
    fun getAlbumByTitleAndArtistId_returnsAlbum() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(
            artists = artists,
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack, simpleAlbum)

        val result = fixture.localAudioAlbumDataStore.getAlbumByTitleAndArtistId(
            albumTitle = simpleAlbum.name,
            artistId = FakeArtist1.id,
        )
        assertNotNull(result)
        assertEquals(simpleAlbum.id, result.id)
    }

    @Test
    fun getAlbumByTitleAndArtistId_returnsNull_forUnknown() = runTest {
        val result = fixture.localAudioAlbumDataStore.getAlbumByTitleAndArtistId(
            albumTitle = "Unknown Album",
            artistId = ArtistId("unknown-artist"),
        )
        assertNull(result)
    }

    @Test
    fun deleteAlbum_removesAlbum() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(
            artists = artists,
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack, simpleAlbum)
        fixture.localAudioAlbumDataStore.delete(simpleAlbum.id)

        assertNull(fixture.localAudioAlbumDataStore.getAlbum(simpleAlbum.id))
    }

    @Test
    fun deleteAlbum_deletesOrphanedTrack() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(
            artists = artists,
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack, simpleAlbum)
        fixture.localAudioAlbumDataStore.delete(simpleAlbum.id)

        assertNull(fixture.localTrackDataStore.getTrack(albumTrack.track.id))
    }

    @Test
    fun deleteAlbum_preservesTrackOnOtherAlbum() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum1 = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val simpleAlbum2 = FakeLocalSimpleAlbum2.copy(
            artists = persistentListOf(FakeArtist2),
            images = persistentListOf(FakeImage2),
        )
        val albumTrack1 = FakeLocalAlbumTrack1.copy(
            artists = artists,
            albumId = AudioAlbumId(simpleAlbum1.id.value),
        )
        val albumTrack2 = FakeLocalAlbumTrack1.copy(
            artists = persistentListOf(FakeArtist2),
            albumId = AudioAlbumId(simpleAlbum2.id.value),
        )

        fixture.putTrack(albumTrack1, simpleAlbum1)
        fixture.putTrack(albumTrack2, simpleAlbum2)
        fixture.localAudioAlbumDataStore.delete(simpleAlbum1.id)

        assertNull(fixture.localAudioAlbumDataStore.getAlbum(simpleAlbum1.id))
        assertNotNull(fixture.localTrackDataStore.getTrack(albumTrack1.track.id))
    }

    @Test
    fun updateAlbumTitle_updatesTitle() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(
            artists = artists,
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack, simpleAlbum)
        fixture.localAudioAlbumDataStore.updateAlbumTitle(simpleAlbum.id, "New Album Title")

        val result = fixture.localAudioAlbumDataStore.getAlbum(simpleAlbum.id)
        assertNotNull(result)
        assertEquals("New Album Title", result.title)
    }

    @Test
    fun updateAlbumNotes_updatesNotes() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(
            artists = artists,
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack, simpleAlbum)
        fixture.localAudioAlbumDataStore.updateAlbumNotes(simpleAlbum.id, "New notes")

        val result = fixture.localAudioAlbumDataStore.getAlbum(simpleAlbum.id)
        assertNotNull(result)
        assertEquals("New notes", result.notes)
    }
}
