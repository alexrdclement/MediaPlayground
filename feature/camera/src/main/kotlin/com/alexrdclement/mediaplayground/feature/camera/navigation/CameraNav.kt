package com.alexrdclement.mediaplayground.feature.camera.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.camera.CameraScreen
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey

fun NavGraphBuilder.cameraNavGraph() {
    route(CameraGraph)
}

fun EntryProviderScope<NavKey>.cameraEntryProvider(
    navController: NavController,
) {
    entry<CameraGraph> {
        CameraScreen()
    }
}
