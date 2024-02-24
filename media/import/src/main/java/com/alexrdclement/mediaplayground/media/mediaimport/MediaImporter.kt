package com.alexrdclement.mediaplayground.media.mediaimport

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toTrack
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.result.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

@OptIn(UnstableApi::class)
class MediaImporter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mediaMetadataRetriever: MediaMetadataRetriever,
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
            val mediaItemFileWriteDir = File(fileWriteDir, UUID.randomUUID().toString())
            if (!mediaItemFileWriteDir.mkdir()) {
                return@withContext Result.Failure(MediaImportError.Unknown())
            }

            val mediaMetadata = mediaMetadataRetriever.getMediaMetadata(
                contentUri = uri,
                onEmbeddedPictureFound = { embeddedPicture ->
                    val file = File(mediaItemFileWriteDir, MediaThumbnailFileName)
                    val bitmap = BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.size) ?:
                        return@getMediaMetadata null
                    when (bitmap.writeToDisk(file)) {
                        is Result.Failure -> null
                        is Result.Success -> file
                    }
                },
            )

            val documentFile = DocumentFile.fromSingleUri(context, uri)
                ?: return@withContext Result.Failure(MediaImportError.InputFileError)
            val documentFileName = documentFile.name
                ?: return@withContext Result.Failure(MediaImportError.InputFileError)

            val fileWriteResult = documentFile.writeToDisk(
                destination = File(fileWriteDir, documentFileName),
                contentResolver = context.contentResolver,
            )
            when (fileWriteResult) {
                is Result.Failure -> Result.Failure(fileWriteResult.failure.toMediaImportError())
                is Result.Success -> {
                    val mediaItem = MediaItem.fromUri(uri)
                    Result.Success(
                        value = mediaItem.toTrack(
                            contentUri = fileWriteResult.value.toUri(),
                            mediaMetadata = mediaMetadata
                        )
                    )
                }
            }
        } catch (e: Throwable) {
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    private fun FileWriteError.toMediaImportError() = when (this) {
        FileWriteError.InputStreamError -> MediaImportError.FileWriteError.InputStreamError
        is FileWriteError.Unknown -> MediaImportError.FileWriteError.Unknown(throwable = throwable)
    }
}
