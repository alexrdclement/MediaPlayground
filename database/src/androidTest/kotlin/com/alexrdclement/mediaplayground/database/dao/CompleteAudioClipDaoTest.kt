package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAudioFile1
import com.alexrdclement.mediaplayground.database.fakes.FakeAudioFile2
import com.alexrdclement.mediaplayground.database.fakes.FakeClip1
import com.alexrdclement.mediaplayground.database.fakes.FakeClip2
import com.alexrdclement.mediaplayground.database.fakes.FakeCompleteAudioClip1
import com.alexrdclement.mediaplayground.database.model.CompleteAudioClip
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CompleteAudioClipDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var audioFileDao: AudioFileDao
    private lateinit var clipDao: ClipDao
    private lateinit var completeAudioClipDao: CompleteAudioClipDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        audioFileDao = db.audioFileDao()
        clipDao = db.clipDao()
        completeAudioClipDao = db.completeAudioClipDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    private fun assertCompleteAudioClipEquals(expected: CompleteAudioClip, actual: CompleteAudioClip?) {
        assertNotNull(actual)
        assertEquals(expected.clip, actual.clip)
        assertEquals(expected.audioFile, actual.audioFile)
    }

    private suspend fun insertCompleteAudioClip(completeAudioClip: CompleteAudioClip) {
        audioFileDao.insert(completeAudioClip.audioFile)
        clipDao.insert(completeAudioClip.clip)
    }

    @Test
    fun getClip_returnsInserted() = runTest {
        insertCompleteAudioClip(FakeCompleteAudioClip1)

        val result = completeAudioClipDao.getClip(FakeClip1.id)

        assertCompleteAudioClipEquals(FakeCompleteAudioClip1, result)
    }

    @Test
    fun getClip_returnsNullForNonExistent() = runTest {
        val result = completeAudioClipDao.getClip("nonexistent")
        assertNull(result)
    }

    @Test
    fun getClipFlow_emitsInserted() = runTest {
        insertCompleteAudioClip(FakeCompleteAudioClip1)

        val result = completeAudioClipDao.getClipFlow(FakeClip1.id).first()

        assertCompleteAudioClipEquals(FakeCompleteAudioClip1, result)
    }

    @Test
    fun getClipCountFlow_isZeroInitially() = runTest {
        val count = completeAudioClipDao.getClipCountFlow().first()
        assertEquals(0, count)
    }

    @Test
    fun getClipCountFlow_incrementsOnInsert() = runTest {
        audioFileDao.insert(FakeAudioFile1, FakeAudioFile2)
        clipDao.insert(FakeClip1, FakeClip2)

        val count = completeAudioClipDao.getClipCountFlow().first()
        assertEquals(2, count)
    }
}
