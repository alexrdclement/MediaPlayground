package com.alexrdclement.mediaplayground.app.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.alexrdclement.mediaplayground.app.catalog.MainCatalogItem
import com.alexrdclement.mediaplayground.app.catalog.navigation.MainCatalogRoute
import com.alexrdclement.mediaplayground.app.catalog.navigation.mainCatalogScreen
import com.alexrdclement.mediaplayground.feature.album.navigation.albumScreen
import com.alexrdclement.mediaplayground.feature.album.navigation.navigateToAlbum
import com.alexrdclement.mediaplayground.feature.audio.library.navigation.audioLibraryScreen
import com.alexrdclement.mediaplayground.feature.audio.library.navigation.navigateToAudioLibrary
import com.alexrdclement.mediaplayground.feature.camera.navigation.cameraScreen
import com.alexrdclement.mediaplayground.feature.camera.navigation.navigateToCamera
import com.alexrdclement.mediaplayground.feature.error.navigation.errorDialog
import com.alexrdclement.mediaplayground.feature.error.navigation.navigateToError
import com.alexrdclement.mediaplayground.feature.media.control.MediaControlSheet
import com.alexrdclement.mediaplayground.feature.player.navigation.navigateToPlayer
import com.alexrdclement.mediaplayground.feature.player.navigation.playerScreen
import com.alexrdclement.mediaplayground.feature.spotify.navigation.navigateToSpotifyLibrary
import com.alexrdclement.mediaplayground.feature.spotify.navigation.spotifyLibraryScreen
import com.alexrdclement.uievent.UiEventState
import com.alexrdclement.palette.components.media.rememberMediaControlSheetState
import kotlinx.coroutines.launch

@Composable
fun NavHost(
    errorMessages: UiEventState<String> = UiEventState(),
) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val mediaControlSheetState = rememberMediaControlSheetState()

    BackHandler(enabled = mediaControlSheetState.isExpanded) {
        coroutineScope.launch {
            mediaControlSheetState.partialExpand()
        }
    }

    LaunchedEffect(
        errorMessages,
    ) {
        errorMessages.collect { message ->
            navController.navigateToError(message)
        }
    }

    NavHost(
        navController = navController,
        startDestination = MainCatalogRoute,
    ) {
        mainCatalogScreen(
            onItemClick = { item ->
                when (item) {
                    MainCatalogItem.AudioLibrary -> navController.navigateToAudioLibrary()
                    MainCatalogItem.Camera -> navController.navigateToCamera()
                    MainCatalogItem.SpotifyLibrary -> navController.navigateToSpotifyLibrary()
                    MainCatalogItem.Player -> navController.navigateToPlayer()
                }
            }
        )
        audioLibraryScreen(
            onNavigateToPlayer = { mediaItem ->
                coroutineScope.launch {
                    mediaControlSheetState.expand()
                }
            },
            onNavigateToAlbum = { album ->
                navController.navigateToAlbum(
                    albumId = album.id,
                )
            }
        )
        cameraScreen()
        spotifyLibraryScreen(
            onNavigateToPlayer = { mediaItem ->
                coroutineScope.launch {
                    mediaControlSheetState.expand()
                }
            },
            onNavigateToAlbum = { album ->
                navController.navigateToAlbum(
                    albumId = album.id,
                )
            }
        )
        albumScreen()
        playerScreen()
        errorDialog(
            navController = navController,
        )
    }

    MediaControlSheet(
        mediaControlSheetState = mediaControlSheetState,
    )
}
