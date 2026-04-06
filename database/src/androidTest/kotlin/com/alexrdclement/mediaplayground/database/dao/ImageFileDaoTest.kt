package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum2
import com.alexrdclement.mediaplayground.database.fakes.FakeImageFile1
import com.alexrdclement.mediaplayground.database.fakes.FakeImage2
import com.alexrdclement.mediaplayground.database.fakes.FakeImage3
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.ImageFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ImageFileDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var albumImageDao: AlbumImageDao
    private lateinit var imageFileDao: ImageFileDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
        albumImageDao = db.albumImageDao()
        imageFileDao = db.imageDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    private fun assertImageEquals(expected: ImageFile, actual: ImageFile?) {
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun getImage_returnsInserted() = runTest {
        imageFileDao.insert(FakeImageFile1)

        val result = imageFileDao.getImage(FakeImageFile1.id)

        assertImageEquals(FakeImageFile1, result)
    }

    @Test
    fun getImage_returnsNullForNonExistent() = runTest {
        val result = imageFileDao.getImage("nonexistent")
        assertNull(result)
    }

    @Test
    fun getImagesForAlbum_returnsInserted() = runTest {
        val album1 = FakeAlbum1
        albumDao.insert(album1)
        val album2 = FakeAlbum2
        albumDao.insert(album2)
        imageFileDao.insert(FakeImageFile1, FakeImage2, FakeImage3)
        albumImageDao.insert(
            AlbumImageCrossRef(albumId = album1.id, imageId = FakeImageFile1.id),
            AlbumImageCrossRef(albumId = album1.id, imageId = FakeImage2.id),
            AlbumImageCrossRef(albumId = album2.id, imageId = FakeImage3.id),
        )

        val album1Images = albumImageDao.getImagesForAlbumFlow(album1.id).first()
        val album2Images = albumImageDao.getImagesForAlbumFlow(album2.id).first()

        assertEquals(listOf(FakeImageFile1, FakeImage2), album1Images)
        assertEquals(listOf(FakeImage3), album2Images)
    }

    @Test
    fun delete_removesEntity() = runTest {
        imageFileDao.insert(FakeImageFile1)

        imageFileDao.delete(FakeImageFile1.id)

        assertNull(imageFileDao.getImage(FakeImageFile1.id))
    }

    @Test
    fun insert_ignoresExistingImage() = runTest {
        imageFileDao.insert(FakeImageFile1)
        imageFileDao.insert(FakeImageFile1.copy(notes = "Updated notes"))

        val result = imageFileDao.getImage(FakeImageFile1.id)
        assertImageEquals(FakeImageFile1, result)
    }

    @Test
    fun update_updatesImage() = runTest {
        imageFileDao.insert(FakeImageFile1)

        imageFileDao.update(FakeImageFile1.copy(notes = "Updated notes"))

        val result = imageFileDao.getImage(FakeImageFile1.id)
        assertNotNull(result)
        assertEquals("Updated notes", result.notes)
    }

    @Test
    fun update_doesNotDeleteAlbumImageCrossRefs() = runTest {
        albumDao.insert(FakeAlbum1)
        imageFileDao.insert(FakeImageFile1)
        albumImageDao.insert(AlbumImageCrossRef(albumId = FakeAlbum1.id, imageId = FakeImageFile1.id))

        imageFileDao.update(FakeImageFile1.copy(notes = "Updated notes"))

        val images = albumImageDao.getImagesForAlbumFlow(FakeAlbum1.id).first()
        assertTrue(images.isNotEmpty())
        assertEquals(FakeImageFile1.id, images.first().id)
    }
}
