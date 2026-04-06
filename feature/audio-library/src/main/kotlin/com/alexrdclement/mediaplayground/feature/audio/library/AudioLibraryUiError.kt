package com.alexrdclement.mediaplayground.feature.audio.library

import android.net.Uri
import com.alexrdclement.mediaplayground.data.track.TrackImportResult
import com.alexrdclement.logging.Loggable
import com.alexrdclement.mediaplayground.media.engine.PlaylistError as EnginePlaylistError

sealed class AudioLibraryUiError(
    override val message: String,
    override val throwable: Throwable? = null,
) : Loggable {
    data class ImportFailure(
        val uri: Uri,
        val error: TrackImportResult.Error,
    ) : AudioLibraryUiError(
        message = "Failed to import media from $uri: ${error.message}",
        throwable = when (error) {
            is TrackImportResult.Error.Unknown -> error.throwable
            is TrackImportResult.Error.ImportError -> null
        },
    )

    data object NotPlayable : AudioLibraryUiError(
        message = "Item not playable",
    )

    data class PlaylistError(
        val error: EnginePlaylistError,
    ) : AudioLibraryUiError(
        message = "Playlist error: ${error.message}",
        throwable = error,
    )
}
