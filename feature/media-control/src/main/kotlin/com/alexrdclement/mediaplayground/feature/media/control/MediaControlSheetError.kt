package com.alexrdclement.mediaplayground.feature.media.control

import com.alexrdclement.loggable.Loggable
import com.alexrdclement.mediaplayground.media.engine.PlaylistError as EnginePlaylistError

sealed class MediaControlSheetError(
    override val message: String,
    override val throwable: Throwable? = null,
) : Loggable {
    data class PlaylistError(
        val error: EnginePlaylistError,
    ) : MediaControlSheetError(
        message = "Playlist error: ${error.message}",
        throwable = error,
    )
}
