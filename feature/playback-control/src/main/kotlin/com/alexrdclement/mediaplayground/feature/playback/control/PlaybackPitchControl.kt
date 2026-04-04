package com.alexrdclement.mediaplayground.feature.playback.control

data class PlaybackPitchControl(
    override val value: Float = 1f,
    override val decrementEnabled: Boolean = true,
    override val incrementEnabled: Boolean = true,
) : NudgeControl
