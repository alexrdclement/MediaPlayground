package com.alexrdclement.mediaplayground.player.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alexrdclement.mediaplayground.player.PlayerScreen
import com.alexrdclement.mediaplayground.player.PlayerViewModel

const val PlayerRoute = "player"

fun NavController.navigateToPlayer(navOptions: NavOptions? = null) {
    navigate(PlayerRoute, navOptions)
}

fun NavGraphBuilder.playerScreen() {
    composable(PlayerRoute) {
        val viewModel: PlayerViewModel = hiltViewModel()
        val player by viewModel.player.collectAsStateWithLifecycle()
        PlayerScreen(
            player = player,
        )
    }
}
