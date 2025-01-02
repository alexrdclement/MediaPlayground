package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum2
import com.alexrdclement.mediaplayground.database.fakes.FakeTrack1
import com.alexrdclement.mediaplayground.database.fakes.FakeTrack2
import com.alexrdclement.mediaplayground.database.fakes.FakeTrack3
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class TrackDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var trackDao: TrackDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
        trackDao = db.trackDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun insert_withoutAlbum_throws() = runTest {
        val track = FakeTrack1
        assertFailsWith<SQLiteConstraintException> {
            trackDao.insert(track)
        }
    }

    @Test
    fun getTrack_returnsInserted() = runTest {
        val album = FakeAlbum1
        albumDao.insert(album)
        val track = FakeTrack1.copy(albumId = album.id)
        trackDao.insert(track)

        val result = trackDao.getTrack(track.id)

        assertTrackEquals(track, result)
    }

    @Test
    fun getTrack_returnsNullForNonExistent() = runTest {
        val result = trackDao.getTrack("nonexistent")
        assertNull(result)
    }

    @Test
    fun getTracksForAlbum_returnsInserted() = runTest {
        val album1 = FakeAlbum1
        albumDao.insert(album1)
        val album2 = FakeAlbum2
        albumDao.insert(album2)
        val track1 = FakeTrack1.copy(albumId = album1.id)
        val track2 = FakeTrack2.copy(albumId = album1.id)
        val track3 = FakeTrack3.copy(albumId = album2.id)
        trackDao.insert(track1, track2, track3)
        val expectedAlbum1Tracks = listOf(track1, track2)
        val expectedAlbum2Tracks = listOf(track3)

        val actualAlbum1Tracks = trackDao.getTracksForAlbum(album1.id)
        val actualAlbum2Tracks = trackDao.getTracksForAlbum(album2.id)

        for (track in expectedAlbum1Tracks) {
            assertTrackEquals(track, actualAlbum1Tracks.find { it.id == track.id })
        }
        for (track in expectedAlbum2Tracks) {
            assertTrackEquals(track, actualAlbum2Tracks.find { it.id == track.id })
        }
    }

    @Test
    fun delete_removesEntity() = runTest {
        val album = FakeAlbum1
        albumDao.insert(album)
        val track = FakeTrack1.copy(albumId = album.id)
        trackDao.insert(track)

        trackDao.delete(track.id)

        val resultAfterDelete = trackDao.getTrack(track.id)
        assertNull(resultAfterDelete)
    }

    @Test
    fun deleteNonExistent() = runTest {
        trackDao.delete("nonexistent")
    }

    @Test
    fun getTrackCountFlow_updatesOnInsert() = runTest {
        val album1 = FakeAlbum1
        albumDao.insert(album1)
        val album2 = FakeAlbum2
        albumDao.insert(album2)
        val track1 = FakeTrack1.copy(albumId = album1.id)
        val track2 = FakeTrack2.copy(albumId = album1.id)
        val track3 = FakeTrack3.copy(albumId = album2.id)

        trackDao.insert(track1)
        val count = trackDao.getTrackCountFlow().first()
        assertEquals(1, count)

        trackDao.insert(track2, track3)
        val count2 = trackDao.getTrackCountFlow().first()
        assertEquals(3, count2)
    }

    @Test
    fun getTrackCountFlow_updatesOnDelete() = runTest {
        val album1 = FakeAlbum1
        albumDao.insert(album1)
        val album2 = FakeAlbum2
        albumDao.insert(album2)
        val track1 = FakeTrack1.copy(albumId = album1.id)
        val track2 = FakeTrack2.copy(albumId = album1.id)
        val track3 = FakeTrack3.copy(albumId = album2.id)
        trackDao.insert(track1, track2, track3)

        trackDao.delete(track1.id)

        val count = albumDao.getAlbumCountFlow().first()
        assertEquals(2, count)
    }
}
