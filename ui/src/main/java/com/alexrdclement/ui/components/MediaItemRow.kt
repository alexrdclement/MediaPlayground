package com.alexrdclement.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.alexrdclement.ui.shared.model.MediaItemUi
import com.alexrdclement.ui.shared.util.PreviewTracksUi1
import com.alexrdclement.ui.theme.MediaPlaygroundTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun MediaItemRow(
    mediaItems: LazyPagingItems<MediaItemUi>,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    itemWidth: Dp = 280.dp,
    title: String? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = contentPadding,
            modifier = Modifier,
        ) {
            items(
                count = mediaItems.itemCount,
                key = mediaItems.itemKey { it.mediaItem.id }
            ) { index ->
                val mediaItem = mediaItems[index] ?: return@items
                MediaItemCardTall(
                    mediaItem = mediaItem.mediaItem,
                    isEnabled = mediaItem.mediaItem.isPlayable,
                    isPlaying = mediaItem.isPlaying,
                    onClick = { onItemClick(mediaItem) },
                    onPlayPauseClick = { onItemPlayPauseClick(mediaItem) },
                    modifier = Modifier
                        .width(itemWidth)
                )
            }

            if (mediaItems.loadState.append == LoadState.Loading) {
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
        Surface {
            val tracks = flowOf(PagingData.from(PreviewTracksUi1))
            MediaItemRow(
                title = "Saved tracks",
                mediaItems = tracks.collectAsLazyPagingItems(),
                onItemClick = {},
                onItemPlayPauseClick = {},
                modifier = Modifier
                    .height(360.dp)
                    .padding(16.dp)
            )
        }
    }
}
