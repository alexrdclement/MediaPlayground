package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toMediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.store.FileWriter
import com.alexrdclement.mediaplayground.model.result.Result
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import java.util.UUID

class ImageImporterImpl @Inject constructor(
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val fileWriter: FileWriter,
) : ImageImporter {

    override suspend fun importImageFromDisk(
        contentUri: Uri,
        getDestination: (ImageId, extension: String) -> Path,
        saveImage: suspend (imageId: ImageId, fileName: String, mediaMetadata: MediaMetadata.Image) -> Unit,
    ): Result<ImageId, MediaImportError> = withContext(Dispatchers.IO) {
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
            is Result.Failure -> Result.Failure(result.failure.toMediaImportError())
            is Result.Success -> {
                saveImage(imageId, fileName, mediaMetadata)
                Result.Success(imageId)
            }
        }
    }

    override suspend fun importImagesFromDisk(
        uris: List<Uri>,
        getDestination: (ImageId, extension: String) -> Path,
        saveImage: suspend (imageId: ImageId, fileName: String, mediaMetadata: MediaMetadata.Image) -> Unit,
    ): Map<Uri, Result<ImageId, MediaImportError>> {
        return uris.associateWith { uri ->
            importImageFromDisk(
                contentUri = uri,
                getDestination = getDestination,
                saveImage = saveImage,
            )
        }
    }
}
