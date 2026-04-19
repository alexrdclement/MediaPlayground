package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.FakeUri
import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.media.model.FakeLocalClip1
import com.alexrdclement.mediaplayground.media.model.FakeLocalMediaAsset1
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum1
import com.alexrdclement.mediaplayground.media.model.FakeSimpleTrack1
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlinx.coroutines.flow.first
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class TrackImporterImplTest {

    private val fixture = MediaImporterFixture()
    private val trackImporter = fixture.trackImporter

    private val audioMetadata = (FakeLocalMediaAsset1.metadata as MediaMetadata.Audio)
        .copy(title = FakeSimpleTrack1.name, trackNumber = FakeSimpleTrack1.trackNumber)

    @Test
    fun import_createsTrack() = runTest {
        fixture.mediaMetadataRetriever.mediaMetadata = audioMetadata

        val result = trackImporter.import(FakeUri)

        assertIs<Result.Success<*, *>>(result)
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

        val result = trackImporter.import(FakeUri)

        assertIs<Result.Failure<*, *>>(result)
    }

    @Test
    fun importSimpleTrack_createsAndStoresTrack() = runTest {
        val result = fixture.transactionRunner.run {
            trackImporter.importSimpleTrack(
                metadata = audioMetadata,
                simpleAlbum = FakeLocalSimpleAlbum1,
                clips = setOf(FakeLocalClip1),
            )
        }

        assertIs<Result.Success<*, *>>(result)
        assertNotNull((result as Result.Success).value)
    }

    @Test
    fun importSimpleTrack_setsTrackTitle_fromMetadata() = runTest {
        val result = fixture.transactionRunner.run {
            trackImporter.importSimpleTrack(
                metadata = audioMetadata,
                simpleAlbum = FakeLocalSimpleAlbum1,
                clips = setOf(FakeLocalClip1),
            )
        }

        assertIs<Result.Success<*, *>>(result)
        assertEquals(FakeSimpleTrack1.name, (result as Result.Success).value.title)
    }

    @Test
    fun importSimpleTrack_setsTrackNumber_fromMetadata() = runTest {
        val result = fixture.transactionRunner.run {
            trackImporter.importSimpleTrack(
                metadata = audioMetadata,
                simpleAlbum = FakeLocalSimpleAlbum1,
                clips = setOf(FakeLocalClip1),
            )
        }

        assertIs<Result.Success<*, *>>(result)
        val trackId = (result as Result.Success).value.id
        val stored = fixture.trackMediaStore.getTrackFlow(trackId).first()
        assertNotNull(stored)
        assertEquals(FakeSimpleTrack1.trackNumber, stored.trackNumber)
    }
}
