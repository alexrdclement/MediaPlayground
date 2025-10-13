package com.alexrdclement.mediaplayground.feature.spotify

import com.alexrdclement.uiplayground.loggable.Loggable

sealed class SpotifyLibraryError : Loggable {
    data object NotPlayable : SpotifyLibraryError()
    data object SpotifyNotAuthenticated: SpotifyLibraryError()
    data class Unknown(val error: Throwable): SpotifyLibraryError()

    override val message: String
        get() = when(this) {
            is NotPlayable -> "Item not playable"
            is SpotifyNotAuthenticated -> "Spotify not authenticated"
            is Unknown -> "Unknown error: ${this.error.message}"
        }
}
