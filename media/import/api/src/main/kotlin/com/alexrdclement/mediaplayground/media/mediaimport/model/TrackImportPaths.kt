package com.alexrdclement.mediaplayground.media.mediaimport.model

import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ImageId
import kotlinx.io.files.Path

/**
 * Resolves where imported track files and their album images are written.
 */
data class TrackImportPaths(
    val getImportDir: (AlbumId) -> Path,
    val getImagePath: (ImageId, extension: String) -> Path,
)
