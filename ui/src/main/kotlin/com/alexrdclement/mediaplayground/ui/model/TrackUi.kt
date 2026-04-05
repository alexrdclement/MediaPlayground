package com.alexrdclement.mediaplayground.ui.model

import com.alexrdclement.mediaplayground.media.model.SimpleTrack

data class TrackUi(
    val track: SimpleTrack,
    val isLoaded: Boolean,
    val isPlayable: Boolean,
    val isPlaying: Boolean,
)
