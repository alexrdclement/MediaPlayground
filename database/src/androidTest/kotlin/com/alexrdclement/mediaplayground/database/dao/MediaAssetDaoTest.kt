package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.dao.MediaAssetSyncStateDao
import com.alexrdclement.mediaplayground.database.fakes.FakeAudioAsset1
import com.alexrdclement.mediaplayground.database.fakes.FakeImageAsset1
import com.alexrdclement.mediaplayground.database.fakes.FakeImageAssetRecord1
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaAssetRecord1
import com.alexrdclement.mediaplayground.database.model.MediaAssetSyncStateEntity
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MediaAssetDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var mediaAssetDao: MediaAssetDao
    private lateinit var audioAssetDao: AudioAssetDao
    private lateinit var imageAssetDao: ImageAssetDao
    private lateinit var syncStateDao: MediaAssetSyncStateDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        mediaAssetDao = db.mediaAssetDao()
        audioAssetDao = db.audioAssetDao()
        imageAssetDao = db.imageAssetDao()
        syncStateDao = db.mediaAssetSyncStateDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun getMediaAsset_returnsInserted() = runTest {
        mediaAssetDao.insert(FakeMediaAssetRecord1)

        val result = mediaAssetDao.getMediaAsset(FakeMediaAssetRecord1.id)

        assertNotNull(result)
        assertEquals(FakeMediaAssetRecord1, result)
    }

    @Test
    fun getMediaAsset_returnsNullForNonExistent() = runTest {
        val result = mediaAssetDao.getMediaAsset("nonexistent")
        assertNull(result)
    }

    @Test
    fun insert_ignoresExisting() = runTest {
        mediaAssetDao.insert(FakeMediaAssetRecord1)
        mediaAssetDao.insert(FakeMediaAssetRecord1.copy(fileName = "updated.mp3"))

        val result = mediaAssetDao.getMediaAsset(FakeMediaAssetRecord1.id)
        assertEquals(FakeMediaAssetRecord1, result)
    }

    @Test
    fun delete_removesMediaAsset() = runTest {
        mediaAssetDao.insert(FakeMediaAssetRecord1)
        mediaAssetDao.delete(FakeMediaAssetRecord1.id)
        assertNull(mediaAssetDao.getMediaAsset(FakeMediaAssetRecord1.id))
    }

    @Test
    fun delete_cascadesToAudioAsset() = runTest {
        mediaAssetDao.insert(FakeMediaAssetRecord1)
        audioAssetDao.insert(FakeAudioAsset1)

        mediaAssetDao.delete(FakeMediaAssetRecord1.id)

        assertNull(audioAssetDao.getAudioAsset(FakeAudioAsset1.id))
    }

    @Test
    fun delete_cascadesToImageAsset() = runTest {
        mediaAssetDao.insert(FakeImageAssetRecord1)
        imageAssetDao.insert(FakeImageAsset1)

        mediaAssetDao.delete(FakeImageAssetRecord1.id)

        assertNull(imageAssetDao.getImage(FakeImageAsset1.id))
    }

    @Test
    fun delete_cascadesToSyncState() = runTest {
        mediaAssetDao.insert(FakeMediaAssetRecord1)
        syncStateDao.upsert(MediaAssetSyncStateEntity(FakeMediaAssetRecord1.id, MediaAssetSyncState.Synced))

        mediaAssetDao.delete(FakeMediaAssetRecord1.id)

        assertNull(syncStateDao.getFlow(FakeMediaAssetRecord1.id).first())
    }

    @Test
    fun audioAssetInsert_requiresMediaAssetParent() = runTest {
        // Inserting audio file without a media_assets parent should be blocked by FK constraint.
        // Room enforces FKs when the database is opened with foreign key support.
        // This test documents the expected constraint behavior.
        var exceptionThrown = false
        try {
            audioAssetDao.insert(FakeAudioAsset1)
        } catch (e: Exception) {
            exceptionThrown = true
        }
        // Note: FK enforcement requires foreign_keys=ON which Room enables by default.
        // In-memory tests may vary — the cascade delete test above covers the FK relationship.
        assertTrue(exceptionThrown)
    }
}
