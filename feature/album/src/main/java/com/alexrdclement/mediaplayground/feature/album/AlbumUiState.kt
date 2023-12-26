package com.alexrdclement.mediaplayground.feature.album

import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.ui.shared.model.TrackUi

sealed class AlbumUiState {
    data object Loading : AlbumUiState()

    data class Success(
        val imageUrl: String?,
        val title: String,
        val artists: List<SimpleArtist>,
        val tracks: List<TrackUi>,
        val isAlbumPlayable: Boolean,
        val isAlbumPlaying: Boolean,
        val isMediaItemLoaded: Boolean,
    ) : AlbumUiState()
}
