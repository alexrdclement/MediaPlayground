package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.FakeUri
import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.media.model.FakeImage1
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum1
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

class ImageImporterImplTest {

    private val fixture = MediaImporterFixture()
    private val imageImporter = fixture.imageImporter

    private val imageMetadata = MediaMetadata.Image(
        mimeType = FakeImage1.mimeType,
        extension = FakeImage1.extension,
        widthPx = 1024,
        heightPx = 768,
        dateTimeOriginal = null,
        gpsLatitude = null,
        gpsLongitude = null,
        cameraMake = null,
        cameraModel = null,
    )

    @Test
    fun import_createsImage() = runTest {
        val result = imageImporter.import(FakeUri, imageMetadata)

        assertIs<Result.Success<*, *>>(result)
        val image = (result as Result.Success).value.image
        assertEquals(FakeImage1.mimeType, image.mimeType)
        assertEquals(MediaAssetSyncState.Synced, fixture.syncStateStore.states[image.id])
    }

    @Test
    fun copyBitmap_returnsSameImageId_forSameBytes() = runTest {
        fixture.mediaMetadataRetriever.fileMetadata = imageMetadata
        val bytes = byteArrayOf(1, 2, 3)
        val uri = MediaAssetUri.Album(FakeLocalSimpleAlbum1.id, "art.png")
        val first = imageImporter.copyBitmap(bytes, uri)
        val second = imageImporter.copyBitmap(bytes, uri)
        assertIs<Result.Success<*, *>>(first)
        assertIs<Result.Success<*, *>>(second)
        assertEquals(
            (first as Result.Success).value.id,
            (second as Result.Success).value.id,
        )
    }

    @Test
    fun copyBitmap_returnsDifferentImageId_forDifferentBytes() = runTest {
        fixture.mediaMetadataRetriever.fileMetadata = imageMetadata
        val uri = MediaAssetUri.Album(FakeLocalSimpleAlbum1.id, "art.png")
        val first = imageImporter.copyBitmap(byteArrayOf(1, 2, 3), uri)
        val second = imageImporter.copyBitmap(byteArrayOf(4, 5, 6), uri)
        assertIs<Result.Success<*, *>>(first)
        assertIs<Result.Success<*, *>>(second)
        assertNotEquals(
            (first as Result.Success).value.id,
            (second as Result.Success).value.id,
        )
    }

    @Test
    fun copyBitmap_storesImageWithProvidedUri() = runTest {
        fixture.mediaMetadataRetriever.fileMetadata = imageMetadata
        val albumId = FakeLocalSimpleAlbum1.id
        val uri = MediaAssetUri.Album(albumId, "art.png")

        val result = imageImporter.copyBitmap(byteArrayOf(1, 2, 3), uri)

        assertIs<Result.Success<*, *>>(result)
        val image = (result as Result.Success).value as Image
        assertEquals(uri, image.uri)
    }

    @Test
    fun copyFile_returnsSameImageId_forSameUri() = runTest {
        fixture.mediaMetadataRetriever.mediaMetadata = imageMetadata
        val first = imageImporter.copyFile(FakeUri, imageMetadata)
        val second = imageImporter.copyFile(FakeUri, imageMetadata)
        assertIs<Result.Success<*, *>>(first)
        assertIs<Result.Success<*, *>>(second)
        assertEquals(
            (first as Result.Success).value.id,
            (second as Result.Success).value.id,
        )
    }

    @Test
    fun importImageTransaction_createsAndStoresImage() = runTest {
        val result = fixture.transactionRunner.run {
            imageImporter.importImageTransaction(image = FakeImage1)
        }

        assertIs<Result.Success<*, *>>(result)
        assertEquals(FakeImage1.id, (result as Result.Success).value.id)
    }

    @Test
    fun importImageTransaction_setsImageMetadata() = runTest {
        val result = fixture.transactionRunner.run {
            imageImporter.importImageTransaction(image = FakeImage1)
        }

        assertIs<Result.Success<*, *>>(result)
        val image = (result as Result.Success).value
        assertEquals(FakeImage1.mimeType, image.mimeType)
        assertEquals(1024, image.widthPx)
        assertEquals(768, image.heightPx)
    }
}
