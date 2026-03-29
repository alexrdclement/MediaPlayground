package com.alexrdclement.mediaplayground.feature.artist.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.artist.ArtistMetadataScreen
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

fun NavGraphBuilder.artistMetadataNavGraph() {
    wildcardRoute<ArtistMetadataRoute> { pathSegment ->
        ArtistMetadataRoute(artistIdValue = pathSegment.value)
    }
}

fun EntryProviderScope<NavKey>.artistMetadataEntryProvider(
    navController: NavController,
) {
    entry<ArtistMetadataRoute> { route ->
        ArtistMetadataScreen(
            artistId = route.artistIdValue,
            onNavigateBack = { navController.goBack() },
        )
    }
}
