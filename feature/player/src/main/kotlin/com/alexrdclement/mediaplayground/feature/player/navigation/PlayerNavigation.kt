package com.alexrdclement.mediaplayground.feature.player.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alexrdclement.mediaplayground.feature.player.PlayerScreen

const val PlayerRoute = "player"

fun NavController.navigateToPlayer(navOptions: NavOptions? = null) {
    navigate(PlayerRoute, navOptions)
}

fun NavGraphBuilder.playerScreen() {
    composable(PlayerRoute) {
        PlayerScreen()
    }
}
