package com.alexrdclement.mediaplayground.feature.player.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.player.PlayerScreen
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

fun NavGraphBuilder.playerNavGraph() {
    route(PlayerGraph)
}

fun EntryProviderScope<NavKey>.playerEntryProvider(
    navController: NavController,
) {
    entry<PlayerGraph> {
        PlayerScreen()
    }
}
