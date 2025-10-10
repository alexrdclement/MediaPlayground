package com.alexrdclement.mediaplayground.ui.components.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.ui.model.TrackUi

@Composable
fun TrackList(
    tracks: List<TrackUi>,
    onTrackClick: (TrackUi) -> Unit,
    onPlayPauseClick: (TrackUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        for (trackUi in tracks) {
            TrackListItem(
                track = trackUi.track,
                isLoaded = trackUi.isLoaded,
                isPlayable = trackUi.isPlayable,
                isPlaying = trackUi.isPlaying,
                onClick = { onTrackClick(trackUi) },
                onPlayPauseClick = { onPlayPauseClick(trackUi) },
            )
        }
    }
}
