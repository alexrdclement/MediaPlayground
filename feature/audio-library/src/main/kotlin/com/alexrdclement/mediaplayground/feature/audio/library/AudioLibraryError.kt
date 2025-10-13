package com.alexrdclement.mediaplayground.feature.audio.library

import com.alexrdclement.uiplayground.log.Loggable

sealed class AudioLibraryError : Loggable {
    data object NotPlayable : AudioLibraryError()
    data object SpotifyNotAuthenticated: AudioLibraryError()
    data class Unknown(val error: Throwable): AudioLibraryError()

    override val message: String
        get() = when (this) {
            NotPlayable -> "Item not playable"
            SpotifyNotAuthenticated -> "Spotify not authenticated"
            is Unknown -> "Unknown error: ${this.error.message}"
        }
}
