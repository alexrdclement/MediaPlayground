package com.alexrdclement.mediaplayground.feature.spotify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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

@Composable
fun SpotifyLibraryScreen(
    savedTracks: LazyPagingItems<Track>,
    onPlayTrack: (Track) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
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

@Preview
@Composable
private fun Preview() {
    MediaPlaygroundTheme {
        val pagingData = flowOf(PagingData.from(PreviewTracks))
        SpotifyLibraryScreen(
            savedTracks = pagingData.collectAsLazyPagingItems(),
            onPlayTrack = {},
        )
    }
}
