package com.alexrdclement.mediaplayground.feature.album.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alexrdclement.mediaplayground.feature.album.AlbumScreen
import com.alexrdclement.mediaplayground.feature.album.AlbumViewModel

private const val AlbumRouteRoot = "album"
internal const val AlbumIdArgKey = "albumId"
const val AlbumRouteTemplate = "$AlbumRouteRoot/{$AlbumIdArgKey}"

fun createAlbumRoute(albumId: String) = "$AlbumRouteRoot/$albumId"

fun NavController.navigateToAlbum(
    albumId: String,
    navOptions: NavOptions? = null
) {
    navigate(createAlbumRoute(albumId), navOptions)
}

fun NavGraphBuilder.albumScreen() {
    composable(
        route = AlbumRouteTemplate,
        arguments = listOf(
            navArgument(AlbumIdArgKey) { type = NavType.StringType },
        ),
    ) {
        val viewModel: AlbumViewModel = hiltViewModel()
        val album by viewModel.album.collectAsStateWithLifecycle()
        AlbumScreen(
            album = album,
            onPlayTrack = viewModel::onPlayTrack,
        )
    }
}
