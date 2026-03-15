package com.alexrdclement.mediaplayground.feature.player.navigation

import com.alexrdclement.palette.navigation.NavGraphRoute
import com.alexrdclement.palette.navigation.NavKey
import com.alexrdclement.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface PlayerNavRoute : NavKey

@Serializable
@SerialName("player")
data object PlayerGraph : PlayerNavRoute, NavGraphRoute {
    override val pathSegment = "player".toPathSegment()
}
