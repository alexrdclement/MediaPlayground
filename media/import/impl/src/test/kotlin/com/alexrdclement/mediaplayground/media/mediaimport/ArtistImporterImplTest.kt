package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.FakeUri
import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.media.model.FakeArtist1
import com.alexrdclement.mediaplayground.media.model.FakeLocalMediaAsset1
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ArtistImporterImplTest {

    private val fixture = MediaImporterFixture()
    private val artistImporter = fixture.artistImporter

    private val audioMetadata = (FakeLocalMediaAsset1.metadata as MediaMetadata.Audio)
        .copy(artistName = FakeArtist1.name)

    @Test
    fun import_createsArtist() = runTest {
        fixture.mediaMetadataRetriever.mediaMetadata = audioMetadata

        val result = artistImporter.import(FakeUri)

        assertIs<Result.Success<*, *>>(result)
        assertEquals(FakeArtist1.name, (result as Result.Success).value.name)
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

        val result = artistImporter.import(FakeUri)

        assertIs<Result.Failure<*, *>>(result)
    }

    @Test
    fun importTransaction_createsNewArtist_whenNotFound() = runTest {
        val result = fixture.transactionRunner.run {
            artistImporter.importTransaction(audioMetadata)
        }

        assertIs<Result.Success<*, *>>(result)
        assertEquals(FakeArtist1.name, (result as Result.Success).value.name)
    }

    @Test
    fun importTransaction_returnsExistingArtist_whenAlreadyImported() = runTest {
        val first = fixture.transactionRunner.run {
            artistImporter.importTransaction(audioMetadata)
        } as Result.Success
        val second = fixture.transactionRunner.run {
            artistImporter.importTransaction(audioMetadata)
        } as Result.Success

        assertEquals(first.value.id, second.value.id)
    }

    @Test
    fun importTransaction_usesUnknownArtist_whenNameIsNull() = runTest {
        val result = fixture.transactionRunner.run {
            artistImporter.importTransaction(audioMetadata.copy(artistName = null))
        }

        assertIs<Result.Success<*, *>>(result)
        assertEquals("Unknown artist", (result as Result.Success).value.name)
    }
}
