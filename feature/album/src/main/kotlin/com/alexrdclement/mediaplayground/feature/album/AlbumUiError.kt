package com.alexrdclement.mediaplayground.feature.album

import com.alexrdclement.logging.Loggable
import com.alexrdclement.mediaplayground.media.engine.PlaylistError as EnginePlaylistError

sealed class AlbumUiError(
    override val message: String,
    override val throwable: Throwable? = null,
) : Loggable {
    data object AlbumNotFound : AlbumUiError(
        message = "Album not found",
    )
    data object AlbumNotPlayable : AlbumUiError(
        message = "Album is not playable",
    )
    data class PlaylistError(
        val error: EnginePlaylistError,
    ) : AlbumUiError(
        message = "Playlist error: ${error.message}",
        throwable = error,
    )
}
