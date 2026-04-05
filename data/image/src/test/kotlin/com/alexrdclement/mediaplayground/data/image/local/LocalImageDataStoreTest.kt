package com.alexrdclement.mediaplayground.data.image.local

import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.data.disk.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.database.fakes.FakeImageDao
import com.alexrdclement.mediaplayground.media.model.FakeImage1
import com.alexrdclement.testing.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import com.alexrdclement.mediaplayground.database.model.Image as ImageEntity

class LocalImageDataStoreTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fixture: Fixture

    @BeforeTest
    fun setUp() {
        fixture = Fixture()
    }

    private class Fixture {
        val imageDao = FakeImageDao()
        val pathProvider: PathProvider = FakePathProvider()
        val localImageDataStore = LocalImageDataStore(imageDao, pathProvider)
    }

    @Test
    fun getImageFlow_returnsNull_forUnknownImage() = runTest {
        val result = fixture.localImageDataStore.getImageFlow(FakeImage1.id).first()
        assertNull(result)
    }

    @Test
    fun getImageFlow_returnsImage_afterInsert() = runTest {
        fixture.imageDao.insert(
            ImageEntity(
                id = FakeImage1.id.value,
                fileName = "image-1.png",
                widthPx = null,
                heightPx = null,
                dateTimeOriginal = null,
                gpsLatitude = null,
                gpsLongitude = null,
                cameraMake = null,
                cameraModel = null,
                notes = null,
            ),
        )

        val result = fixture.localImageDataStore.getImageFlow(FakeImage1.id).first()
        assertNotNull(result)
        assertEquals(FakeImage1.id, result.id)
    }

    @Test
    fun updateImageNotes_updatesNotes() = runTest {
        fixture.imageDao.insert(
            ImageEntity(
                id = FakeImage1.id.value,
                fileName = "image-1.png",
                widthPx = null,
                heightPx = null,
                dateTimeOriginal = null,
                gpsLatitude = null,
                gpsLongitude = null,
                cameraMake = null,
                cameraModel = null,
                notes = null,
            ),
        )

        fixture.localImageDataStore.updateImageNotes(FakeImage1.id, "New notes")

        val result = fixture.imageDao.getImage(FakeImage1.id.value)
        assertNotNull(result)
        assertEquals("New notes", result.notes)
    }
}
