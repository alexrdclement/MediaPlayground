package com.alexrdclement.mediaplayground.feature.image.library.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface ImageLibraryNavRoute : NavKey

@Serializable
@SerialName("image-library")
data object ImageLibraryGraph : ImageLibraryNavRoute, NavGraphRoute {
    override val pathSegment = "image-library".toPathSegment()
}
