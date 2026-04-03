package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum2
import com.alexrdclement.mediaplayground.database.fakes.FakeImage1
import com.alexrdclement.mediaplayground.database.fakes.FakeImage2
import com.alexrdclement.mediaplayground.database.fakes.FakeImage3
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.Image
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ImageDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var albumImageDao: AlbumImageDao
    private lateinit var imageDao: ImageDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
        albumImageDao = db.albumImageDao()
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
    fun getImage_returnsInserted() = runTest {
        imageDao.insert(FakeImage1)

        val result = imageDao.getImage(FakeImage1.id)

        assertImageEquals(FakeImage1, result)
    }

    @Test
    fun getImage_returnsNullForNonExistent() = runTest {
        val result = imageDao.getImage("nonexistent")
        assertNull(result)
    }

    @Test
    fun getImagesForAlbum_returnsInserted() = runTest {
        val album1 = FakeAlbum1
        albumDao.insert(album1)
        val album2 = FakeAlbum2
        albumDao.insert(album2)
        imageDao.insert(FakeImage1, FakeImage2, FakeImage3)
        albumImageDao.insert(
            AlbumImageCrossRef(albumId = album1.id, imageId = FakeImage1.id),
            AlbumImageCrossRef(albumId = album1.id, imageId = FakeImage2.id),
            AlbumImageCrossRef(albumId = album2.id, imageId = FakeImage3.id),
        )

        val album1Images = albumImageDao.getImagesForAlbumFlow(album1.id).first()
        val album2Images = albumImageDao.getImagesForAlbumFlow(album2.id).first()

        assertEquals(listOf(FakeImage1, FakeImage2), album1Images)
        assertEquals(listOf(FakeImage3), album2Images)
    }

    @Test
    fun delete_removesEntity() = runTest {
        imageDao.insert(FakeImage1)

        imageDao.delete(FakeImage1.id)

        assertNull(imageDao.getImage(FakeImage1.id))
    }

    @Test
    fun insert_ignoresExistingImage() = runTest {
        imageDao.insert(FakeImage1)
        imageDao.insert(FakeImage1.copy(notes = "Updated notes"))

        val result = imageDao.getImage(FakeImage1.id)
        assertImageEquals(FakeImage1, result)
    }

    @Test
    fun update_updatesImage() = runTest {
        imageDao.insert(FakeImage1)

        imageDao.update(FakeImage1.copy(notes = "Updated notes"))

        val result = imageDao.getImage(FakeImage1.id)
        assertNotNull(result)
        assertEquals("Updated notes", result.notes)
    }

    @Test
    fun update_doesNotDeleteAlbumImageCrossRefs() = runTest {
        albumDao.insert(FakeAlbum1)
        imageDao.insert(FakeImage1)
        albumImageDao.insert(AlbumImageCrossRef(albumId = FakeAlbum1.id, imageId = FakeImage1.id))

        imageDao.update(FakeImage1.copy(notes = "Updated notes"))

        val images = albumImageDao.getImagesForAlbumFlow(FakeAlbum1.id).first()
        assertTrue(images.isNotEmpty())
        assertEquals(FakeImage1.id, images.first().id)
    }
}
