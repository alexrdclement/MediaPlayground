package com.alexrdclement.mediaplayground.feature.image.library.navigation

import com.alexrdclement.palette.navigation.NavGraphRoute
import com.alexrdclement.palette.navigation.NavKey
import com.alexrdclement.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface ImageLibraryNavRoute : NavKey

@Serializable
@SerialName("image-library")
data object ImageLibraryGraph : ImageLibraryNavRoute, NavGraphRoute {
    override val pathSegment = "image-library".toPathSegment()
}
