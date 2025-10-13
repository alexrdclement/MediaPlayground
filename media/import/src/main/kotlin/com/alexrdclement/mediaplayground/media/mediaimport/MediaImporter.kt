package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeSimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeSimpleArtist
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeTrack
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.Source
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.mapFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import java.util.UUID
import javax.inject.Inject

class MediaImporter @Inject constructor(
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val fileWriter: FileWriter,
) {

    private companion object {
        const val MediaThumbnailFileName = "thumbnail.png"
    }

    private data class ImportData(
        val uri: Uri,
        val mediaMetadata: MediaMetadata,
        val simpleAlbum: SimpleAlbum,
    )

    /**
     * Import audio file as a track from disk.
     * @param uri: URI of the file as given by the document provider (not MediaStore).
     */
    suspend fun importTrackFromDisk(
        uri: Uri,
        getImportDir: (AlbumId) -> Path,
        getArtistByName: suspend (String) -> SimpleArtist?,
        getAlbumByTitleAndArtistId: suspend (String, String) -> SimpleAlbum?,
        saveTrack: suspend (Track) -> Unit,
    ): Result<Track, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val importData = getImportData(
                uri = uri,
                getImportDir = getImportDir,
                getArtistByName = getArtistByName,
                getAlbumByTitleAndArtistId = getAlbumByTitleAndArtistId,
            )
            import(
                importData = importData,
                getImportDir = getImportDir,
                saveTrack = saveTrack,
            )
        } catch (e: Throwable) {
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    suspend fun importTracksFromDisk(
        uris: List<Uri>,
        getImportDir: (AlbumId) -> Path,
        getArtistByName: suspend (String) -> SimpleArtist?,
        getAlbumByTitleAndArtistId: suspend (String, String) -> SimpleAlbum?,
        saveTrack: suspend (Track) -> Unit,
    ): Map<Uri, Result<Track, MediaImportError>> {
        // Album directory is only correct after a track has been saved. Import sequentially.
        return uris.associateWith { uri ->
            val importData = getImportData(
                uri = uri,
                getImportDir = getImportDir,
                getArtistByName = getArtistByName,
                getAlbumByTitleAndArtistId = getAlbumByTitleAndArtistId,
            )
            import(
                importData = importData,
                getImportDir = getImportDir,
                saveTrack = saveTrack,
            )
        }
    }

    private suspend fun getImportData(
        uri: Uri,
        getImportDir: (AlbumId) -> Path,
        getArtistByName: suspend (String) -> SimpleArtist?,
        getAlbumByTitleAndArtistId: suspend (String, String) -> SimpleAlbum?,
    ): ImportData {
        val mediaMetadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri)
        val simpleArtist = makeSimpleArtist(mediaMetadata, getArtistByName)
        val simpleAlbum = makeSimpleAlbum(
            mediaMetadata = mediaMetadata,
            simpleArtist = simpleArtist,
            getImageFilePath = { albumId ->
                Path(getImportDir(albumId), MediaThumbnailFileName)
            },
            getAlbumByTitleAndArtistId = getAlbumByTitleAndArtistId,
            source = Source.Local,
        )
        return ImportData(
            uri = uri,
            mediaMetadata = mediaMetadata,
            simpleAlbum = simpleAlbum
        )
    }

    private suspend fun import(
        importData: ImportData,
        getImportDir: (AlbumId) -> Path,
        saveTrack: suspend (Track) -> Unit,
    ): Result<Track, MediaImportError> {
        val fileWriteResult = writeToDisk(
            uri = importData.uri,
            embeddedPicture = importData.mediaMetadata.embeddedPicture,
            importDir = getImportDir(importData.simpleAlbum.id),
        )
        return when (fileWriteResult) {
            is Result.Failure -> Result.Failure(fileWriteResult.failure)
            is Result.Success -> {
                val track = makeTrack(
                    id = UUID.randomUUID(),
                    filePath = fileWriteResult.value,
                    mediaMetadata = importData.mediaMetadata,
                    simpleArtists = importData.simpleAlbum.artists,
                    simpleAlbum = importData.simpleAlbum,
                    source = Source.Local,
                )
                saveTrack(track)
                Result.Success(track)
            }
        }
    }

    private suspend fun writeToDisk(
        uri: Uri,
        embeddedPicture: ByteArray?,
        importDir: Path,
    ): Result<Path, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            try {
                SystemFileSystem.createDirectories(importDir)
            } catch (e: IOException) {
                return@withContext Result.Failure(MediaImportError.MkdirError)
            }

            if (embeddedPicture != null) {
                val path = Path(importDir, MediaThumbnailFileName)
                when (fileWriter.writeBitmapToDisk(embeddedPicture, path)) {
                    is Result.Failure -> Unit // Fail silently for now
                    is Result.Success -> Unit
                }
            }

            return@withContext fileWriter.writeToDisk(
                contentUri = uri,
                destinationDir = importDir,
            ).mapFailure { it.toMediaImportError() }
        } catch (e: Throwable) {
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    private fun FileWriteError.toMediaImportError() = when (this) {
        FileWriteError.UnknownInputFileError -> MediaImportError.InputFileError
        is FileWriteError.InputFileNotFound ->
            MediaImportError.FileWriteError.InputFileNotFound(throwable = throwable)
        FileWriteError.InputStreamError -> MediaImportError.FileWriteError.InputStreamError
        is FileWriteError.Unknown -> MediaImportError.FileWriteError.Unknown(throwable = throwable)
    }
}
