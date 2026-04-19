package com.alexrdclement.mediaplayground.ui.model

import com.alexrdclement.mediaplayground.media.model.Track

data class TrackUi(
    val track: Track,
    val isLoaded: Boolean,
    val isPlayable: Boolean,
    val isPlaying: Boolean,
)
