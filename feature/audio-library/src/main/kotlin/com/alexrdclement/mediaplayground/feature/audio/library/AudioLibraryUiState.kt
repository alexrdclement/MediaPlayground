package com.alexrdclement.mediaplayground.feature.audio.library

import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContentState

sealed class AudioLibraryUiState {
    data object InitialState : AudioLibraryUiState()

    data class ContentReady(
        val localContentState: LocalContentState,
        val isMediaItemLoaded: Boolean,
    ) : AudioLibraryUiState()
}
