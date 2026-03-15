package com.alexrdclement.mediaplayground.app.catalog.navigation

import com.alexrdclement.palette.navigation.NavGraphRoute
import com.alexrdclement.palette.navigation.NavKey
import com.alexrdclement.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface MainCatalogNavRoute : NavKey

@Serializable
@SerialName("main-catalog")
data object MainCatalogGraph : MainCatalogNavRoute, NavGraphRoute {
    override val pathSegment = "catalog".toPathSegment()
}
