package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.alexrdclement.mediaplayground.ui.theme.component.media.mediaItemRow
import com.alexrdclement.mediaplayground.ui.util.PreviewTracksUi1
import com.embarrasdf.palette.components.core.IndeterminateProgressIndicator
import com.embarrasdf.palette.components.core.ProgressIndicatorStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.core.Surface
import kotlinx.coroutines.flow.flowOf

data class MediaItemRowStyle(
    val contentPadding: PaddingValues = PaddingValues(0.dp),
    val contentSpacing: Dp = 16.dp,
    val itemSpacing: Dp = 16.dp,
    val itemWidth: Dp = 280.dp,
    val titleStyle: TextStyle = TextStyle(),
    val itemStyle: MediaItemCardStyle = MediaItemCardStyle(),
    val progressIndicatorStyle: ProgressIndicatorStyle = ProgressIndicatorStyle(),
)

@Composable
fun MediaItemRow(
    mediaItems: LazyPagingItems<MediaItemUi>,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    modifier: Modifier = Modifier,
    style: MediaItemRowStyle = MediaItemRowStyle(),
    lazyListState: LazyListState = rememberLazyListState(),
    title: String? = null,
    onItemLongClick: ((MediaItemUi) -> Unit)? = null,
    itemOverlayContent: (@Composable BoxScope.(MediaItemUi, Boolean, Offset, () -> Unit) -> Unit)? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(style.contentSpacing),
        modifier = modifier
    ) {
        if (title != null) {
            Text(
                text = title,
                style = style.titleStyle,
                modifier = Modifier.padding(style.contentPadding),
            )
        }

        LazyRow(
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(style.itemSpacing),
            contentPadding = style.contentPadding,
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
                            modifier = Modifier.width(style.itemWidth),
                            style = style.itemStyle,
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
                        modifier = Modifier.width(style.itemWidth),
                        style = style.itemStyle,
                    )
                }
            }

            if (mediaItems.loadState.append == LoadState.Loading) {
                item {
                    IndeterminateProgressIndicator(style = style.progressIndicatorStyle)
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
                style = PaletteTheme.component.media.mediaItemRow,
            )
        }
    }
}
