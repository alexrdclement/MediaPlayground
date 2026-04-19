package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.model.result.Result

interface AlbumImporter {
    suspend fun import(
        uri: Uri,
    ): Result<AudioAlbum, MediaImportError>

    suspend fun import(
        uris: List<Uri>,
    ): Map<Uri, Result<AudioAlbum, MediaImportError>>
}
