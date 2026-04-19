package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.model.result.Result

interface AudioAssetImporter {
    suspend fun import(
        uri: Uri,
    ): Result<AudioAsset, MediaImportError>

    suspend fun import(
        uris: List<Uri>,
    ): Map<Uri, Result<AudioAsset, MediaImportError>>
}
