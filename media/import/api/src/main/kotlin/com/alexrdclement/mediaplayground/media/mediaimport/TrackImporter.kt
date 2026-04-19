package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.model.result.Result

interface TrackImporter {
    suspend fun import(
        uri: Uri,
    ): Result<AudioTrack, MediaImportError>

    suspend fun import(
        uris: List<Uri>,
    ): Map<Uri, Result<AudioTrack, MediaImportError>>
}
