package com.alexrdclement.mediaplayground.feature.spotify

import com.alexrdclement.logging.Loggable
import com.alexrdclement.mediaplayground.media.engine.PlaylistError as EnginePlaylistError

sealed class SpotifyLibraryUiError(
    override val message: String,
    override val throwable: Throwable? = null,
) : Loggable {
    data object NotPlayable : SpotifyLibraryUiError(
        message = "Item is not playable",
    )
    data class PlaylistError(
        val error: EnginePlaylistError,
    ) : SpotifyLibraryUiError(
        message = "Playlist error: ${error.message}",
        throwable = error,
    )
}
