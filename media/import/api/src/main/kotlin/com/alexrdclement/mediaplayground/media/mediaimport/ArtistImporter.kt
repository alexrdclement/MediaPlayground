package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.model.result.Result

interface ArtistImporter {
    suspend fun import(uri: Uri): Result<Artist, MediaImportError>

    suspend fun import(uris: List<Uri>): Map<Uri, Result<Artist, MediaImportError>>
}
