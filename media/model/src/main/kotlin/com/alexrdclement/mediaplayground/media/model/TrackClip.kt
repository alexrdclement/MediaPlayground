package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackClip(
    val clip: Clip,
    val startFrameInTrack: Long,
)
