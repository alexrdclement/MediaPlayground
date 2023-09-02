package com.alexrdclement.mediaplayground.feature.spotify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.ui.components.TrackCard
import com.alexrdclement.ui.shared.util.PreviewTracks
import com.alexrdclement.ui.theme.MediaPlaygroundTheme

@Composable
fun SpotifyLibraryScreen(
    savedTracks: List<Track>,
    onPlayTrack: (Track) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            items(
                items = savedTracks,
                key = { it.id }
            ) { track ->
                TrackCard(
                    track = track,
                    onPlayClick = { onPlayTrack(track) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MediaPlaygroundTheme {
        SpotifyLibraryScreen(
            savedTracks = PreviewTracks,
            onPlayTrack = {},
        )
    }
}
