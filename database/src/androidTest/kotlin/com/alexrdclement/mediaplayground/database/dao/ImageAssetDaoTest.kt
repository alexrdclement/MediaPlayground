package com.alexrdclement.mediaplayground.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum1
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbum2
import com.alexrdclement.mediaplayground.database.fakes.FakeImageAsset1
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaCollection1
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaCollection2
import com.alexrdclement.mediaplayground.database.fakes.FakeImage2
import com.alexrdclement.mediaplayground.database.fakes.FakeImage3
import com.alexrdclement.mediaplayground.database.fakes.FakeImageAssetRecord1
import com.alexrdclement.mediaplayground.database.fakes.FakeImageAssetRecord2
import com.alexrdclement.mediaplayground.database.fakes.FakeImageAssetRecord3
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteImageAsset
import com.alexrdclement.mediaplayground.database.model.ImageAsset
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ImageAssetDaoTest {

    private lateinit var db: MediaPlaygroundDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var albumImageDao: AlbumImageDao
    private lateinit var imageAssetDao: ImageAssetDao
    private lateinit var mediaAssetDao: MediaAssetDao
    private lateinit var mediaCollectionDao: MediaCollectionDao

    @Before
    fun create() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MediaPlaygroundDatabase::class.java)
            .build()
        albumDao = db.albumDao()
        albumImageDao = db.albumImageDao()
        imageAssetDao = db.imageAssetDao()
        mediaAssetDao = db.mediaAssetDao()
        mediaCollectionDao = db.mediaCollectionDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    private fun assertImageEquals(expected: ImageAsset, actual: CompleteImageAsset?) {
        assertNotNull(actual)
        assertEquals(expected, actual.imageAsset)
    }

    @Test
    fun getImage_returnsInserted() = runTest {
        mediaAssetDao.insert(FakeImageAssetRecord1)
        imageAssetDao.insert(FakeImageAsset1)

        val result = imageAssetDao.getImage(FakeImageAsset1.id)

        assertImageEquals(FakeImageAsset1, result)
    }

    @Test
    fun getImage_returnsNullForNonExistent() = runTest {
        val result = imageAssetDao.getImage("nonexistent")
        assertNull(result)
    }

    @Test
    fun getImagesForAlbum_returnsInserted() = runTest {
        val album1 = FakeAlbum1
        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(album1)
        val album2 = FakeAlbum2
        mediaCollectionDao.insert(FakeMediaCollection2)
        albumDao.insert(album2)
        mediaAssetDao.insert(FakeImageAssetRecord1, FakeImageAssetRecord2, FakeImageAssetRecord3)
        imageAssetDao.insert(FakeImageAsset1, FakeImage2, FakeImage3)
        albumImageDao.insert(
            AlbumImageCrossRef(albumId = album1.id, imageId = FakeImageAsset1.id),
            AlbumImageCrossRef(albumId = album1.id, imageId = FakeImage2.id),
            AlbumImageCrossRef(albumId = album2.id, imageId = FakeImage3.id),
        )

        val album1Images = albumImageDao.getImagesForAlbumFlow(album1.id).first()
        val album2Images = albumImageDao.getImagesForAlbumFlow(album2.id).first()

        assertEquals(listOf(FakeImageAsset1, FakeImage2), album1Images)
        assertEquals(listOf(FakeImage3), album2Images)
    }

    @Test
    fun delete_removesEntity() = runTest {
        mediaAssetDao.insert(FakeImageAssetRecord1)
        imageAssetDao.insert(FakeImageAsset1)

        imageAssetDao.delete(FakeImageAsset1.id)

        assertNull(imageAssetDao.getImage(FakeImageAsset1.id))
    }

    @Test
    fun insert_ignoresExistingImage() = runTest {
        mediaAssetDao.insert(FakeImageAssetRecord1)
        imageAssetDao.insert(FakeImageAsset1)
        imageAssetDao.insert(FakeImageAsset1.copy(notes = "Updated notes"))

        val result = imageAssetDao.getImage(FakeImageAsset1.id)
        assertImageEquals(FakeImageAsset1, result)
    }

    @Test
    fun update_updatesImage() = runTest {
        mediaAssetDao.insert(FakeImageAssetRecord1)
        imageAssetDao.insert(FakeImageAsset1)

        imageAssetDao.update(FakeImageAsset1.copy(notes = "Updated notes"))

        val result = assertNotNull(imageAssetDao.getImage(FakeImageAsset1.id))
        assertEquals("Updated notes", result.imageAsset.notes)
    }

    @Test
    fun update_doesNotDeleteAlbumImageCrossRefs() = runTest {
        mediaCollectionDao.insert(FakeMediaCollection1)
        albumDao.insert(FakeAlbum1)
        mediaAssetDao.insert(FakeImageAssetRecord1)
        imageAssetDao.insert(FakeImageAsset1)
        albumImageDao.insert(AlbumImageCrossRef(albumId = FakeAlbum1.id, imageId = FakeImageAsset1.id))

        imageAssetDao.update(FakeImageAsset1.copy(notes = "Updated notes"))

        val images = albumImageDao.getImagesForAlbumFlow(FakeAlbum1.id).first()
        assertTrue(images.isNotEmpty())
        assertEquals(FakeImageAsset1.id, images.first().id)
    }
}
