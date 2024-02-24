package com.alexrdclement.mediaplayground.feature.audio.library.content.spotify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexrdclement.mediaplayground.feature.audio.library.content.AudioLibraryContent
import com.alexrdclement.ui.components.MediaItemRow
import com.alexrdclement.ui.components.MediaItemWidthCompact
import com.alexrdclement.ui.components.spotify.SpotifyAuthButton
import com.alexrdclement.ui.components.spotify.SpotifyAuthButtonStyle
import com.alexrdclement.ui.shared.model.MediaItemUi
import com.alexrdclement.ui.shared.util.PreviewAlbumsUi1
import com.alexrdclement.ui.shared.util.PreviewTracksUi1
import com.alexrdclement.ui.theme.MediaPlaygroundTheme
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun SpotifyContent(
    spotifyContentState: SpotifyContentState,
    onLogInClick: () -> Unit,
    onLogOutClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    AudioLibraryContent(
        headerText = "Spotify",
        headerPadding = contentPadding,
        headerAction = {
            when (spotifyContentState) {
                is SpotifyContentState.LoggedIn -> {
                    SpotifyAuthButton(
                        style = SpotifyAuthButtonStyle.Compact,
                        isLoggedIn = true,
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
        Spacer(modifier = Modifier.height(16.dp))
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
fun SpotifyNotLoggedInContent(
    onLogInClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        SpotifyAuthButton(
            isLoggedIn = false,
            onClick = onLogInClick,
        )
    }
}

@Preview
@Composable
private fun LoggedInPreview() {
    MediaPlaygroundTheme {
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
    MediaPlaygroundTheme {
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
