package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result

interface AudioAssetImporter {
    suspend fun import(
        uri: Uri,
        metadata: MediaMetadata.Audio,
    ): Result<MediaAssetImportResult.Audio, MediaImportError>
}
