package com.alexrdclement.mediaplayground.feature.camera.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.camera.CameraScreen
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

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
