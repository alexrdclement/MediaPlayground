package com.alexrdclement.mediaplayground.feature.playback.control

sealed class PlaybackControl {
    data class Speed(
        override val value: Float = 1f,
        override val decrementEnabled: Boolean = true,
        override val incrementEnabled: Boolean = true,
    ) : PlaybackControl(), NudgeControl

    data class Pitch(
        override val value: Float = 1f,
        override val decrementEnabled: Boolean = true,
        override val incrementEnabled: Boolean = true,
    ) : PlaybackControl(), NudgeControl
}

interface NudgeControl {
    val value: Float
    val incrementEnabled: Boolean
    val decrementEnabled: Boolean
}
