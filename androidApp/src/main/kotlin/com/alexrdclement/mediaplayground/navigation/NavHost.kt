package com.alexrdclement.mediaplayground.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.alexrdclement.mediaplayground.feature.album.navigation.albumScreen
import com.alexrdclement.mediaplayground.feature.album.navigation.navigateToAlbum
import com.alexrdclement.mediaplayground.feature.audio.library.navigation.audioLibraryScreen
import com.alexrdclement.mediaplayground.feature.error.navigation.errorDialog
import com.alexrdclement.mediaplayground.feature.error.navigation.navigateToError
import com.alexrdclement.mediaplayground.feature.media.control.MediaControlSheet
import com.alexrdclement.mediaplayground.feature.player.navigation.playerScreen
import com.alexrdclement.mediaplayground.feature.spotify.navigation.spotifyLibraryScreen
import com.alexrdclement.uiplayground.components.media.rememberMediaControlSheetState
import com.alexrdclement.uiplayground.uievent.UiEventState
import kotlinx.coroutines.launch

@Composable
fun NavHost(
    errorMessages: UiEventState<String> = UiEventState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
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
        startDestination = Destination.AudioLibrary.route,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .consumeWindowInsets(contentPadding)
    ) {
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
