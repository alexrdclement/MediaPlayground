package com.alexrdclement.mediaplayground.ui.model

import com.alexrdclement.mediaplayground.media.model.AudioTrack

data class TrackUi(
    val track: AudioTrack,
    val isLoaded: Boolean,
    val isPlayable: Boolean,
    val isPlaying: Boolean,
)
