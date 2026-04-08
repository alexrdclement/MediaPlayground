package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.model.result.Result

interface AlbumImporter {
    suspend fun import(
        uri: Uri,
        source: Source = Source.Local,
    ): Result<Album, MediaImportError>

    suspend fun import(
        uris: List<Uri>,
        source: Source = Source.Local,
    ): Map<Uri, Result<Album, MediaImportError>>
}
