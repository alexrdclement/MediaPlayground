package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAudioFile1
import com.alexrdclement.mediaplayground.database.fakes.FakeAudioFile2
import com.alexrdclement.mediaplayground.database.model.AudioFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AudioFileDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var audioFileDao: AudioFileDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        audioFileDao = db.audioFileDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    private fun assertAudioFileEquals(expected: AudioFile, actual: AudioFile?) {
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun getAudioFile_returnsInserted() = runTest {
        audioFileDao.insert(FakeAudioFile1)

        val result = audioFileDao.getAudioFile(FakeAudioFile1.id)

        assertAudioFileEquals(FakeAudioFile1, result)
    }

    @Test
    fun getAudioFile_returnsNullForNonExistent() = runTest {
        val result = audioFileDao.getAudioFile("nonexistent")
        assertNull(result)
    }

    @Test
    fun getAudioFileFlow_emitsInserted() = runTest {
        audioFileDao.insert(FakeAudioFile1)

        val result = audioFileDao.getAudioFileFlow(FakeAudioFile1.id).first()

        assertAudioFileEquals(FakeAudioFile1, result)
    }

    @Test
    fun getAudioFileCountFlow_isZeroInitially() = runTest {
        val count = audioFileDao.getAudioFileCountFlow().first()
        assertEquals(0, count)
    }

    @Test
    fun getAudioFileCountFlow_incrementsOnInsert() = runTest {
        audioFileDao.insert(FakeAudioFile1)
        audioFileDao.insert(FakeAudioFile2)

        val count = audioFileDao.getAudioFileCountFlow().first()
        assertEquals(2, count)
    }

    @Test
    fun delete_removesEntity() = runTest {
        audioFileDao.insert(FakeAudioFile1)

        audioFileDao.delete(FakeAudioFile1.id)

        assertNull(audioFileDao.getAudioFile(FakeAudioFile1.id))
    }

    @Test
    fun insert_ignoresExisting() = runTest {
        audioFileDao.insert(FakeAudioFile1)
        audioFileDao.insert(FakeAudioFile1.copy(fileName = "updated.mp3"))

        val result = audioFileDao.getAudioFile(FakeAudioFile1.id)
        assertAudioFileEquals(FakeAudioFile1, result)
    }

    @Test
    fun update_updatesAudioFile() = runTest {
        audioFileDao.insert(FakeAudioFile1)

        audioFileDao.update(FakeAudioFile1.copy(fileName = "updated.mp3"))

        val result = audioFileDao.getAudioFile(FakeAudioFile1.id)
        assertNotNull(result)
        assertEquals("updated.mp3", result.fileName)
    }
}
