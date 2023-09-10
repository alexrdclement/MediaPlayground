package com.alexrdclement.mediaplayground.feature.spotify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.ui.components.MediaItemRow
import com.alexrdclement.ui.shared.util.PreviewAlbums1
import com.alexrdclement.ui.shared.util.PreviewTracks1
import com.alexrdclement.ui.theme.MediaPlaygroundTheme
import kotlinx.coroutines.flow.flowOf

private val MediaItemWidth = 200.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyLibraryScreen(
    isLoggedIn: Boolean,
    savedTracks: LazyPagingItems<MediaItem>,
    savedAlbums: LazyPagingItems<MediaItem>,
    onPlayMediaItem: (MediaItem) -> Unit,
    onLogInClick: () -> Unit,
    onLogOutClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Spotify Library",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                },
                actions = {
                    LogInOutButton(
                        isLoggedIn = isLoggedIn,
                        onClick = {
                            if (isLoggedIn) {
                                onLogOutClick()
                            } else {
                                onLogInClick()
                            }
                        },
                    )
                }
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                MediaItemRow(
                    mediaItems = savedAlbums,
                    onPlayClick = onPlayMediaItem,
                    title = "Saved albums",
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    itemWidth = MediaItemWidth,
                    modifier = Modifier
                )
                MediaItemRow(
                    mediaItems = savedTracks,
                    onPlayClick = onPlayMediaItem,
                    title = "Saved tracks",
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    itemWidth = MediaItemWidth,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun LogInOutButton(
    isLoggedIn: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
    ) {
        if (!isLoggedIn) {
            Text(text = "Log In")
        } else {
            Text(text = "Log Out")
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MediaPlaygroundTheme {
        val savedTracks = flowOf(PagingData.from<MediaItem>(PreviewTracks1)).collectAsLazyPagingItems()
        val savedAlbums = flowOf(PagingData.from<MediaItem>(PreviewAlbums1)).collectAsLazyPagingItems()
        SpotifyLibraryScreen(
            isLoggedIn = true,
            savedTracks = savedTracks,
            savedAlbums = savedAlbums,
            onPlayMediaItem = {},
            onLogInClick = {},
            onLogOutClick = {},
        )
    }
}
