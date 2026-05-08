package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result

interface ImageImporter {
    suspend fun import(
        uri: Uri,
        metadata: MediaMetadata.Image,
    ): Result<MediaAssetImportResult.Image, MediaImportError>
}
