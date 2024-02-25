package com.alexrdclement.mediaplayground.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.alexrdclement.mediaplayground.feature.album.navigation.albumScreen
import com.alexrdclement.mediaplayground.feature.album.navigation.navigateToAlbum
import com.alexrdclement.mediaplayground.feature.audio.library.navigation.audioLibraryScreen
import com.alexrdclement.mediaplayground.feature.player.navigation.playerScreen
import com.alexrdclement.mediaplayground.feature.spotify.navigation.spotifyLibraryScreen
import com.alexrdclement.mediaplayground.feature.media.control.MediaControlSheet
import com.alexrdclement.uiplayground.components.rememberMediaControlSheetState
import kotlinx.coroutines.launch

@Composable
fun MediaPlaygroundNavHost(
    contentPadding: PaddingValues,
) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val mediaControlSheetState = rememberMediaControlSheetState()

    BackHandler(enabled = mediaControlSheetState.isExpanded) {
        coroutineScope.launch {
            mediaControlSheetState.partialExpand()
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
    }

    MediaControlSheet(
        mediaControlSheetState = mediaControlSheetState,
    )
}
