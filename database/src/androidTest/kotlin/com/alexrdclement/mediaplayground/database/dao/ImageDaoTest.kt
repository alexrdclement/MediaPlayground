package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum2
import com.alexrdclement.mediaplayground.database.fakes.FakeImage1
import com.alexrdclement.mediaplayground.database.fakes.FakeImage2
import com.alexrdclement.mediaplayground.database.fakes.FakeImage3
import com.alexrdclement.mediaplayground.database.model.Image
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ImageDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var imageDao: ImageDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
        imageDao = db.imageDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    private fun assertImageEquals(expected: Image, actual: Image?) {
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun insert_withoutAlbum_throws() = runTest {
        val image = FakeImage1
        assertFailsWith<SQLiteConstraintException> {
            imageDao.insert(image)
        }
    }

    @Test
    fun get_returnsInserted() = runTest {
        val album = FakeAlbum1
        albumDao.insert(album)
        val image = FakeImage1.copy(albumId = album.id)
        imageDao.insert(image)

        val result = imageDao.getImage(image.id)

        assertImageEquals(image, result)
    }

    @Test
    fun get_returnsNullForNonExistent() = runTest {
        val result = imageDao.getImage("nonexistent")
        assertNull(result)
    }

    @Test
    fun getImagesForAlbum_returnsInserted() = runTest {
        val album1 = FakeAlbum1
        albumDao.insert(album1)
        val album2 = FakeAlbum2
        albumDao.insert(album2)
        val image1 = FakeImage1.copy(albumId = album1.id)
        val image2 = FakeImage2.copy(albumId = album1.id)
        val image3 = FakeImage3.copy(albumId = album2.id)
        imageDao.insert(image1, image2, image3)
        val expectedAlbum1Images = listOf(image1, image2)
        val expectedAlbum2Images = listOf(image3)

        val actualAlbum1Images = imageDao.getImagesForAlbum(album1.id)
        val actualAlbum2Images = imageDao.getImagesForAlbum(album2.id)

        assertEquals(expectedAlbum1Images, actualAlbum1Images)
        assertEquals(expectedAlbum2Images, actualAlbum2Images)
    }

    @Test
    fun delete_removesEntity() = runTest {
        val album = FakeAlbum1
        albumDao.insert(album)
        val image = FakeImage1
        imageDao.insert(image)

        imageDao.delete(image.id)

        val resultAfterDelete = imageDao.getImage(image.id)
        assertNull(resultAfterDelete)
    }

    @Test
    fun deleteNonExistent() = runTest {
        imageDao.delete("nonexistent")
    }
}
