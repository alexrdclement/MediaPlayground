package com.alexrdclement.mediaplayground.feature.track.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import com.alexrdclement.mediaplayground.feature.track.TrackMetadataScreen
import com.alexrdclement.mediaplayground.feature.track.delete.TrackDeleteScreen
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

fun NavGraphBuilder.trackMetadataNavGraph() {
    wildcardRoute<TrackMetadataRoute> { pathSegment ->
        TrackMetadataRoute(trackIdValue = pathSegment.value)
    }
    wildcardRoute<TrackDeleteRoute> { pathSegment ->
        TrackDeleteRoute(trackIdValue = pathSegment.value)
    }
}

fun EntryProviderScope<NavKey>.trackMetadataEntryProvider(
    navController: NavController,
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
) {
    entry<TrackMetadataRoute> { route ->
        TrackMetadataScreen(
            trackId = TrackId(route.trackIdValue),
            onNavigateBack = { navController.goBack() },
            onNavigateToDelete = { displayName -> onNavigateToTrackDelete(route.trackIdValue, displayName) },
            onNavigateToArtistMetadata = onNavigateToArtistMetadata,
        )
    }
    entry<TrackDeleteRoute>(metadata = DialogSceneStrategy.dialog()) { route ->
        TrackDeleteScreen(
            trackId = TrackId(route.trackIdValue),
            displayName = route.displayName,
            onNavigateBack = { navController.goBack() },
        )
    }
}
