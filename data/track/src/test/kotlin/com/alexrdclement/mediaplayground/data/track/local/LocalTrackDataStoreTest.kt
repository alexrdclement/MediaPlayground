package com.alexrdclement.mediaplayground.data.track.local

import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef
import kotlin.time.Instant
import com.alexrdclement.mediaplayground.media.model.deletion.DeleteTrackPolicy
import com.alexrdclement.mediaplayground.media.model.FakeImage1
import com.alexrdclement.mediaplayground.media.model.FakeImage2
import com.alexrdclement.mediaplayground.media.model.FakeLocalClip1
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum1
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum2
import com.alexrdclement.mediaplayground.media.model.FakeLocalAlbumTrack1
import com.alexrdclement.mediaplayground.media.model.FakeLocalAlbumTrack2
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.FakeArtist1
import com.alexrdclement.mediaplayground.media.model.FakeArtist2
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
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
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack, simpleAlbum)

        val result = fixture.localTrackDataStore.getTrack(albumTrack.track.id) as? AudioTrack
        assertNotNull(result)
        assertEquals(albumTrack.track.id, result.id)
        assertEquals(albumTrack.title, result.title)
        assertEquals(albumTrack.track.items.size, result.items.size)
    }

    @Test
    fun getTrack_returnsNull_forUnknownId() = runTest {
        val result = fixture.localTrackDataStore.getTrack(FakeLocalAlbumTrack1.track.id)
        assertNull(result)
    }

    @Test
    fun getTrackCountFlow_isZero_initially() = runTest {
        val count = fixture.localTrackDataStore.getTrackCountFlow().first()
        assertEquals(0, count)
    }

    @Test
    fun getTrackCountFlow_incrementsOnPut() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack, simpleAlbum)

        val count = fixture.localTrackDataStore.getTrackCountFlow().first()
        assertEquals(1, count)
    }

    @Test
    fun updateTrackTitle_updatesTitle() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack, simpleAlbum)
        fixture.localTrackDataStore.updateTrackTitle(albumTrack.track.id, "New Title")

        val result = fixture.localTrackDataStore.getTrack(albumTrack.track.id)
        assertNotNull(result)
        assertEquals("New Title", result.title)
    }

    @Test
    fun updateTrackNotes_updatesNotes() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack, simpleAlbum)
        fixture.localTrackDataStore.updateTrackNotes(albumTrack.track.id, "New notes")

        val result = fixture.localTrackDataStore.getTrack(albumTrack.track.id)
        assertNotNull(result)
        assertEquals("New notes", result.notes)
    }

    @Test
    fun putTrack_secondTrackSameAlbum_doesNotDeleteFirstTrack() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack1 = FakeLocalAlbumTrack1.copy(
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )
        val albumTrack2 = FakeLocalAlbumTrack2.copy(
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack1, simpleAlbum)
        fixture.putTrack(albumTrack2, simpleAlbum)

        assertNotNull(fixture.localTrackDataStore.getTrack(albumTrack1.track.id))
        assertNotNull(fixture.localTrackDataStore.getTrack(albumTrack2.track.id))
    }

    @Test
    fun deleteTrack_deletesOrphanedClips() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(albumId = AudioAlbumId(simpleAlbum.id.value))

        fixture.putTrack(albumTrack, simpleAlbum)
        fixture.localTrackDataStore.delete(albumTrack.track.id)

        assertNull(fixture.clipDao.getClip(FakeLocalClip1.id.value))
    }

    @Test
    fun deleteTrack_doesNotDeleteClipsSharedWithOtherTrack() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack1 = FakeLocalAlbumTrack1.copy(albumId = AudioAlbumId(simpleAlbum.id.value))
        val albumTrack2 = FakeLocalAlbumTrack2.copy(albumId = AudioAlbumId(simpleAlbum.id.value))

        fixture.putTrack(albumTrack1, simpleAlbum)
        fixture.putTrack(albumTrack2, simpleAlbum)
        // Share clip-1 with track2 as well
        fixture.trackClipDao.insert(
            TrackClipCrossRef(
                id = "track-clip-shared",
                trackId = albumTrack2.track.id.value,
                clipId = FakeLocalClip1.id.value,
                startSampleInTrack = 0L,
                createdAt = Instant.fromEpochMilliseconds(0L),
                modifiedAt = Instant.fromEpochMilliseconds(0L),
            )
        )

        fixture.localTrackDataStore.delete(albumTrack1.track.id)

        assertNotNull(fixture.clipDao.getClip(FakeLocalClip1.id.value))
    }

    @Test
    fun deleteTrack_withDeleteOrphanedClipsFalse_keepsClips() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack = FakeLocalAlbumTrack1.copy(albumId = AudioAlbumId(simpleAlbum.id.value))

        fixture.putTrack(albumTrack, simpleAlbum)
        fixture.localTrackDataStore.delete(albumTrack.track.id, DeleteTrackPolicy(deleteOrphanedClips = false))

        assertNotNull(fixture.clipDao.getClip(FakeLocalClip1.id.value))
    }

    @Test
    fun deleteTrack_notOnlyTrackInAlbum_doesNotDeleteAlbum() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack1 = FakeLocalAlbumTrack1.copy(
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )
        val albumTrack2 = FakeLocalAlbumTrack2.copy(
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack1, simpleAlbum)
        fixture.putTrack(albumTrack2, simpleAlbum)

        fixture.deleteTrack(albumTrack1, simpleAlbum)

        val result = fixture.localTrackDataStore.getTrack(albumTrack1.track.id)
        assertNull(result)
        val albumResult = fixture.localAudioAlbumDataStore.getAlbum(simpleAlbum.id)
        assertNotNull(albumResult)
        assertEquals(simpleAlbum.id, albumResult.id)
        assertEquals(1, albumResult.items.size)
        assertEquals(albumTrack2.track.id, albumResult.items[0].track.id)
        assertEquals(albumTrack2.track.items.size, albumResult.items[0].track.items.size)
    }

    @Test
    fun deleteTrack_onlyTrackOnAlbum_deletesAlbum() = runTest {
        val artists1 = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists1,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack1 = FakeLocalAlbumTrack1.copy(
            albumId = AudioAlbumId(simpleAlbum.id.value),
        )

        fixture.putTrack(albumTrack1, simpleAlbum)

        fixture.deleteTrack(albumTrack1, simpleAlbum)

        assertNull(fixture.localTrackDataStore.getTrack(albumTrack1.track.id))
        assertNull(fixture.localAudioAlbumDataStore.getAlbum(simpleAlbum.id))
        assertNull(fixture.albumDao.getAlbum(simpleAlbum.id.value))
        assertTrue(fixture.trackDao.getTracksForAlbum(simpleAlbum.id.value).isEmpty())
        assertTrue(fixture.imageDao.images.value.isEmpty())
    }

    @Test
    fun deleteTrack_notOnlyAlbumWithArtist_doesNotDeleteArtist() = runTest {
        val artists1 = persistentListOf(FakeArtist1)
        val artists2 = persistentListOf(FakeArtist1, FakeArtist2)
        val simpleAlbum1 = FakeLocalSimpleAlbum1.copy(
            artists = artists1,
            images = persistentListOf(FakeImage1),
        )
        val simpleAlbum2 = FakeLocalSimpleAlbum2.copy(
            artists = artists2,
            images = persistentListOf(FakeImage2),
        )
        val albumTrack1 = FakeLocalAlbumTrack1.copy(
            albumId = AudioAlbumId(simpleAlbum1.id.value),
        )
        val albumTrack2 = FakeLocalAlbumTrack2.copy(
            albumId = AudioAlbumId(simpleAlbum2.id.value),
        )

        fixture.putTrack(albumTrack1, simpleAlbum1)
        fixture.putTrack(albumTrack2, simpleAlbum2)

        fixture.deleteTrack(albumTrack1, simpleAlbum1)

        for (artist in artists2) {
            assertNotNull(fixture.artistDao.getArtist(artist.id.value))
        }
    }

    @Test
    fun deleteTrack_onlyAlbumWithArtist_deletesArtist() = runTest {
        val artists1 = persistentListOf(FakeArtist1, FakeArtist2)
        val simpleAlbum1 = FakeLocalSimpleAlbum1.copy(
            artists = artists1,
            images = persistentListOf(FakeImage1),
        )
        val albumTrack1 = FakeLocalAlbumTrack1.copy(
            albumId = AudioAlbumId(simpleAlbum1.id.value),
        )

        fixture.putTrack(albumTrack1, simpleAlbum1)

        fixture.deleteTrack(albumTrack1, simpleAlbum1)

        for (artist in artists1) {
            assertNull(fixture.artistDao.getArtist(artist.id.value))
        }
    }
}
