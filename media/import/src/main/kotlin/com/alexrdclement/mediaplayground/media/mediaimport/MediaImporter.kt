package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeSimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeSimpleArtist
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeTrack
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.Source
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.result.Result
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

    /**
     * Import audio file as a track from disk.
     * @param uri: URI of the file as given by the document provider (not MediaStore).
     */
    suspend fun importTrackFromDisk(
        uri: Uri,
        getImportDir: (AlbumId) -> Path,
        getArtistByName: suspend (String) -> SimpleArtist?,
        getAlbumByTitleAndArtistId: suspend (String, String) -> SimpleAlbum?,
    ): Result<Track, MediaImportError> = withContext(Dispatchers.IO) {
        try {
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

            val importDir = getImportDir(simpleAlbum.id)
            try {
                SystemFileSystem.createDirectories(importDir)
            } catch (e: IOException) {
                return@withContext Result.Failure(MediaImportError.MkdirError)
            }

            if (mediaMetadata.embeddedPicture != null) {
                val path = Path(importDir, MediaThumbnailFileName)
                when (fileWriter.writeBitmapToDisk(mediaMetadata.embeddedPicture, path)) {
                    is Result.Failure -> Unit // Fail silently for now
                    is Result.Success -> Unit
                }
            }

            val fileWriteResult = fileWriter.writeToDisk(
                contentUri = uri,
                destinationDir = importDir,
            )
            when (fileWriteResult) {
                is Result.Failure -> Result.Failure(fileWriteResult.failure.toMediaImportError())
                is Result.Success -> Result.Success(
                    value = makeTrack(
                        id = UUID.randomUUID(),
                        filePath = fileWriteResult.value,
                        mediaMetadata = mediaMetadata,
                        simpleArtist = simpleArtist,
                        simpleAlbum = simpleAlbum,
                        source = Source.Local,
                    )
                )
            }
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
