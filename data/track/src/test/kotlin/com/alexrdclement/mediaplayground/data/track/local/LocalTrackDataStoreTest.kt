package com.alexrdclement.mediaplayground.data.track.local

import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef
import com.alexrdclement.mediaplayground.media.model.deletion.DeleteTrackPolicy
import com.alexrdclement.mediaplayground.media.model.FakeImage1
import com.alexrdclement.mediaplayground.media.model.FakeImage2
import com.alexrdclement.mediaplayground.media.model.FakeLocalClip1
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum1
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum2
import com.alexrdclement.mediaplayground.media.model.FakeLocalTrack1
import com.alexrdclement.mediaplayground.media.model.FakeLocalTrack2
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.FakeArtist1
import com.alexrdclement.mediaplayground.media.model.FakeArtist2
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
        val track = FakeLocalTrack1.copy(
            artists = artists,
            albums = persistentListOf(simpleAlbum),
        )

        fixture.putTrack(track)

        val result = fixture.localTrackDataStore.getTrack(track.id) as? AudioTrack
        assertNotNull(result)
        assertEquals(track.id, result.id)
        assertEquals(track.title, result.title)
        assertEquals(track.artists, result.artists)
        assertEquals(track.trackNumber, result.trackNumber)
        assertEquals(track.albums.first().id, result.albums.first().id)
        assertEquals(track.clips.size, result.clips.size)
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
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val track = FakeLocalTrack1.copy(
            artists = artists,
            albums = persistentListOf(simpleAlbum),
        )

        fixture.putTrack(track)

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
        val track = FakeLocalTrack1.copy(
            artists = artists,
            albums = persistentListOf(simpleAlbum),
        )

        fixture.putTrack(track)
        fixture.localTrackDataStore.updateTrackTitle(track.id, "New Title")

        val result = fixture.localTrackDataStore.getTrack(track.id)
        assertNotNull(result)
        assertEquals("New Title", result.title)
    }

    @Test
    fun updateTrackNumber_updatesTrackNumber() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val track = FakeLocalTrack1.copy(
            artists = artists,
            albums = persistentListOf(simpleAlbum),
        )

        fixture.putTrack(track)
        fixture.localTrackDataStore.updateTrackNumber(track.id, 99)

        val result = fixture.localTrackDataStore.getTrack(track.id)
        assertNotNull(result)
        assertEquals(99, result.trackNumber)
    }

    @Test
    fun updateTrackNotes_updatesNotes() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val track = FakeLocalTrack1.copy(
            artists = artists,
            albums = persistentListOf(simpleAlbum),
        )

        fixture.putTrack(track)
        fixture.localTrackDataStore.updateTrackNotes(track.id, "New notes")

        val result = fixture.localTrackDataStore.getTrack(track.id)
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
        val track1 = FakeLocalTrack1.copy(
            artists = artists,
            albums = persistentListOf(simpleAlbum),
        )
        val track2 = FakeLocalTrack2.copy(
            artists = artists,
            albums = persistentListOf(simpleAlbum),
        )

        fixture.putTrack(track1)
        fixture.putTrack(track2)

        assertNotNull(fixture.localTrackDataStore.getTrack(track1.id))
        assertNotNull(fixture.localTrackDataStore.getTrack(track2.id))
    }

    @Test
    fun deleteTrack_deletesOrphanedClips() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val track = FakeLocalTrack1.copy(artists = artists, albums = persistentListOf(simpleAlbum))

        fixture.putTrack(track)
        fixture.localTrackDataStore.delete(track.id)

        assertNull(fixture.clipDao.getClip(FakeLocalClip1.id.value))
    }

    @Test
    fun deleteTrack_doesNotDeleteClipsSharedWithOtherTrack() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val track1 = FakeLocalTrack1.copy(artists = artists, albums = persistentListOf(simpleAlbum))
        val track2 = FakeLocalTrack2.copy(artists = artists, albums = persistentListOf(simpleAlbum))

        fixture.putTrack(track1)
        fixture.putTrack(track2)
        // Share clip-1 with track2 as well
        fixture.trackClipDao.insert(
            TrackClipCrossRef(
                trackId = track2.id.value,
                clipId = FakeLocalClip1.id.value,
                startSampleInTrack = 0L,
            )
        )

        fixture.localTrackDataStore.delete(track1.id)

        assertNotNull(fixture.clipDao.getClip(FakeLocalClip1.id.value))
    }

    @Test
    fun deleteTrack_withDeleteOrphanedClipsFalse_keepsClips() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val track = FakeLocalTrack1.copy(artists = artists, albums = persistentListOf(simpleAlbum))

        fixture.putTrack(track)
        fixture.localTrackDataStore.delete(track.id, DeleteTrackPolicy(deleteOrphanedClips = false))

        assertNotNull(fixture.clipDao.getClip(FakeLocalClip1.id.value))
    }

    @Test
    fun deleteTrack_notOnlyTrackInAlbum_doesNotDeleteAlbum() = runTest {
        val artists = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists,
            images = persistentListOf(FakeImage1),
        )
        val track1 = FakeLocalTrack1.copy(
            artists = artists,
            albums = persistentListOf(simpleAlbum),
        )
        val track2 = FakeLocalTrack2.copy(
            artists = artists,
            albums = persistentListOf(simpleAlbum),
        )

        fixture.putTrack(track1)
        fixture.putTrack(track2)

        fixture.deleteTrack(track1)

        val result = fixture.localTrackDataStore.getTrack(track1.id)
        assertNull(result)
        val albumResult = fixture.localAudioAlbumDataStore.getAlbum(simpleAlbum.id)
        assertNotNull(albumResult)
        assertEquals(simpleAlbum.id, albumResult.id)
        assertEquals(1, albumResult.items.size)
        assertEquals(track2.id, albumResult.items[0].id)
        assertEquals(track2.clips.size, albumResult.items[0].clips.size)
    }

    @Test
    fun deleteTrack_onlyTrackOnAlbum_deletesAlbum() = runTest {
        val artists1 = persistentListOf(FakeArtist1)
        val simpleAlbum = FakeLocalSimpleAlbum1.copy(
            artists = artists1,
            images = persistentListOf(FakeImage1),
        )
        val track1 = FakeLocalTrack1.copy(
            artists = artists1,
            albums = persistentListOf(simpleAlbum),
        )

        fixture.putTrack(track1)

        fixture.deleteTrack(track1)

        assertNull(fixture.localTrackDataStore.getTrack(track1.id))
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
        val track1 = FakeLocalTrack1.copy(
            artists = artists1,
            albums = persistentListOf(simpleAlbum1),
        )
        val track2 = FakeLocalTrack2.copy(
            artists = artists2,
            albums = persistentListOf(simpleAlbum2),
        )

        fixture.putTrack(track1)
        fixture.putTrack(track2)

        fixture.deleteTrack(track1)

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
        val track1 = FakeLocalTrack1.copy(
            artists = artists1,
            albums = persistentListOf(simpleAlbum1),
        )

        fixture.putTrack(track1)

        fixture.deleteTrack(track1)

        for (artist in artists1) {
            assertNull(fixture.artistDao.getArtist(artist.id.value))
        }
    }
}
