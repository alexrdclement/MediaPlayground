package com.alexrdclement.mediaplayground.feature.audio.library

import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContentState
import com.alexrdclement.mediaplayground.feature.audio.library.content.spotify.SpotifyContentState

sealed class AudioLibraryUiState {
    data object InitialState : AudioLibraryUiState()

    data class ContentReady(
        val spotifyContentState: SpotifyContentState,
        val localContentState: LocalContentState,
        val isMediaItemLoaded: Boolean,
    ) : AudioLibraryUiState()
}
