package com.alexrdclement.mediaplayground.feature.album

import com.alexrdclement.uiplayground.loggable.Loggable

sealed class AlbumUiError : Loggable {
    data object AlbumNotFound : AlbumUiError()
    data object AlbumNotPlayable : AlbumUiError()

    override val message: String
        get() = when (this) {
            AlbumNotFound -> "Album not found"
            AlbumNotPlayable -> "Album is not playable"
        }
}
