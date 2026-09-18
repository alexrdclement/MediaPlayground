package com.alexrdclement.mediaplayground.feature.audio.library.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface AudioLibraryNavRoute : NavKey

@Serializable
@SerialName("audio-library")
data object AudioLibraryGraph : AudioLibraryNavRoute, NavGraphRoute {
    override val pathSegment = "audio-library".toPathSegment()
}
