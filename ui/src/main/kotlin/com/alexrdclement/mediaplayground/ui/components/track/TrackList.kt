package com.alexrdclement.mediaplayground.ui.components.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.ui.model.TrackUi
import com.alexrdclement.mediaplayground.ui.util.calculateHorizontalPadding

@Composable
fun TrackList(
    tracks: List<TrackUi>,
    onTrackClick: (TrackUi) -> Unit,
    onPlayPauseClick: (TrackUi) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .padding(horizontal = contentPadding.calculateHorizontalPadding())
    ) {
        Spacer(modifier = Modifier.size(contentPadding.calculateTopPadding()))
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
        Spacer(modifier = Modifier.size(contentPadding.calculateBottomPadding()))
    }
}
