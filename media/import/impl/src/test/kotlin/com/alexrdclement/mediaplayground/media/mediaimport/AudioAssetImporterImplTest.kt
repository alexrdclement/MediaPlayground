package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.FakeUri
import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import com.alexrdclement.mediaplayground.media.model.FakeLocalMediaAsset1
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum1
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class AudioAssetImporterImplTest {

    private val fixture = MediaImporterFixture()
    private val audioFileImporter = fixture.audioAssetImporter

    private val audioMetadata = FakeLocalMediaAsset1.metadata
    private val filePath = Path("/tmp/audio/test-id.mp3")

    @Test
    fun import_createsMediaAsset() = runTest {
        fixture.mediaMetadataRetriever.mediaMetadata = audioMetadata

        val result = audioFileImporter.import(uri = FakeUri)

        assertIs<Result.Success<*, *>>(result)
        val asset = (result as Result.Success).value
        assertNotNull(asset)
        assertEquals(MediaAssetSyncState.Synced, fixture.syncStateStore.states[asset.id])
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

        val result = audioFileImporter.import(uri = FakeUri)

        assertIs<Result.Failure<*, *>>(result)
    }

    @Test
    fun importTransaction_createsAndStoresMediaAsset() = runTest {
        val result = fixture.transactionRunner.run {
            audioFileImporter.importTransaction(
                filePath = filePath,
                mediaMetadata = audioMetadata,
                simpleAlbum = FakeLocalSimpleAlbum1,
                id = AudioAssetId("test-id"),
                originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/audio"),
            )
        } as? Result.Success<*, *>

        assertIs<Result.Success<*, *>>(result)
        assertNotNull(result.value)
    }

    @Test
    fun importTransaction_returnsExistingAsset_whenSameFileName() = runTest {
        val first = fixture.transactionRunner.run {
            audioFileImporter.importTransaction(
                filePath = filePath,
                mediaMetadata = audioMetadata,
                simpleAlbum = FakeLocalSimpleAlbum1,
                id = AudioAssetId("test-id"),
                originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/audio"),
            )
        } as? Result.Success<*, *>
        val second = fixture.transactionRunner.run {
            audioFileImporter.importTransaction(
                filePath = filePath,
                mediaMetadata = audioMetadata,
                simpleAlbum = FakeLocalSimpleAlbum1,
                id = AudioAssetId("test-id"),
                originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/audio"),
            )
        } as? Result.Success<*, *>

        assertIs<Result.Success<*, *>>(first)
        assertIs<Result.Success<*, *>>(second)
        val firstAsset = first.value as AudioAsset
        val secondAsset = second.value as AudioAsset
        assertEquals(firstAsset.id, secondAsset.id)
    }
}
