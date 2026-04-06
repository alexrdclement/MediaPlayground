package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAudioFile1
import com.alexrdclement.mediaplayground.database.fakes.FakeAudioFile2
import com.alexrdclement.mediaplayground.database.fakes.FakeClip1
import com.alexrdclement.mediaplayground.database.fakes.FakeClip2
import com.alexrdclement.mediaplayground.database.model.Clip
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ClipDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var audioFileDao: AudioFileDao
    private lateinit var clipDao: ClipDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        audioFileDao = db.audioFileDao()
        clipDao = db.clipDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    private fun assertClipEquals(expected: Clip, actual: Clip?) {
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun getClip_returnsInserted() = runTest {
        audioFileDao.insert(FakeAudioFile1)
        clipDao.insert(FakeClip1)

        val result = clipDao.getClip(FakeClip1.id)

        assertClipEquals(FakeClip1, result)
    }

    @Test
    fun getClip_returnsNullForNonExistent() = runTest {
        val result = clipDao.getClip("nonexistent")
        assertNull(result)
    }

    @Test
    fun getClipFlow_emitsInserted() = runTest {
        audioFileDao.insert(FakeAudioFile1)
        clipDao.insert(FakeClip1)

        val result = clipDao.getClipFlow(FakeClip1.id).first()

        assertClipEquals(FakeClip1, result)
    }

    @Test
    fun getClipCountFlow_isZeroInitially() = runTest {
        val count = clipDao.getClipCountFlow().first()
        assertEquals(0, count)
    }

    @Test
    fun getClipCountFlow_incrementsOnInsert() = runTest {
        audioFileDao.insert(FakeAudioFile1, FakeAudioFile2)
        clipDao.insert(FakeClip1, FakeClip2)

        val count = clipDao.getClipCountFlow().first()
        assertEquals(2, count)
    }

    @Test
    fun delete_removesClip() = runTest {
        audioFileDao.insert(FakeAudioFile1)
        clipDao.insert(FakeClip1)

        clipDao.delete(FakeClip1.id)

        assertNull(clipDao.getClip(FakeClip1.id))
    }

    @Test
    fun insert_ignoresExisting() = runTest {
        audioFileDao.insert(FakeAudioFile1)
        clipDao.insert(FakeClip1)
        clipDao.insert(FakeClip1.copy(title = "Updated Title"))

        val result = clipDao.getClip(FakeClip1.id)
        assertClipEquals(FakeClip1, result)
    }

    @Test
    fun update_updatesClip() = runTest {
        audioFileDao.insert(FakeAudioFile1)
        clipDao.insert(FakeClip1)

        clipDao.update(FakeClip1.copy(title = "Updated Title"))

        val result = clipDao.getClip(FakeClip1.id)
        assertNotNull(result)
        assertEquals("Updated Title", result.title)
    }

}
