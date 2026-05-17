package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.AlbumTrack
import com.alexrdclement.mediaplayground.model.result.Result

interface AlbumTrackImporter {
    suspend fun import(
        uri: Uri,
    ): Result<AlbumTrack, MediaImportError>

    suspend fun import(
        uris: List<Uri>,
    ): Map<Uri, Result<AlbumTrack, MediaImportError>>
}
