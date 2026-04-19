package com.alexrdclement.mediaplayground.data.image.local

import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionScope
import com.alexrdclement.mediaplayground.database.model.MediaAsset
import com.alexrdclement.mediaplayground.database.model.MediaAssetType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.alexrdclement.mediaplayground.media.model.FakeImage1
import com.alexrdclement.mediaplayground.media.model.FakeImage2
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.testing.MainDispatcherRule
import kotlin.time.Instant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import com.alexrdclement.mediaplayground.database.model.ImageAsset as ImageEntity

class LocalImageDataStoreTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fixture: Fixture

    @BeforeTest
    fun setUp() {
        fixture = Fixture()
    }

    private class Fixture {
        val transactionScope = FakeDatabaseTransactionScope(CoroutineScope(Dispatchers.Unconfined))
        val databaseTransactionRunner = FakeDatabaseTransactionRunner(transactionScope)
        val imageDao get() = transactionScope.imageAssetDao
        val localImageDataStore = LocalImageDataStore(
            imageAssetDao = transactionScope.imageAssetDao,
            databaseTransactionRunner = databaseTransactionRunner,
        )
    }

    private suspend fun Fixture.insertFakeImage1() {
        imageDao.mediaAssetDao.insert(
            MediaAsset(
                id = FakeImage1.id.value,
                uri = MediaAssetUri.Shared("${FakeImage1.id.value}.${FakeImage1.extension}"),
                mediaAssetType = MediaAssetType.IMAGE,
                fileName = "${FakeImage1.id.value}.${FakeImage1.extension}",
                mimeType = FakeImage1.mimeType,
                extension = FakeImage1.extension,
                createdAt = Instant.DISTANT_PAST,
                modifiedAt = Instant.DISTANT_PAST,
                originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/image-1"),
            )
        )
        imageDao.insert(
            ImageEntity(
                id = FakeImage1.id.value,
                widthPx = 1024,
                heightPx = 768,
                dateTimeOriginal = null,
                gpsLatitude = null,
                gpsLongitude = null,
                cameraMake = null,
                cameraModel = null,
                notes = null,
            ),
        )
    }

    @Test
    fun getImageFlow_returnsNull_forUnknownImage() = runTest {
        val result = fixture.localImageDataStore.getImageFlow(FakeImage1.id).first()
        assertNull(result)
    }

    @Test
    fun getImageFlow_returnsImage_afterInsert() = runTest {
        fixture.insertFakeImage1()

        val result = fixture.localImageDataStore.getImageFlow(FakeImage1.id).first()
        assertNotNull(result)
        assertEquals(FakeImage1.id, result.id)
    }

    @Test
    fun putSet_insertsAllImagesWithMediaAssets() = runTest {
        fixture.localImageDataStore.put(setOf(FakeImage1, FakeImage2))

        assertNotNull(fixture.imageDao.getImage(FakeImage1.id.value))
        assertNotNull(fixture.imageDao.getImage(FakeImage2.id.value))
    }

    @Test
    fun updateImageNotes_updatesNotes() = runTest {
        fixture.insertFakeImage1()

        fixture.localImageDataStore.updateImageNotes(FakeImage1.id, "New notes")

        val result = assertNotNull(fixture.imageDao.getImage(FakeImage1.id.value))
        assertEquals("New notes", result.imageAsset.notes)
    }
}
