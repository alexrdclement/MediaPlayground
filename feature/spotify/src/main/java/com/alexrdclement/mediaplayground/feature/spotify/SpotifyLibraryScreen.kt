package com.alexrdclement.mediaplayground.feature.spotify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.ui.components.TrackCardWide
import com.alexrdclement.ui.components.TrackRow
import com.alexrdclement.ui.shared.util.PreviewTracks
import com.alexrdclement.ui.theme.MediaPlaygroundTheme
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyLibraryScreen(
    isLoggedIn: Boolean,
    savedTracks: LazyPagingItems<Track>,
    onPlayTrack: (Track) -> Unit,
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
                TrackRow(
                    tracks = savedTracks,
                    onPlayClick = onPlayTrack,
                    title = "Saved tracks",
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    itemWidth = 200.dp,
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
        val pagingData = flowOf(PagingData.from(PreviewTracks))
        SpotifyLibraryScreen(
            isLoggedIn = true,
            savedTracks = pagingData.collectAsLazyPagingItems(),
            onPlayTrack = {},
            onLogInClick = {},
            onLogOutClick = {},
        )
    }
}
