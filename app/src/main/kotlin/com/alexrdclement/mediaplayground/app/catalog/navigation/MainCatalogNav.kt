package com.alexrdclement.mediaplayground.app.catalog.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.app.catalog.CatalogScreen
import com.alexrdclement.mediaplayground.app.catalog.MainCatalogItem
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey

fun NavGraphBuilder.mainCatalogNavGraph() {
    route(MainCatalogGraph)
}

fun EntryProviderScope<NavKey>.mainCatalogEntryProvider(
    navController: NavController,
    onItemClick: (MainCatalogItem) -> Unit,
) {
    entry<MainCatalogGraph> {
        CatalogScreen(
            items = MainCatalogItem.entries.toList(),
            onItemClick = onItemClick,
        )
    }
}
