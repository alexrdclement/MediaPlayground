package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.FakeUri
import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.media.model.FakeLocalMediaAsset1
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum1
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class AudioFileImporterImplTest {

    private val fixture = MediaImporterFixture()
    private val audioFileImporter = fixture.audioFileImporter

    private val audioMetadata = FakeLocalMediaAsset1.metadata as MediaMetadata.Audio
    private val filePath = Path("/tmp/albums/${FakeLocalSimpleAlbum1.id.value}/song.mp3")

    @Test
    fun import_createsMediaAsset() = runTest {
        fixture.mediaMetadataRetriever.mediaMetadata = audioMetadata

        val result = audioFileImporter.import(
            uri = FakeUri,
            destinationDir = Path("/tmp"),
            source = FakeLocalSimpleAlbum1.source,
        )

        assertIs<Result.Success<*, *>>(result)
        assertNotNull((result as Result.Success).value)
    }

    @Test
    fun import_returnsInputFileError_whenMetadataIsNotAudio() = runTest {
        fixture.mediaMetadataRetriever.mediaMetadata = MediaMetadata.Image(
            mimeType = "image/png",
            extension = "png",
            widthPx = null,
            heightPx = null,
            dateTimeOriginal = null,
            gpsLatitude = null,
            gpsLongitude = null,
            cameraMake = null,
            cameraModel = null,
        )

        val result = audioFileImporter.import(
            uri = FakeUri,
            destinationDir = Path("/tmp"),
            source = FakeLocalSimpleAlbum1.source,
        )

        assertIs<Result.Failure<*, *>>(result)
    }

    @Test
    fun importTransaction_createsAndStoresMediaAsset() = runTest {
        val result = fixture.transactionRunner.run {
            audioFileImporter.importTransaction(
                filePath = filePath,
                mediaMetadata = audioMetadata,
                source = FakeLocalSimpleAlbum1.source,
                simpleAlbum = FakeLocalSimpleAlbum1,
            )
        } as? Result.Success

        assertIs<Result.Success<*, *>>(result)
        assertNotNull(result.value)
    }

    @Test
    fun importTransaction_returnsExistingAsset_whenSameFileName() = runTest {
        val first = fixture.transactionRunner.run {
            audioFileImporter.importTransaction(
                filePath = filePath,
                mediaMetadata = audioMetadata,
                source = FakeLocalSimpleAlbum1.source,
                simpleAlbum = FakeLocalSimpleAlbum1,
            )
        } as? Result.Success
        val second = fixture.transactionRunner.run {
            audioFileImporter.importTransaction(
                filePath = filePath,
                mediaMetadata = audioMetadata,
                source = FakeLocalSimpleAlbum1.source,
                simpleAlbum = FakeLocalSimpleAlbum1,
            )
        } as? Result.Success

        assertIs<Result.Success<*, *>>(first)
        assertIs<Result.Success<*, *>>(second)
        assertEquals(first.value.id, second.value.id)
    }
}
