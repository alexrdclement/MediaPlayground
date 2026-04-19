package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.model.result.Result

interface ClipImporter {
    suspend fun import(
        uri: Uri,
    ): Result<Clip, MediaImportError>

    suspend fun import(
        uris: List<Uri>,
    ): Map<Uri, Result<Clip, MediaImportError>>
}
