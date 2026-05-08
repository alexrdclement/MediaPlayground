package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.FakeUri
import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeAudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import com.alexrdclement.mediaplayground.media.model.FakeImage1
import com.alexrdclement.mediaplayground.media.model.FakeLocalMediaAsset1
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum1
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AudioAssetImporterImplTest {

    private val fixture = MediaImporterFixture()
    private val audioFileImporter = fixture.audioAssetImporter

    private val audioMetadata = FakeLocalMediaAsset1.metadata
    private val filePath = Path("/tmp/audio/test-id.mp3")

    @Test
    fun import_createsMediaAsset() = runTest {
        val result = audioFileImporter.import(uri = FakeUri, metadata = audioMetadata)

        assertIs<Result.Success<*, *>>(result)
        val audioAsset = (result as Result.Success).value.audioAsset
        assertEquals(MediaAssetSyncState.Synced, fixture.syncStateStore.states[audioAsset.id])
    }

    @Test
    fun import_includesEmbeddedArtwork_inAudioAsset() = runTest {
        val imageMetadata = MediaMetadata.Image(
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
        val metadata = audioMetadata.copy(embeddedPicture = byteArrayOf(1, 2, 3))
        fixture.mediaMetadataRetriever.fileMetadata = imageMetadata

        val result = audioFileImporter.import(uri = FakeUri, metadata = metadata)

        assertIs<Result.Success<*, *>>(result)
        val asset = (result as Result.Success).value.audioAsset
        assertTrue(asset.images.isNotEmpty())
        assertIs<MediaAssetUri.Album>(asset.images.first().uri)
    }

    @Test
    fun importTransaction_createsAndStoresMediaAsset() = runTest {
        val audioAsset = makeAudioAsset(
            id = AudioAssetId("test-id"),
            uri = MediaAssetUri.Album(albumId = FakeLocalSimpleAlbum1.id, fileName = filePath.name),
            originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/audio"),
            mediaMetadata = audioMetadata,
            artists = FakeLocalSimpleAlbum1.artists,
            images = FakeLocalSimpleAlbum1.images,
        )
        val result = fixture.transactionRunner.run {
            audioFileImporter.importTransaction(
                audioAsset = audioAsset,
                filePath = filePath,
            )
        } as? Result.Success<*, *>

        assertIs<Result.Success<*, *>>(result)
        assertNotNull(result.value)
    }

    @Test
    fun importTransaction_linksImagesToAlbum() = runTest {
        val audioAsset = makeAudioAsset(
            id = AudioAssetId("test-id"),
            uri = MediaAssetUri.Album(albumId = FakeLocalSimpleAlbum1.id, fileName = filePath.name),
            originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/audio"),
            mediaMetadata = audioMetadata,
            artists = FakeLocalSimpleAlbum1.artists,
            images = FakeLocalSimpleAlbum1.images,
        )
        fixture.transactionRunner.run {
            audioFileImporter.importTransaction(
                audioAsset = audioAsset,
                filePath = filePath,
            )
        }

        val linkedImageIds = fixture.albumMediaStore.albumImageLinks[FakeLocalSimpleAlbum1.id]
        assertNotNull(linkedImageIds)
        assertTrue(linkedImageIds.containsAll(FakeLocalSimpleAlbum1.images.map { it.id }))
    }

    @Test
    fun importTransaction_linksImagesToAlbum_onReimport() = runTest {
        val audioAsset = makeAudioAsset(
            id = AudioAssetId("test-id"),
            uri = MediaAssetUri.Album(albumId = FakeLocalSimpleAlbum1.id, fileName = filePath.name),
            originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/audio"),
            mediaMetadata = audioMetadata,
            artists = FakeLocalSimpleAlbum1.artists,
            images = FakeLocalSimpleAlbum1.images,
        )
        // First import
        fixture.transactionRunner.run {
            audioFileImporter.importTransaction(audioAsset = audioAsset, filePath = filePath)
        }
        // Clear links to simulate pre-fix state
        fixture.albumMediaStore.albumImageLinks.clear()

        // Re-import same file
        fixture.transactionRunner.run {
            audioFileImporter.importTransaction(audioAsset = audioAsset, filePath = filePath)
        }

        val linkedImageIds = fixture.albumMediaStore.albumImageLinks[FakeLocalSimpleAlbum1.id]
        assertNotNull(linkedImageIds)
        assertTrue(linkedImageIds.containsAll(FakeLocalSimpleAlbum1.images.map { it.id }))
    }

    @Test
    fun importTransaction_setsImageSyncStateToSynced() = runTest {
        val audioAsset = makeAudioAsset(
            id = AudioAssetId("test-id"),
            uri = MediaAssetUri.Album(albumId = FakeLocalSimpleAlbum1.id, fileName = filePath.name),
            originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/audio"),
            mediaMetadata = audioMetadata,
            artists = FakeLocalSimpleAlbum1.artists,
            images = FakeLocalSimpleAlbum1.images,
        )
        fixture.transactionRunner.run {
            audioFileImporter.importTransaction(audioAsset = audioAsset, filePath = filePath)
        }

        FakeLocalSimpleAlbum1.images.forEach { image ->
            assertEquals(MediaAssetSyncState.Synced, fixture.syncStateStore.states[image.id])
        }
    }

    @Test
    fun importTransaction_doesNotLinkImages_whenAssetHasNoImages() = runTest {
        val audioAsset = makeAudioAsset(
            id = AudioAssetId("test-id"),
            uri = MediaAssetUri.Album(albumId = FakeLocalSimpleAlbum1.id, fileName = filePath.name),
            originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/audio"),
            mediaMetadata = audioMetadata,
            artists = FakeLocalSimpleAlbum1.artists,
            images = kotlinx.collections.immutable.persistentListOf(),
        )
        fixture.transactionRunner.run {
            audioFileImporter.importTransaction(
                audioAsset = audioAsset,
                filePath = filePath,
            )
        }

        assertNull(fixture.albumMediaStore.albumImageLinks[FakeLocalSimpleAlbum1.id])
    }

    @Test
    fun importTransaction_returnsExistingAsset_whenSameFileName() = runTest {
        val audioAsset = makeAudioAsset(
            id = AudioAssetId("test-id"),
            uri = MediaAssetUri.Album(albumId = FakeLocalSimpleAlbum1.id, fileName = filePath.name),
            originUri = MediaAssetOriginUri.AndroidContentUri("content://fake/audio"),
            mediaMetadata = audioMetadata,
            artists = FakeLocalSimpleAlbum1.artists,
            images = FakeLocalSimpleAlbum1.images,
        )
        val first = fixture.transactionRunner.run {
            audioFileImporter.importTransaction(
                audioAsset = audioAsset,
                filePath = filePath,
            )
        } as? Result.Success<*, *>
        val second = fixture.transactionRunner.run {
            audioFileImporter.importTransaction(
                audioAsset = audioAsset,
                filePath = filePath,
            )
        } as? Result.Success<*, *>

        assertIs<Result.Success<*, *>>(first)
        assertIs<Result.Success<*, *>>(second)
        val firstAsset = first.value as AudioAsset
        val secondAsset = second.value as AudioAsset
        assertEquals(firstAsset.id, secondAsset.id)
    }
}
