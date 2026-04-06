package com.alexrdclement.mediaplayground.feature.album

import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.ui.model.TrackUi

sealed class AlbumUiState {
    data object Loading : AlbumUiState()
    data object NotFound : AlbumUiState()

    data class Success(
        val imageUrl: String?,
        val title: String,
        val artists: List<Artist>,
        val tracks: List<TrackUi>,
        val isAlbumPlayable: Boolean,
        val isAlbumPlaying: Boolean,
        val isMediaItemLoaded: Boolean,
    ) : AlbumUiState()
}
