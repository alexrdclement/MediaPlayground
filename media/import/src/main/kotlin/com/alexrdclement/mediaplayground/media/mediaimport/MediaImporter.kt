package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeTrack
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
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
     * @param fileWriteDir: Where to write files associated with this media.
     */
    suspend fun importTrackFromDisk(
        uri: Uri,
        fileWriteDir: File,
    ): Result<Track, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val mediaItemId = UUID.randomUUID().toString()
            val mediaItemFileWriteDir = File(fileWriteDir, mediaItemId)
            if (!mediaItemFileWriteDir.mkdir()) {
                return@withContext Result.Failure(MediaImportError.MkdirError)
            }

            val mediaMetadata = mediaMetadataRetriever.getMediaMetadata(
                contentUri = uri,
                onEmbeddedPictureFound = { embeddedPicture ->
                    val file = File(mediaItemFileWriteDir, MediaThumbnailFileName)
                    when (fileWriter.writeBitmapToDisk(embeddedPicture, file)) {
                        is Result.Failure -> null // Fail silently for now
                        is Result.Success -> file
                    }
                },
            )

            val fileWriteResult = fileWriter.writeToDisk(
                contentUri = uri,
                destinationDir = mediaItemFileWriteDir,
            )
            when (fileWriteResult) {
                is Result.Failure -> Result.Failure(fileWriteResult.failure.toMediaImportError())
                is Result.Success -> Result.Success(
                    value = makeTrack(
                        mediaId = mediaItemId,
                        file = fileWriteResult.value,
                        mediaMetadata = mediaMetadata,
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
