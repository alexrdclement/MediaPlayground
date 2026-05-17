package com.alexrdclement.mediaplayground.ui.model

import com.alexrdclement.mediaplayground.media.model.Track

data class TrackUi(
    val track: Track,
    val trackNumber: Int?,
    val subtitle: String,
    val isLoaded: Boolean,
    val isPlayable: Boolean,
    val isPlaying: Boolean,
)
