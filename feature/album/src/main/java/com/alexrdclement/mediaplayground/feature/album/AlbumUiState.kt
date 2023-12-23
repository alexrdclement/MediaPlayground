package com.alexrdclement.mediaplayground.feature.album

import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack

sealed class AlbumUiState {
    data object Loading : AlbumUiState()

    data class Success(
        val imageUrl: String?,
        val title: String,
        val artists: List<SimpleArtist>,
        val tracks: List<TrackUi>,
        val isMediaItemLoaded: Boolean,
    ) : AlbumUiState() {
        data class TrackUi(
            val track: SimpleTrack,
            val isLoaded: Boolean,
            val isPlaying: Boolean,
            val isPlayable: Boolean,
        )
    }
}
