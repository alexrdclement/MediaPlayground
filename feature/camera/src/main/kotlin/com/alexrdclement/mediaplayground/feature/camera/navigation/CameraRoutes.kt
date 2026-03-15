package com.alexrdclement.mediaplayground.feature.camera.navigation

import com.alexrdclement.palette.navigation.NavGraphRoute
import com.alexrdclement.palette.navigation.NavKey
import com.alexrdclement.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface CameraNavRoute : NavKey

@Serializable
@SerialName("camera")
data object CameraGraph : CameraNavRoute, NavGraphRoute {
    override val pathSegment = "camera".toPathSegment()
}
