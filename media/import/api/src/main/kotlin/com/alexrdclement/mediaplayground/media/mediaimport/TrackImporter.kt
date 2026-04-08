package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.model.result.Result

interface TrackImporter {
    suspend fun import(
        uri: Uri,
        source: Source = Source.Local,
    ): Result<Track, MediaImportError>

    suspend fun import(
        uris: List<Uri>,
        source: Source = Source.Local,
    ): Map<Uri, Result<Track, MediaImportError>>
}
