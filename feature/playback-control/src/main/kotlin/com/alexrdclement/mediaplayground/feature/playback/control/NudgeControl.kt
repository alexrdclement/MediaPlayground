package com.alexrdclement.mediaplayground.feature.playback.control

interface NudgeControl {
    val value: Float
    val incrementEnabled: Boolean
    val decrementEnabled: Boolean
}
