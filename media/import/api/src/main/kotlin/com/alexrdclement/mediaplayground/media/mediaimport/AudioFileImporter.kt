package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.io.files.Path

interface AudioFileImporter {
    suspend fun import(
        uri: Uri,
        destinationDir: Path,
        source: Source = Source.Local,
    ): Result<MediaAsset, MediaImportError>

    suspend fun import(
        uris: List<Uri>,
        destinationDir: Path,
        source: Source = Source.Local,
    ): Map<Uri, Result<MediaAsset, MediaImportError>>
}
