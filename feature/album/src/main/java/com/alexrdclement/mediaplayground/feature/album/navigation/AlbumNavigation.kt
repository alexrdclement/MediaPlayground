package com.alexrdclement.mediaplayground.feature.album.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alexrdclement.mediaplayground.feature.album.AlbumScreen

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
        AlbumScreen()
    }
}
