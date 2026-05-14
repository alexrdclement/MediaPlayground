package com.alexrdclement.mediaplayground.feature.audio.library.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.audio.library.AudioLibraryScreen
import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.MediaItem
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

fun NavGraphBuilder.audioLibraryNavGraph() {
    route(AudioLibraryGraph)
}

fun EntryProviderScope<NavKey>.audioLibraryEntryProvider(
    navController: NavController,
    onNavigateToPlayer: (MediaItem) -> Unit,
    onNavigateToAlbum: (AudioAlbum) -> Unit,
    onNavigateToTrackMetadata: (TrackId) -> Unit = {},
    onNavigateToAlbumMetadata: (AlbumId) -> Unit = {},
    onNavigateToAlbumDelete: (albumId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
) {
    entry<AudioLibraryGraph> {
        AudioLibraryScreen(
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateToAlbum = onNavigateToAlbum,
            onNavigateToTrackMetadata = { onNavigateToTrackMetadata(TrackId(it)) },
            onNavigateToAlbumMetadata = { onNavigateToAlbumMetadata(AudioAlbumId(it)) },
            onNavigateToAlbumDelete = onNavigateToAlbumDelete,
            onNavigateToTrackDelete = onNavigateToTrackDelete,
        )
    }
}
