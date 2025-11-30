package com.alexrdclement.mediaplayground.feature.camera.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alexrdclement.mediaplayground.feature.camera.CameraScreen

const val CameraRoute = "camera"

fun NavController.navigateToCamera(navOptions: NavOptions? = null) {
    navigate(CameraRoute, navOptions)
}

fun NavGraphBuilder.cameraScreen() {
    composable(CameraRoute) {
        CameraScreen()
    }
}
