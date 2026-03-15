package com.alexrdclement.mediaplayground.feature.audio.library.navigation

import com.alexrdclement.palette.navigation.NavGraphRoute
import com.alexrdclement.palette.navigation.NavKey
import com.alexrdclement.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface AudioLibraryNavRoute : NavKey

@Serializable
@SerialName("audio-library")
data object AudioLibraryGraph : AudioLibraryNavRoute, NavGraphRoute {
    override val pathSegment = "audio-library".toPathSegment()
}
