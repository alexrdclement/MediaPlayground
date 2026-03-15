package com.alexrdclement.mediaplayground.feature.error.navigation

import com.alexrdclement.palette.navigation.NavGraphRoute
import com.alexrdclement.palette.navigation.NavKey
import com.alexrdclement.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface ErrorNavRoute : NavKey

@Serializable
@SerialName("error")
data class ErrorGraph(
    val message: String,
) : ErrorNavRoute, NavGraphRoute {
    override val pathSegment = "error".toPathSegment()
}
