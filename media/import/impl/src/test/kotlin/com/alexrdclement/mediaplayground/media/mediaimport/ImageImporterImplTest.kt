package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.FakeUri
import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.media.model.FakeImage1
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
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
        fixture.mediaMetadataRetriever.mediaMetadata = imageMetadata

        val result = imageImporter.import(FakeUri)

        assertIs<Result.Success<*, *>>(result)
        assertEquals(FakeImage1.mimeType, (result as Result.Success).value.mimeType)
    }

    @Test
    fun import_returnsInputFileError_whenMetadataIsNotImage() = runTest {
        // FakeMediaMetadataRetriever returns MediaMetadata.Audio by default

        val result = imageImporter.import(FakeUri)

        assertIs<Result.Failure<*, *>>(result)
    }

    @Test
    fun copyBitmap_returnsSameImageId_forSameBytes() = runTest {
        val bytes = byteArrayOf(1, 2, 3)
        val first = imageImporter.copyBitmap(bytes)
        val second = imageImporter.copyBitmap(bytes)
        assertIs<Result.Success<*, *>>(first)
        assertIs<Result.Success<*, *>>(second)
        assertEquals(
            (first as Result.Success).value.second,
            (second as Result.Success).value.second,
        )
    }

    @Test
    fun copyBitmap_returnsDifferentImageId_forDifferentBytes() = runTest {
        val first = imageImporter.copyBitmap(byteArrayOf(1, 2, 3))
        val second = imageImporter.copyBitmap(byteArrayOf(4, 5, 6))
        assertIs<Result.Success<*, *>>(first)
        assertIs<Result.Success<*, *>>(second)
        assertNotEquals(
            (first as Result.Success).value.second,
            (second as Result.Success).value.second,
        )
    }

    @Test
    fun copyFile_returnsSameImageId_forSameUri() = runTest {
        fixture.mediaMetadataRetriever.mediaMetadata = imageMetadata
        val first = imageImporter.copyFile(FakeUri, imageMetadata)
        val second = imageImporter.copyFile(FakeUri, imageMetadata)
        assertIs<Result.Success<*, *>>(first)
        assertIs<Result.Success<*, *>>(second)
        assertEquals(
            (first as Result.Success).value.second,
            (second as Result.Success).value.second,
        )
    }

    @Test
    fun importImageTransaction_createsAndStoresImage() = runTest {
        val result = fixture.transactionRunner.run {
            imageImporter.importImageTransaction(
                filePath = Path("/tmp/images/${FakeImage1.id.value}.${FakeImage1.extension}"),
                imageId = FakeImage1.id,
                mediaMetadata = imageMetadata,
            )
        }

        assertIs<Result.Success<*, *>>(result)
        assertEquals(FakeImage1.id, (result as Result.Success).value.id)
    }

    @Test
    fun importImageTransaction_setsImageMetadata() = runTest {
        val result = fixture.transactionRunner.run {
            imageImporter.importImageTransaction(
                filePath = Path("/tmp/images/${FakeImage1.id.value}.${FakeImage1.extension}"),
                imageId = FakeImage1.id,
                mediaMetadata = imageMetadata,
            )
        }

        assertIs<Result.Success<*, *>>(result)
        val image = (result as Result.Success).value
        assertEquals(FakeImage1.mimeType, image.mimeType)
        assertEquals(1024, image.widthPx)
        assertEquals(768, image.heightPx)
    }
}
