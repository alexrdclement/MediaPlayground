package com.alexrdclement.mediaplayground.feature.audio.library.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.audio.library.AudioLibraryScreen
import com.alexrdclement.mediaplayground.media.model.audio.Album
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.audio.MediaItem
import com.alexrdclement.mediaplayground.media.model.audio.TrackId
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

fun NavGraphBuilder.audioLibraryNavGraph() {
    route(AudioLibraryGraph)
}

fun EntryProviderScope<NavKey>.audioLibraryEntryProvider(
    navController: NavController,
    onNavigateToPlayer: (MediaItem) -> Unit,
    onNavigateToAlbum: (Album) -> Unit,
    onNavigateToTrackMetadata: (TrackId) -> Unit = {},
    onNavigateToAlbumMetadata: (AlbumId) -> Unit = {},
) {
    entry<AudioLibraryGraph> {
        AudioLibraryScreen(
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateToAlbum = onNavigateToAlbum,
            onNavigateToTrackEditor = onNavigateToTrackMetadata,
            onNavigateToAlbumEditor = onNavigateToAlbumMetadata,
        )
    }
}
