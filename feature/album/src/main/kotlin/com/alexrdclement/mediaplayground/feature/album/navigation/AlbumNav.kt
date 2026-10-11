package com.alexrdclement.mediaplayground.feature.album.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import com.alexrdclement.mediaplayground.feature.album.AlbumScreen
import com.alexrdclement.mediaplayground.feature.album.delete.AlbumDeleteScreen
import com.alexrdclement.mediaplayground.feature.album.metadata.AlbumMetadataScreen
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey

fun NavGraphBuilder.albumNavGraph() {
    wildcardRoute<AlbumRoute> { pathSegment ->
        AlbumRoute(albumIdValue = pathSegment.value)
    }
    wildcardRoute<AlbumMetadataRoute> { pathSegment ->
        AlbumMetadataRoute(albumIdValue = pathSegment.value)
    }
    wildcardRoute<AlbumDeleteRoute> { pathSegment ->
        AlbumDeleteRoute(albumIdValue = pathSegment.value)
    }
}

data class AlbumNavigationCallbacks(
    val onNavigateToAlbumMetadata: (AlbumId) -> Unit = {},
    val onNavigateToAlbumDelete: (albumId: String, displayName: String) -> Unit = { _, _ -> },
    val onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
    val onNavigateToArtistDelete: (artistId: String, displayName: String) -> Unit = { _, _ -> },
    val onNavigateToTrackMetadata: (trackId: String) -> Unit = {},
    val onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
    val onNavigateToImageMetadata: (imageId: ImageId) -> Unit = {},
)

fun EntryProviderScope<NavKey>.albumEntryProvider(
    navController: NavController,
    callbacks: AlbumNavigationCallbacks = AlbumNavigationCallbacks(),
) {
    entry<AlbumRoute> { route ->
        AlbumScreen(
            albumId = route.albumId,
            onNavigateBack = { navController.goBack() },
            onNavigateToAlbumMetadata = { callbacks.onNavigateToAlbumMetadata(route.albumId) },
            onNavigateToAlbumDelete = { displayName ->
                callbacks.onNavigateToAlbumDelete(route.albumId.value, displayName)
            },
            onNavigateToArtistMetadata = callbacks.onNavigateToArtistMetadata,
            onNavigateToArtistDelete = callbacks.onNavigateToArtistDelete,
            onNavigateToTrackMetadata = callbacks.onNavigateToTrackMetadata,
            onNavigateToTrackDelete = callbacks.onNavigateToTrackDelete,
        )
    }
    entry<AlbumMetadataRoute> { route ->
        AlbumMetadataScreen(
            albumId = route.albumId,
            onNavigateBack = { navController.goBack() },
            onNavigateToDelete = { displayName ->
                callbacks.onNavigateToAlbumDelete(route.albumId.value, displayName)
            },
            onNavigateToArtistMetadata = callbacks.onNavigateToArtistMetadata,
            onNavigateToImageMetadata = { callbacks.onNavigateToImageMetadata(ImageId(it)) },
        )
    }
    entry<AlbumDeleteRoute>(metadata = DialogSceneStrategy.dialog()) { route ->
        AlbumDeleteScreen(
            albumId = route.albumId,
            displayName = route.displayName,
            onNavigateBack = { navController.goBack() },
        )
    }
}
