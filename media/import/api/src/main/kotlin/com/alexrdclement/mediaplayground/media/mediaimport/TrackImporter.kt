package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.io.files.Path

interface TrackImporter {
    suspend fun importTrackFromDisk(
        uri: Uri,
        getImportDir: (AlbumId) -> Path,
        getImagePath: (ImageId, extension: String) -> Path,
    ): Result<Track, MediaImportError>

    suspend fun importTracksFromDisk(
        uris: List<Uri>,
        getImportDir: (AlbumId) -> Path,
        getImagePath: (ImageId, extension: String) -> Path,
    ): Map<Uri, Result<Track, MediaImportError>>
}
