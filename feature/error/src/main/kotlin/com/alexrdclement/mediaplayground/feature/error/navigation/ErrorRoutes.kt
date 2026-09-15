package com.alexrdclement.mediaplayground.feature.error.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
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
