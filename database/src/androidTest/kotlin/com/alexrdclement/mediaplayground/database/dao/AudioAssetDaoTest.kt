package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAudioAsset1
import com.alexrdclement.mediaplayground.database.fakes.FakeAudioAsset2
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaAssetRecord1
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaAssetRecord2
import com.alexrdclement.mediaplayground.database.model.CompleteAudioAsset
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AudioAssetDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var audioAssetDao: AudioAssetDao
    private lateinit var mediaAssetDao: MediaAssetDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        audioAssetDao = db.audioAssetDao()
        mediaAssetDao = db.mediaAssetDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    private fun assertCompleteAudioAssetEquals(expected: CompleteAudioAsset, actual: CompleteAudioAsset?) {
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun getAudioAsset_returnsInserted() = runTest {
        mediaAssetDao.insert(FakeMediaAssetRecord1)
        audioAssetDao.insert(FakeAudioAsset1)

        val result = audioAssetDao.getAudioAsset(FakeAudioAsset1.id)

        assertCompleteAudioAssetEquals(
            CompleteAudioAsset(audioAsset = FakeAudioAsset1, mediaAsset = FakeMediaAssetRecord1),
            result,
        )
    }

    @Test
    fun getAudioAsset_returnsNullForNonExistent() = runTest {
        val result = audioAssetDao.getAudioAsset("nonexistent")
        assertNull(result)
    }

    @Test
    fun getAudioAssetFlow_emitsInserted() = runTest {
        mediaAssetDao.insert(FakeMediaAssetRecord1)
        audioAssetDao.insert(FakeAudioAsset1)

        val result = audioAssetDao.getAudioAssetFlow(FakeAudioAsset1.id).first()

        assertCompleteAudioAssetEquals(
            CompleteAudioAsset(audioAsset = FakeAudioAsset1, mediaAsset = FakeMediaAssetRecord1),
            result,
        )
    }

    @Test
    fun getAudioAssetCountFlow_isZeroInitially() = runTest {
        val count = audioAssetDao.getAudioAssetCountFlow().first()
        assertEquals(0, count)
    }

    @Test
    fun getAudioAssetCountFlow_incrementsOnInsert() = runTest {
        mediaAssetDao.insert(FakeMediaAssetRecord1)
        mediaAssetDao.insert(FakeMediaAssetRecord2)
        audioAssetDao.insert(FakeAudioAsset1)
        audioAssetDao.insert(FakeAudioAsset2)

        val count = audioAssetDao.getAudioAssetCountFlow().first()
        assertEquals(2, count)
    }

    @Test
    fun delete_removesEntity() = runTest {
        mediaAssetDao.insert(FakeMediaAssetRecord1)
        audioAssetDao.insert(FakeAudioAsset1)

        audioAssetDao.delete(FakeAudioAsset1.id)

        assertNull(audioAssetDao.getAudioAsset(FakeAudioAsset1.id))
    }

    @Test
    fun insert_ignoresExisting() = runTest {
        mediaAssetDao.insert(FakeMediaAssetRecord1)
        audioAssetDao.insert(FakeAudioAsset1)
        audioAssetDao.insert(FakeAudioAsset1.copy(durationUs = 999L))

        val result = audioAssetDao.getAudioAsset(FakeAudioAsset1.id)
        assertCompleteAudioAssetEquals(
            CompleteAudioAsset(audioAsset = FakeAudioAsset1, mediaAsset = FakeMediaAssetRecord1),
            result,
        )
    }

    @Test
    fun update_updatesAudioAsset() = runTest {
        mediaAssetDao.insert(FakeMediaAssetRecord1)
        audioAssetDao.insert(FakeAudioAsset1)

        audioAssetDao.update(FakeAudioAsset1.copy(durationUs = 999L))

        val result = audioAssetDao.getAudioAsset(FakeAudioAsset1.id)
        assertNotNull(result)
        assertEquals(999L, result.audioAsset.durationUs)
    }
}
