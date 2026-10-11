package com.alexrdclement.mediaplayground.app.catalog.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.app.catalog.CatalogScreen
import com.alexrdclement.mediaplayground.app.catalog.MainCatalogItem
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey

fun NavGraphBuilder.mainCatalogNavGraph() {
    route(MainCatalogGraph)
}

fun EntryProviderScope<NavKey>.mainCatalogEntryProvider(onItemClick: (MainCatalogItem) -> Unit) {
    entry<MainCatalogGraph> {
        CatalogScreen(
            items = MainCatalogItem.entries.toList(),
            onItemClick = onItemClick,
        )
    }
}
