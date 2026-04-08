package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.FakeUri
import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.media.model.FakeArtist1
import com.alexrdclement.mediaplayground.media.model.FakeLocalMediaAsset1
import com.alexrdclement.mediaplayground.media.model.FakeLocalSimpleAlbum1
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AlbumImporterImplTest {

    private val fixture = MediaImporterFixture()
    private val albumImporter = fixture.albumImporter

    private val audioMetadata = (FakeLocalMediaAsset1.metadata as MediaMetadata.Audio)
        .copy(albumTitle = FakeLocalSimpleAlbum1.name, artistName = FakeArtist1.name)

    @Test
    fun import_createsAlbum() = runTest {
        fixture.mediaMetadataRetriever.mediaMetadata = audioMetadata

        val result = albumImporter.import(FakeUri)

        assertIs<Result.Success<*, *>>(result)
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

        val result = albumImporter.import(FakeUri)

        assertIs<Result.Failure<*, *>>(result)
    }

    @Test
    fun importSimpleAlbum_createsNewAlbum_whenNotFound() = runTest {
        val result = fixture.transactionRunner.run {
            albumImporter.importSimpleAlbum(
                metadata = audioMetadata,
                source = FakeLocalSimpleAlbum1.source,
                artist = FakeArtist1,
            )
        }

        assertIs<Result.Success<*, *>>(result)
        val album = (result as Result.Success).value
        assertEquals(FakeLocalSimpleAlbum1.name, album.name)
        assertEquals(FakeArtist1.id, album.artists.first().id)
    }

    @Test
    fun importSimpleAlbum_returnsExistingAlbum_whenAlreadyImported() = runTest {
        val first = fixture.transactionRunner.run {
            albumImporter.importSimpleAlbum(
                metadata = audioMetadata,
                source = FakeLocalSimpleAlbum1.source,
                artist = FakeArtist1,
            )
        } as Result.Success
        val second = fixture.transactionRunner.run {
            albumImporter.importSimpleAlbum(
                metadata = audioMetadata,
                source = FakeLocalSimpleAlbum1.source,
                artist = FakeArtist1,
            )
        } as Result.Success

        assertEquals(first.value.id, second.value.id)
    }

    @Test
    fun importSimpleAlbum_usesUnknownAlbum_whenTitleIsNull() = runTest {
        val result = fixture.transactionRunner.run {
            albumImporter.importSimpleAlbum(
                metadata = audioMetadata.copy(albumTitle = null),
                source = FakeLocalSimpleAlbum1.source,
                artist = FakeArtist1,
            )
        }

        assertIs<Result.Success<*, *>>(result)
        assertEquals("Unknown album", (result as Result.Success).value.name)
    }
}
