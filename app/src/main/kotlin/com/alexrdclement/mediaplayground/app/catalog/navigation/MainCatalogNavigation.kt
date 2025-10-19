package com.alexrdclement.mediaplayground.app.catalog.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alexrdclement.mediaplayground.app.catalog.CatalogScreen
import com.alexrdclement.mediaplayground.app.catalog.MainCatalogItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("catalog")
object MainCatalogRoute

fun NavGraphBuilder.mainCatalogScreen(
    onItemClick: (MainCatalogItem) -> Unit,
) {
    composable<MainCatalogRoute> {
        CatalogScreen(
            items = MainCatalogItem.entries.toList(),
            onItemClick = onItemClick,
        )
    }
}
