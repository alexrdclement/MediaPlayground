package com.alexrdclement.mediaplayground.feature.playback.control.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface PlaybackControlNavRoute : NavKey

@Serializable
@SerialName("playback-control")
data object PlaybackControlRoute : PlaybackControlNavRoute, NavGraphRoute {
    override val pathSegment = "playback-control".toPathSegment()
}
