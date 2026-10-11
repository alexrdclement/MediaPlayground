package com.alexrdclement.mediaplayground.feature.playback.control.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import com.alexrdclement.mediaplayground.feature.playback.control.PlaybackControlScreen
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey

fun NavGraphBuilder.playbackControlNavGraph() {
    route(PlaybackControlRoute)
}

fun EntryProviderScope<NavKey>.playbackControlEntryProvider() {
    entry<PlaybackControlRoute>(
        metadata = DialogSceneStrategy.dialog(),
    ) {
        PlaybackControlScreen()
    }
}
