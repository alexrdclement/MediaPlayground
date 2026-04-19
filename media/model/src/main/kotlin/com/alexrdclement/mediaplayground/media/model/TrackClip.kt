package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackClip<T : TimeUnit>(
    val clip: Clip,
    val trackOffset: T,
)
