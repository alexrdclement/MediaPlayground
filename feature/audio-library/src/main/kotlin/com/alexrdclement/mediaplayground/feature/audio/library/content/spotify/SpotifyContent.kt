package com.alexrdclement.mediaplayground.feature.audio.library.content.spotify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexrdclement.mediaplayground.feature.audio.library.content.AudioLibraryContent
import com.alexrdclement.mediaplayground.ui.components.MediaItemRow
import com.alexrdclement.mediaplayground.ui.components.MediaItemWidthCompact
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewAlbumsUi1
import com.alexrdclement.mediaplayground.ui.util.PreviewTracksUi1
import com.alexrdclement.palette.components.auth.AuthButton
import com.alexrdclement.palette.components.auth.AuthButtonStyle
import com.alexrdclement.palette.components.auth.AuthState
import com.alexrdclement.palette.theme.PaletteTheme
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun SpotifyContent(
    spotifyContentState: SpotifyContentState,
    onLogInClick: () -> Unit,
    onLogOutClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = PaletteTheme.spacing.medium),
) {
    AudioLibraryContent(
        headerText = "Spotify",
        headerPadding = contentPadding,
        headerAction = {
            when (spotifyContentState) {
                is SpotifyContentState.LoggedIn -> {
                    AuthButton(
                        authState = AuthState.LoggedIn,
                        style = AuthButtonStyle.Secondary,
                        onClick = onLogOutClick,
                    )
                }

                SpotifyContentState.NotLoggedIn -> {}
            }
        },
    ) {
        when (spotifyContentState) {
            SpotifyContentState.NotLoggedIn -> SpotifyNotLoggedInContent(
                onLogInClick = onLogInClick,
            )

            is SpotifyContentState.LoggedIn -> SpotifyLoggedInContent(
                spotifyContentState = spotifyContentState,
                onItemClick = onItemClick,
                onItemPlayPauseClick = onItemPlayPauseClick,
                contentPadding = contentPadding,
            )
        }
    }
}

@Composable
private fun SpotifyLoggedInContent(
    spotifyContentState: SpotifyContentState.LoggedIn,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    contentPadding: PaddingValues,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.medium),
        modifier = Modifier
            .fillMaxSize()
    ) {
        val savedAlbums = spotifyContentState.savedAlbums.collectAsLazyPagingItems()
        val savedTracks = spotifyContentState.savedTracks.collectAsLazyPagingItems()
        MediaItemRow(
            mediaItems = savedAlbums,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            title = "Saved albums",
            itemWidth = MediaItemWidthCompact,
            contentPadding = contentPadding,
            modifier = Modifier
        )
        MediaItemRow(
            mediaItems = savedTracks,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            title = "Saved tracks",
            itemWidth = MediaItemWidthCompact,
            contentPadding = contentPadding,
            modifier = Modifier
        )
    }
}

@Composable
private fun SpotifyNotLoggedInContent(
    onLogInClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        AuthButton(
            authState = AuthState.LoggedOut,
            style = AuthButtonStyle.Secondary,
            onClick = onLogInClick,
        )
    }
}

@Preview
@Composable
private fun LoggedInPreview() {
    PaletteTheme {
        val spotifyContentState = SpotifyContentState.LoggedIn(
            savedTracks = flowOf(PagingData.from(PreviewTracksUi1)),
            savedAlbums = flowOf(PagingData.from(PreviewAlbumsUi1)),
        )
        SpotifyContent(
            spotifyContentState = spotifyContentState,
            onLogInClick = {},
            onLogOutClick = {},
            onItemClick = {},
            onItemPlayPauseClick = {},
        )
    }
}

@Preview
@Composable
private fun LoggedOutPreview() {
    PaletteTheme {
        val spotifyContentState = SpotifyContentState.NotLoggedIn
        SpotifyContent(
            spotifyContentState = spotifyContentState,
            onLogInClick = {},
            onLogOutClick = {},
            onItemClick = {},
            onItemPlayPauseClick = {},
        )
    }
}
