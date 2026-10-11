package com.alexrdclement.mediaplayground.feature.audio.library.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.audio.library.AudioLibraryScreen
import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.MediaItem
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey

fun NavGraphBuilder.audioLibraryNavGraph() {
    route(AudioLibraryGraph)
}

data class AudioLibraryNavigationCallbacks(
    val onNavigateToPlayer: (MediaItem) -> Unit,
    val onNavigateToAlbum: (Album) -> Unit,
    val onNavigateToTrackMetadata: (TrackId) -> Unit = {},
    val onNavigateToAlbumMetadata: (AlbumId) -> Unit = {},
    val onNavigateToAlbumDelete: (albumId: String, displayName: String) -> Unit = { _, _ -> },
    val onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
)

fun EntryProviderScope<NavKey>.audioLibraryEntryProvider(callbacks: AudioLibraryNavigationCallbacks) {
    entry<AudioLibraryGraph> {
        AudioLibraryScreen(
            onNavigateToPlayer = callbacks.onNavigateToPlayer,
            onNavigateToAlbum = callbacks.onNavigateToAlbum,
            onNavigateToTrackMetadata = { trackId -> callbacks.onNavigateToTrackMetadata(TrackId(trackId)) },
            onNavigateToAlbumMetadata = { albumId -> callbacks.onNavigateToAlbumMetadata(AlbumId(albumId)) },
            onNavigateToAlbumDelete = callbacks.onNavigateToAlbumDelete,
            onNavigateToTrackDelete = callbacks.onNavigateToTrackDelete,
        )
    }
}
