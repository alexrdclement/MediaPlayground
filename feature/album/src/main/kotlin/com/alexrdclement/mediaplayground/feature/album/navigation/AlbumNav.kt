package com.alexrdclement.mediaplayground.feature.album.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import com.alexrdclement.mediaplayground.feature.album.AlbumScreen
import com.alexrdclement.mediaplayground.feature.album.delete.AlbumDeleteScreen
import com.alexrdclement.mediaplayground.feature.album.metadata.AlbumMetadataScreen
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

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

fun EntryProviderScope<NavKey>.albumEntryProvider(
    navController: NavController,
    onNavigateToAlbumMetadata: (AlbumId) -> Unit = {},
    onNavigateToAlbumDelete: (albumId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
    onNavigateToArtistDelete: (artistId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToTrackMetadata: (trackId: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToImageMetadata: (imageId: ImageId) -> Unit = {},
) {
    entry<AlbumRoute> { route ->
        AlbumScreen(
            albumId = route.albumId,
            onNavigateBack = { navController.goBack() },
            onNavigateToAlbumMetadata = { onNavigateToAlbumMetadata(route.albumId) },
            onNavigateToAlbumDelete = { displayName -> onNavigateToAlbumDelete(route.albumId.value, displayName) },
            onNavigateToArtistMetadata = onNavigateToArtistMetadata,
            onNavigateToArtistDelete = onNavigateToArtistDelete,
            onNavigateToTrackMetadata = onNavigateToTrackMetadata,
            onNavigateToTrackDelete = onNavigateToTrackDelete,
        )
    }
    entry<AlbumMetadataRoute> { route ->
        AlbumMetadataScreen(
            albumId = route.albumId,
            onNavigateBack = { navController.goBack() },
            onNavigateToDelete = { displayName -> onNavigateToAlbumDelete(route.albumId.value, displayName) },
            onNavigateToArtistMetadata = onNavigateToArtistMetadata,
            onNavigateToImageMetadata = { onNavigateToImageMetadata(ImageId(it)) },
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
