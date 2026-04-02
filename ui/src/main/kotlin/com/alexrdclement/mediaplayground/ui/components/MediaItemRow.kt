package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewTracksUi1
import com.alexrdclement.palette.components.core.IndeterminateProgressIndicator
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.theme.PaletteTheme
import kotlinx.coroutines.flow.flowOf

val MediaItemWidthDefault = 280.dp
val MediaItemWidthCompact = 200.dp

@Composable
fun MediaItemRow(
    mediaItems: LazyPagingItems<MediaItemUi>,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
    itemWidth: Dp = MediaItemWidthDefault,
    title: String? = null,
    onItemLongClick: ((MediaItemUi) -> Unit)? = null,
    itemOverlayContent: (@Composable BoxScope.(MediaItemUi, Boolean, Offset, () -> Unit) -> Unit)? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        if (title != null) {
            Text(
                text = title,
                style = PaletteTheme.styles.text.titleMedium,
                modifier = Modifier.padding(contentPadding),
            )
        }

        LazyRow(
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = contentPadding,
            modifier = Modifier,
        ) {
            items(
                count = mediaItems.itemCount,
                key = mediaItems.itemKey { it.mediaItem.id.value }
            ) { index ->
                val mediaItem = mediaItems[index] ?: return@items
                if (itemOverlayContent != null) {
                    var dropdownExpanded by remember { mutableStateOf(false) }
                    var touchOffset by remember { mutableStateOf(Offset.Zero) }
                    Box {
                        MediaItemCard(
                            mediaItem = mediaItem.mediaItem,
                            isPlaybackEnabled = mediaItem.mediaItem.isPlayable,
                            isPlaying = mediaItem.isPlaying,
                            onClick = { onItemClick(mediaItem) },
                            onPlayPauseClick = { onItemPlayPauseClick(mediaItem) },
                            onLongClick = { offset: Offset ->
                                touchOffset = offset
                                dropdownExpanded = true
                                onItemLongClick?.invoke(mediaItem)
                            },
                            modifier = Modifier.width(itemWidth),
                        )
                        if (dropdownExpanded) {
                            itemOverlayContent(this, mediaItem, true, touchOffset) { dropdownExpanded = false }
                        }
                    }
                } else {
                    MediaItemCard(
                        mediaItem = mediaItem.mediaItem,
                        isPlaybackEnabled = mediaItem.mediaItem.isPlayable,
                        isPlaying = mediaItem.isPlaying,
                        onClick = { onItemClick(mediaItem) },
                        onPlayPauseClick = { onItemPlayPauseClick(mediaItem) },
                        onLongClick = onItemLongClick?.let { { it(mediaItem) } },
                        modifier = Modifier.width(itemWidth),
                    )
                }
            }

            if (mediaItems.loadState.append == LoadState.Loading) {
                item {
                    IndeterminateProgressIndicator()
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
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
