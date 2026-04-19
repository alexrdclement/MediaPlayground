package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.database.fakes.FakeAudioAsset1
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaCollection1
import com.alexrdclement.mediaplayground.database.fakes.FakeAudioAsset2
import com.alexrdclement.mediaplayground.database.fakes.FakeClip1
import com.alexrdclement.mediaplayground.database.fakes.FakeClip2
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaAssetRecord1
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaAssetRecord2
import com.alexrdclement.mediaplayground.database.fakes.FakeTrack1
import com.alexrdclement.mediaplayground.database.fakes.FakeTrack2
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TrackClipDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var trackDao: TrackDao
    private lateinit var audioAssetDao: AudioAssetDao
    private lateinit var clipDao: ClipDao
    private lateinit var trackClipDao: TrackClipDao
    private lateinit var mediaAssetDao: MediaAssetDao
    private lateinit var mediaCollectionDao: MediaCollectionDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
        trackDao = db.trackDao()
        audioAssetDao = db.audioAssetDao()
        clipDao = db.clipDao()
        trackClipDao = db.trackClipDao()
        mediaAssetDao = db.mediaAssetDao()
        mediaCollectionDao = db.mediaCollectionDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    private suspend fun insertPrerequisites() {
        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(FakeAlbum1)
        mediaAssetDao.insert(FakeMediaAssetRecord1, FakeMediaAssetRecord2)
        audioAssetDao.insert(FakeAudioAsset1, FakeAudioAsset2)
        clipDao.insert(FakeClip1, FakeClip2)
        trackDao.insert(FakeTrack1, FakeTrack2)
    }

    @Test
    fun getClipIdsForTrack_returnsInserted() = runTest {
        insertPrerequisites()
        trackClipDao.insert(
            TrackClipCrossRef(
                trackId = FakeTrack1.id,
                clipId = FakeClip1.id,
                startFrameInTrack = 0L,
            )
        )

        val result = trackClipDao.getClipIdsForTrack(FakeTrack1.id)

        assertEquals(listOf(FakeClip1.id), result)
    }

    @Test
    fun getClipIdsForTrack_returnsEmptyForNonExistent() = runTest {
        val result = trackClipDao.getClipIdsForTrack("nonexistent")
        assertTrue(result.isEmpty())
    }

    @Test
    fun getClipIdsForTrack_returnsOnlyClipsForRequestedTrack() = runTest {
        insertPrerequisites()
        trackClipDao.insert(
            TrackClipCrossRef(trackId = FakeTrack1.id, clipId = FakeClip1.id, startFrameInTrack = 0L),
            TrackClipCrossRef(trackId = FakeTrack2.id, clipId = FakeClip2.id, startFrameInTrack = 0L),
        )

        val result = trackClipDao.getClipIdsForTrack(FakeTrack1.id)

        assertEquals(listOf(FakeClip1.id), result)
    }

    @Test
    fun insert_ignoresExisting() = runTest {
        insertPrerequisites()
        val crossRef = TrackClipCrossRef(
            trackId = FakeTrack1.id,
            clipId = FakeClip1.id,
            startFrameInTrack = 0L,
        )
        trackClipDao.insert(crossRef)
        trackClipDao.insert(crossRef.copy(startFrameInTrack = 100L))

        val result = trackClipDao.getClipIdsForTrack(FakeTrack1.id)
        assertEquals(1, result.size)
    }
}
