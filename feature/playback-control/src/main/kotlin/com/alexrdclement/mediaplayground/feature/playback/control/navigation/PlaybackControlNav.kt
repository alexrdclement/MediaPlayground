package com.alexrdclement.mediaplayground.feature.playback.control.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import com.alexrdclement.mediaplayground.feature.playback.control.PlaybackControlScreen
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

fun NavGraphBuilder.playbackControlNavGraph() {
    route(PlaybackControlRoute)
}

fun EntryProviderScope<NavKey>.playbackControlEntryProvider(
    navController: NavController,
) {
    entry<PlaybackControlRoute>(
        metadata = DialogSceneStrategy.dialog(),
    ) {
        PlaybackControlScreen(
            onDismissRequest = navController::goBack,
        )
    }
}
