package com.alexrdclement.mediaplayground.feature.artist.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import com.alexrdclement.mediaplayground.feature.artist.ArtistMetadataScreen
import com.alexrdclement.mediaplayground.feature.artist.delete.ArtistDeleteScreen
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

fun NavGraphBuilder.artistMetadataNavGraph() {
    wildcardRoute<ArtistMetadataRoute> { pathSegment ->
        ArtistMetadataRoute(artistIdValue = pathSegment.value)
    }
    wildcardRoute<ArtistDeleteRoute> { pathSegment ->
        ArtistDeleteRoute(artistIdValue = pathSegment.value)
    }
}

fun EntryProviderScope<NavKey>.artistMetadataEntryProvider(
    navController: NavController,
    onNavigateToArtistDelete: (artistId: String, displayName: String) -> Unit = { _, _ -> },
) {
    entry<ArtistMetadataRoute> { route ->
        ArtistMetadataScreen(
            artistId = route.artistIdValue,
            onNavigateBack = { navController.goBack() },
            onNavigateToDelete = { displayName -> onNavigateToArtistDelete(route.artistIdValue, displayName) },
        )
    }
    entry<ArtistDeleteRoute>(metadata = DialogSceneStrategy.dialog()) { route ->
        ArtistDeleteScreen(
            artistIdValue = route.artistIdValue,
            displayName = route.displayName,
            onNavigateBack = { navController.goBack() },
        )
    }
}
