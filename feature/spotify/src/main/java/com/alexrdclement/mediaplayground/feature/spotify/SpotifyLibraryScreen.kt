package com.alexrdclement.mediaplayground.feature.spotify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.alexrdclement.ui.components.TrackCard
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
                title = { Text(text = "Saved Tracks") },
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(
                    count = savedTracks.itemCount,
                    key = savedTracks.itemKey { it.id },
                ) { trackIndex ->
                    val track = savedTracks[trackIndex] ?: return@items
                    TrackCard(
                        track = track,
                        onPlayClick = { onPlayTrack(track) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (savedTracks.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator()
                    }
                }
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
