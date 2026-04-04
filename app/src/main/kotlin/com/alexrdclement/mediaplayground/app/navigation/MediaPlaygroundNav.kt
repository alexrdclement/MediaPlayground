package com.alexrdclement.mediaplayground.app.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.alexrdclement.mediaplayground.app.catalog.MainCatalogItem
import com.alexrdclement.mediaplayground.app.catalog.navigation.MainCatalogGraph
import com.alexrdclement.mediaplayground.app.catalog.navigation.mainCatalogEntryProvider
import com.alexrdclement.mediaplayground.app.catalog.navigation.mainCatalogNavGraph
import com.alexrdclement.mediaplayground.feature.album.navigation.AlbumDeleteRoute
import com.alexrdclement.mediaplayground.feature.album.navigation.AlbumMetadataRoute
import com.alexrdclement.mediaplayground.feature.album.navigation.AlbumRoute
import com.alexrdclement.mediaplayground.feature.album.navigation.albumEntryProvider
import com.alexrdclement.mediaplayground.feature.album.navigation.albumNavGraph
import com.alexrdclement.mediaplayground.feature.artist.navigation.ArtistDeleteRoute
import com.alexrdclement.mediaplayground.feature.artist.navigation.ArtistMetadataRoute
import com.alexrdclement.mediaplayground.feature.artist.navigation.artistMetadataEntryProvider
import com.alexrdclement.mediaplayground.feature.artist.navigation.artistMetadataNavGraph
import com.alexrdclement.mediaplayground.feature.audio.library.navigation.AudioLibraryGraph
import com.alexrdclement.mediaplayground.feature.audio.library.navigation.audioLibraryEntryProvider
import com.alexrdclement.mediaplayground.feature.audio.library.navigation.audioLibraryNavGraph
import com.alexrdclement.mediaplayground.feature.camera.navigation.CameraGraph
import com.alexrdclement.mediaplayground.feature.camera.navigation.cameraEntryProvider
import com.alexrdclement.mediaplayground.feature.camera.navigation.cameraNavGraph
import com.alexrdclement.mediaplayground.feature.error.navigation.ErrorGraph
import com.alexrdclement.mediaplayground.feature.error.navigation.errorEntryProvider
import com.alexrdclement.mediaplayground.feature.error.navigation.errorNavGraph
import com.alexrdclement.mediaplayground.feature.image.library.navigation.ImageLibraryGraph
import com.alexrdclement.mediaplayground.feature.image.library.navigation.imageLibraryEntryProvider
import com.alexrdclement.mediaplayground.feature.image.library.navigation.imageLibraryNavGraph
import com.alexrdclement.mediaplayground.feature.image.navigation.ImageDeleteRoute
import com.alexrdclement.mediaplayground.feature.image.navigation.ImageMetadataRoute
import com.alexrdclement.mediaplayground.feature.image.navigation.imageMetadataEntryProvider
import com.alexrdclement.mediaplayground.feature.image.navigation.imageMetadataNavGraph
import com.alexrdclement.mediaplayground.feature.media.control.MediaControlSheet
import com.alexrdclement.mediaplayground.feature.playback.control.navigation.PlaybackControlRoute
import com.alexrdclement.mediaplayground.feature.playback.control.navigation.playbackControlEntryProvider
import com.alexrdclement.mediaplayground.feature.playback.control.navigation.playbackControlNavGraph
import com.alexrdclement.mediaplayground.feature.player.navigation.PlayerGraph
import com.alexrdclement.mediaplayground.feature.player.navigation.playerEntryProvider
import com.alexrdclement.mediaplayground.feature.player.navigation.playerNavGraph
import com.alexrdclement.mediaplayground.feature.track.navigation.TrackDeleteRoute
import com.alexrdclement.mediaplayground.feature.track.navigation.TrackMetadataRoute
import com.alexrdclement.mediaplayground.feature.track.navigation.trackMetadataEntryProvider
import com.alexrdclement.mediaplayground.feature.track.navigation.trackMetadataNavGraph
import com.alexrdclement.palette.components.media.MediaControlSheetState
import com.alexrdclement.palette.components.media.rememberMediaControlSheetState
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphRoute
import com.alexrdclement.palette.navigation.NavKey
import com.alexrdclement.palette.navigation.navGraph
import com.alexrdclement.palette.navigation.rememberNavController
import com.alexrdclement.palette.navigation.rememberNavState
import com.alexrdclement.palette.navigation.toPathSegment
import com.alexrdclement.uievent.UiEventState
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("media-playground")
data object MediaPlaygroundGraph : NavGraphRoute {
    override val pathSegment = "".toPathSegment()
}

val MediaPlaygroundNavGraph = navGraph(
    root = MediaPlaygroundGraph,
    start = MainCatalogGraph,
) {
    mainCatalogNavGraph()
    audioLibraryNavGraph()
    albumNavGraph()
    trackMetadataNavGraph()
    artistMetadataNavGraph()
    imageLibraryNavGraph()
    imageMetadataNavGraph()
    playerNavGraph()
    cameraNavGraph()
    errorNavGraph()
    playbackControlNavGraph()
}

@Composable
fun rememberMediaPlaygroundNavController(
    initialDeeplink: String? = null,
    buildSyntheticBackStack: Boolean = true,
    onBackStackEmpty: () -> Unit = {},
) = rememberNavController(
    state = rememberNavState(
        navGraph = MediaPlaygroundNavGraph,
        initialDeeplink = initialDeeplink,
        buildSyntheticBackStack = buildSyntheticBackStack,
        onWouldBecomeEmpty = onBackStackEmpty,
    ),
)

@Composable
fun MediaPlaygroundNav(
    errorMessages: UiEventState<String> = UiEventState(),
    navController: NavController = rememberMediaPlaygroundNavController(),
) {
    val coroutineScope = rememberCoroutineScope()
    val mediaControlSheetState = rememberMediaControlSheetState()

    BackHandler(enabled = mediaControlSheetState.isExpanded) {
        coroutineScope.launch {
            mediaControlSheetState.partialExpand()
        }
    }

    LaunchedEffect(errorMessages) {
        errorMessages.collect { message ->
            navController.navigate(ErrorGraph(message))
        }
    }

    NavDisplay(
        backStack = navController.state.backStack,
        sceneStrategies = listOf(
            DialogSceneStrategy(),
            SinglePaneSceneStrategy(),
        ),
        entryProvider = entryProvider {
            mediaPlaygroundEntryProvider(
                navController = navController,
                coroutineScope = coroutineScope,
                mediaControlSheetState = mediaControlSheetState,
            )
        },
        onBack = navController::goBack,
    )

    MediaControlSheet(
        mediaControlSheetState = mediaControlSheetState,
        onNavigateToTrackMetadata = { trackId ->
            coroutineScope.launch { mediaControlSheetState.partialExpand() }
            navController.navigate(TrackMetadataRoute(trackIdValue = trackId))
        },
        onNavigateToTrackDelete = { trackId, displayName ->
            navController.navigate(TrackDeleteRoute(trackIdValue = trackId, displayName = displayName))
        },
        onNavigateToArtistMetadata = { artistId ->
            coroutineScope.launch { mediaControlSheetState.partialExpand() }
            navController.navigate(ArtistMetadataRoute(artistIdValue = artistId))
        },
        onNavigateToArtistDelete = { artistId, displayName ->
            navController.navigate(ArtistDeleteRoute(artistIdValue = artistId, displayName = displayName))
        },
        onNavigateToPlaybackControl = {
            navController.navigate(PlaybackControlRoute)
        },
    )
}

fun EntryProviderScope<NavKey>.mediaPlaygroundEntryProvider(
    navController: NavController,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    mediaControlSheetState: MediaControlSheetState,
) {
    mainCatalogEntryProvider(
        navController = navController,
        onItemClick = { item ->
            when (item) {
                MainCatalogItem.AudioLibrary -> navController.navigate(AudioLibraryGraph)
                MainCatalogItem.Camera -> navController.navigate(CameraGraph)
                MainCatalogItem.ImageLibrary -> navController.navigate(ImageLibraryGraph)
                MainCatalogItem.Player -> navController.navigate(PlayerGraph)
            }
        },
    )
    audioLibraryEntryProvider(
        navController = navController,
        onNavigateToPlayer = { _ ->
            coroutineScope.launch {
                mediaControlSheetState.expand()
            }
        },
        onNavigateToAlbum = { album ->
            navController.navigate(AlbumRoute(albumIdValue = album.id.value))
        },
        onNavigateToTrackMetadata = { trackId ->
            navController.navigate(TrackMetadataRoute(trackIdValue = trackId.value))
        },
        onNavigateToAlbumMetadata = { albumId ->
            navController.navigate(AlbumMetadataRoute(albumIdValue = albumId.value))
        },
        onNavigateToAlbumDelete = { albumId, displayName ->
            navController.navigate(AlbumDeleteRoute(albumIdValue = albumId, displayName = displayName))
        },
        onNavigateToTrackDelete = { trackId, displayName ->
            navController.navigate(TrackDeleteRoute(trackIdValue = trackId, displayName = displayName))
        },
    )
    albumEntryProvider(
        navController = navController,
        onNavigateToAlbumMetadata = { albumId ->
            navController.navigate(AlbumMetadataRoute(albumIdValue = albumId.value))
        },
        onNavigateToAlbumDelete = { albumId, displayName ->
            navController.navigate(AlbumDeleteRoute(albumIdValue = albumId, displayName = displayName))
        },
        onNavigateToArtistMetadata = { artistId ->
            navController.navigate(ArtistMetadataRoute(artistIdValue = artistId))
        },
        onNavigateToArtistDelete = { artistId, displayName ->
            navController.navigate(ArtistDeleteRoute(artistIdValue = artistId, displayName = displayName))
        },
        onNavigateToTrackMetadata = { trackId ->
            navController.navigate(TrackMetadataRoute(trackIdValue = trackId))
        },
        onNavigateToTrackDelete = { trackId, displayName ->
            navController.navigate(TrackDeleteRoute(trackIdValue = trackId, displayName = displayName))
        },
        onNavigateToImageMetadata = { imageId ->
            navController.navigate(ImageMetadataRoute(imageIdValue = imageId.value))
        },
    )
    trackMetadataEntryProvider(
        navController = navController,
        onNavigateToTrackDelete = { trackId, displayName ->
            navController.navigate(TrackDeleteRoute(trackIdValue = trackId, displayName = displayName))
        },
        onNavigateToArtistMetadata = { artistId ->
            navController.navigate(ArtistMetadataRoute(artistIdValue = artistId))
        },
    )
    artistMetadataEntryProvider(
        navController = navController,
        onNavigateToArtistDelete = { artistId, displayName ->
            navController.navigate(ArtistDeleteRoute(artistIdValue = artistId, displayName = displayName))
        },
    )
    imageLibraryEntryProvider(
        navController = navController,
        onNavigateToImageMetadata = { imageId ->
            navController.navigate(ImageMetadataRoute(imageIdValue = imageId.value))
        },
        onNavigateToImageDelete = { imageId, displayName ->
            navController.navigate(ImageDeleteRoute(imageIdValue = imageId, displayName = displayName))
        },
    )
    imageMetadataEntryProvider(
        navController = navController,
        onNavigateToImageDelete = { imageId, displayName ->
            navController.navigate(ImageDeleteRoute(imageIdValue = imageId, displayName = displayName))
        },
    )
    playbackControlEntryProvider(navController)
    playerEntryProvider(navController)
    cameraEntryProvider(navController)
    errorEntryProvider(navController)
}
