package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.FakeUri
import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.media.model.FakeClip1
import com.alexrdclement.mediaplayground.media.model.FakeLocalMediaAsset1
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class ClipImporterImplTest {

    private val fixture = MediaImporterFixture()
    private val clipImporter = fixture.clipImporter

    private val audioMetadata = FakeLocalMediaAsset1.metadata as MediaMetadata.Audio

    // FakeClip1.timeUnit was computed from the same FakeMediaAsset1 metadata,
    // so it serves as the expected value for our computed duration.
    private val filePath = Path("/tmp/albums/album-1/song.mp3")

    @Test
    fun import_createsClip() = runTest {
        fixture.mediaMetadataRetriever.mediaMetadata = audioMetadata

        val result = clipImporter.import(FakeUri)

        assertIs<Result.Success<*, *>>(result)
        assertNotNull(fixture.clipMediaStore.get((result as Result.Success).value.id))
    }

    @Test
    fun import_returnsInputFileError_whenMetadataIsNotAudio() = runTest {
        fixture.mediaMetadataRetriever.mediaMetadata = MediaMetadata.Image(
            mimeType = "image/png",
            extension = "png",
            widthPx = 1024,
            heightPx = 768,
            dateTimeOriginal = null,
            gpsLatitude = null,
            gpsLongitude = null,
            cameraMake = null,
            cameraModel = null,
        )

        val result = clipImporter.import(FakeUri)

        assertIs<Result.Failure<*, *>>(result)
    }

    @Test
    fun importTransaction_createsAndStoresClip() = runTest {
        val result = fixture.transactionRunner.run {
            clipImporter.importTransaction(
                filePath = filePath,
                metadata = audioMetadata,
                audioFile = FakeLocalMediaAsset1,
            )
        } as? Result.Success

        assertIs<Result.Success<*, *>>(result)
        assertNotNull(fixture.clipMediaStore.get(result.value.id))
    }

    @Test
    fun importTransaction_setsCorrectTimeUnit() = runTest {
        val result = fixture.transactionRunner.run {
            clipImporter.importTransaction(
                filePath = filePath,
                metadata = audioMetadata,
                audioFile = FakeLocalMediaAsset1,
            )
        } as? Result.Success

        assertIs<Result.Success<*, *>>(result)
        assertEquals(FakeClip1.duration, result.value.duration)
    }
}
