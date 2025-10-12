package com.alexrdclement.mediaplayground.feature.media.control

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.uiplayground.theme.PlaygroundTheme
import kotlinx.collections.immutable.PersistentList

@Composable
fun PlaylistContent(
    playlist: PersistentList<MediaItemUi>,
    onItemClick: (MediaItemUi) -> Unit,
    onPlayPauseClick: (MediaItemUi) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(PlaygroundTheme.spacing.small),
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        items(
            items = playlist,
            key = { item -> item.mediaItem.id.value },
        ) { item ->
            PlaylistItem(
                item = item,
                onClick = { onItemClick(item) },
                onPlayPauseClick = { onPlayPauseClick(item) },
            )
        }

    }
}
