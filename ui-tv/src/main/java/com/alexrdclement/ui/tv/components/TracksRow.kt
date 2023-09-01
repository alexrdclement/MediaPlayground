package com.alexrdclement.ui.tv.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.ui.shared.util.PreviewTracks
import com.alexrdclement.ui.tv.theme.MediaPlaygroundTheme

@Composable
fun TracksRow(
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit,
) {
   TvLazyRow {
       items(
           items = tracks,
           key = { it.id }
       ) { track ->
           TrackCard(
               track = track,
               onClick = { onTrackClick(track) },
               modifier = Modifier.size(300.dp).padding(16.dp)
           )
       }
   }
}

@Preview
@Composable
private fun Preview() {
    MediaPlaygroundTheme {
        TracksRow(
            tracks = PreviewTracks,
            onTrackClick = {}
        )
    }
}
