package com.alexrdclement.mediaplayground.media.engine

import dev.zacsweers.metro.Inject

/**
 * The playback controls shared by [AndroidMediaEngineState] and [AndroidMediaEngineControl].
 */
data class PlaybackControls @Inject constructor(
    val playheadControl: PlayheadControl,
    val timelineControl: TimelineControl,
    val playbackRateControl: PlaybackRateControl,
    val playbackPitchControl: PlaybackPitchControl,
)
