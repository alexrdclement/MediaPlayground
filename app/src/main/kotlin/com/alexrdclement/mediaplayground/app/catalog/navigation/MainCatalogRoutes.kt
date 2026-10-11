package com.alexrdclement.mediaplayground.app.catalog.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface MainCatalogNavRoute : NavKey

@Serializable
@SerialName("main-catalog")
data object MainCatalogGraph : MainCatalogNavRoute, NavGraphRoute {
    override val pathSegment = "catalog".toPathSegment()
}
