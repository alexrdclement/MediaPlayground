package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeSimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeSimpleArtist
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeTrack
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.image.ImageId
import com.alexrdclement.mediaplayground.media.model.image.ImageMetadata
import com.alexrdclement.mediaplayground.media.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.media.model.audio.Source
import com.alexrdclement.mediaplayground.media.model.audio.Track
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.mapFailure
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import java.util.UUID

class MediaImporter @Inject constructor(
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val fileWriter: FileWriter,
) {

    private data class ImportData(
        val uri: Uri,
        val mediaMetadata: MediaMetadata.Audio,
        val simpleAlbum: SimpleAlbum,
    )

    /**
     * Import audio file as a track from disk.
     * @param uri: URI of the file as given by the document provider (not MediaStore).
     */
    suspend fun importTrackFromDisk(
        uri: Uri,
        getImportDir: (AlbumId) -> Path,
        getImagePath: (ImageId, extension: String) -> Path,
        getArtistByName: suspend (String) -> SimpleArtist?,
        getAlbumByTitleAndArtistId: suspend (String, String) -> SimpleAlbum?,
        saveTrack: suspend (Track) -> Unit,
    ): Result<Track, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val importData = getImportData(
                uri = uri,
                getImportDir = getImportDir,
                getImagePath = getImagePath,
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
        getImagePath: (ImageId, extension: String) -> Path,
        getArtistByName: suspend (String) -> SimpleArtist?,
        getAlbumByTitleAndArtistId: suspend (String, String) -> SimpleAlbum?,
        saveTrack: suspend (Track) -> Unit,
    ): Map<Uri, Result<Track, MediaImportError>> {
        // Album directory is only correct after a track has been saved. Import sequentially.
        return uris.associateWith { uri ->
            val importData = getImportData(
                uri = uri,
                getImportDir = getImportDir,
                getImagePath = getImagePath,
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

    suspend fun importImagesFromDisk(
        uris: List<Uri>,
        getDestination: (ImageId, extension: String) -> Path,
        saveImage: suspend (imageId: ImageId, fileName: String, metadata: ImageMetadata?) -> Unit,
    ): Map<Uri, Result<ImageId, FileWriteError>> {
        return uris.associateWith { uri ->
            importImageFromDisk(
                contentUri = uri,
                getDestination = getDestination,
                saveImage = saveImage,
            )
        }
    }

    suspend fun importImageFromDisk(
        contentUri: Uri,
        getDestination: (ImageId, extension: String) -> Path,
        saveImage: suspend (imageId: ImageId, fileName: String, metadata: ImageMetadata?) -> Unit,
    ): Result<ImageId, FileWriteError> = withContext(Dispatchers.IO) {
        val mediaMetadata = mediaMetadataRetriever.getMediaMetadata(contentUri) as MediaMetadata.Image
        val imageId = ImageId(UUID.randomUUID().toString())
        val fileName = "${imageId.value}.${mediaMetadata.extension}"
        val destination = getDestination(imageId, mediaMetadata.extension)

        try {
            destination.parent?.let { SystemFileSystem.createDirectories(it) }
        } catch (e: IOException) {
            // Directory may already exist
        }

        when (val result = fileWriter.writeFileToDisk(contentUri, destination)) {
            is Result.Failure -> Result.Failure(result.failure)
            is Result.Success -> {
                saveImage(imageId, fileName, mediaMetadata.imageMetadata)
                Result.Success(imageId)
            }
        }
    }

    private suspend fun getImportData(
        uri: Uri,
        getImportDir: (AlbumId) -> Path,
        getImagePath: (ImageId, extension: String) -> Path,
        getArtistByName: suspend (String) -> SimpleArtist?,
        getAlbumByTitleAndArtistId: suspend (String, String) -> SimpleAlbum?,
    ): ImportData {
        val mediaMetadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri) as MediaMetadata.Audio
        val simpleArtist = makeSimpleArtist(mediaMetadata, getArtistByName)
        val simpleAlbum = makeSimpleAlbum(
            mediaMetadata = mediaMetadata,
            simpleArtist = simpleArtist,
            getImageFilePath = getImagePath,
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
        val imagePath = importData.simpleAlbum.images.firstOrNull()?.uri?.let { Path(it) }
        val fileWriteResult = writeToDisk(
            uri = importData.uri,
            embeddedPicture = importData.mediaMetadata.embeddedPicture,
            imagePath = imagePath,
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
        imagePath: Path?,
        importDir: Path,
    ): Result<Path, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            try {
                SystemFileSystem.createDirectories(importDir)
            } catch (e: IOException) {
                return@withContext Result.Failure(MediaImportError.MkdirError)
            }

            if (embeddedPicture != null && imagePath != null) {
                try {
                    imagePath.parent?.let { SystemFileSystem.createDirectories(it) }
                } catch (e: IOException) {
                    // Fail silently — image write is best-effort
                }
                when (fileWriter.writeBitmapToDisk(embeddedPicture, imagePath)) {
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
