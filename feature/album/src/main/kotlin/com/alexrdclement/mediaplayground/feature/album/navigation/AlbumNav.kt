package com.alexrdclement.mediaplayground.feature.album.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.album.AlbumScreen
import com.alexrdclement.mediaplayground.feature.album.metadata.AlbumMetadataScreen
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.audio.ImageId
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
}

fun EntryProviderScope<NavKey>.albumEntryProvider(
    navController: NavController,
    onNavigateToAlbumEditor: (AlbumId) -> Unit = {},
    onNavigateToArtistEditor: (artistId: String) -> Unit = {},
    onNavigateToTrackEditor: (trackId: String) -> Unit = {},
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
    onNavigateToImageMetadata: (imageId: ImageId) -> Unit = {},
) {
    entry<AlbumRoute> { route ->
        AlbumScreen(
            albumId = route.albumId,
            onNavigateToAlbumEditor = onNavigateToAlbumEditor,
            onNavigateToArtistEditor = onNavigateToArtistEditor,
            onNavigateToTrackEditor = onNavigateToTrackEditor,
        )
    }
    entry<AlbumMetadataRoute> { route ->
        AlbumMetadataScreen(
            albumId = route.albumId,
            onNavigateBack = { navController.goBack() },
            onNavigateToArtistMetadata = onNavigateToArtistMetadata,
            onNavigateToImageMetadata = onNavigateToImageMetadata,
        )
    }
}
