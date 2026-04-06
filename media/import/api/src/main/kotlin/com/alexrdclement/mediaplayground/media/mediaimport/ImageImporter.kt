package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.io.files.Path

interface ImageImporter {
    suspend fun importImagesFromDisk(
        uris: List<Uri>,
        getDestination: (ImageId, extension: String) -> Path,
        saveImage: suspend (imageId: ImageId, fileName: String, mediaMetadata: MediaMetadata.Image) -> Unit,
    ): Map<Uri, Result<ImageId, MediaImportError>>

    suspend fun importImageFromDisk(
        contentUri: Uri,
        getDestination: (ImageId, extension: String) -> Path,
        saveImage: suspend (imageId: ImageId, fileName: String, mediaMetadata: MediaMetadata.Image) -> Unit,
    ): Result<ImageId, MediaImportError>
}
