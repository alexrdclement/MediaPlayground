package com.alexrdclement.mediaplayground.feature.album.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.album.AlbumScreen
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

fun NavGraphBuilder.albumNavGraph() {
    wildcardRoute<AlbumRoute> { pathSegment ->
        AlbumRoute(albumIdValue = pathSegment.value)
    }
}

fun EntryProviderScope<NavKey>.albumEntryProvider(
    navController: NavController,
) {
    entry<AlbumRoute> { route ->
        AlbumScreen(albumId = route.albumId)
    }
}
