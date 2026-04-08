package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.model.result.Result

interface ImageImporter {
    suspend fun import(uri: Uri): Result<Image, MediaImportError>

    suspend fun import(uris: List<Uri>): Map<Uri, Result<Image, MediaImportError>>
}
