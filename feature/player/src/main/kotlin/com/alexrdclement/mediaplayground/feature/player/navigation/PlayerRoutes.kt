package com.alexrdclement.mediaplayground.feature.player.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface PlayerNavRoute : NavKey

@Serializable
@SerialName("player")
data object PlayerGraph : PlayerNavRoute, NavGraphRoute {
    override val pathSegment = "player".toPathSegment()
}
