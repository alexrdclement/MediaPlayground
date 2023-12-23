package com.alexrdclement.ui.components.mediacontrolsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.uiplayground.components.Artist
import com.alexrdclement.uiplayground.components.MediaControlSheetState
import kotlinx.coroutines.launch
import com.alexrdclement.uiplayground.components.MediaControlSheet as MediaControlSheetComponent
import com.alexrdclement.uiplayground.components.MediaItem as UiMediaItem

private val MediaControlSheetPartialExpandHeight = 64.dp

fun Modifier.mediaControlSheetPadding(isMediaItemLoaded: Boolean) = this.then(
    if (isMediaItemLoaded) {
        Modifier.padding(bottom = MediaControlSheetPartialExpandHeight)
    } else {
        Modifier
    }
)

@Composable
fun MediaControlSheet(
    mediaControlSheetState: MediaControlSheetState,
    loadedMediaItem: MediaItem?,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = loadedMediaItem != null,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        loadedMediaItem?.let { mediaItem ->
            // TODO: temp
            val artists by derivedStateOf { mediaItem.artists.map { Artist(name = it.name) } }
            val uiMediaItem by derivedStateOf {
                UiMediaItem(
                    title = mediaItem.title,
                    artists = artists
                )
            }

            MediaControlSheetComponent(
                mediaItem = uiMediaItem,
                isPlaying = isPlaying,
                onPlayPauseClick = onPlayPauseClick,
                state = mediaControlSheetState,
                onControlBarClick = {
                    coroutineScope.launch {
                        if (mediaControlSheetState.isExpanded) {
                            mediaControlSheetState.partialExpand()
                        } else {
                            mediaControlSheetState.expand()
                        }
                    }
                },
                minHeight = MediaControlSheetPartialExpandHeight,
                modifier = Modifier.systemBarsPadding()
            ) {
                Surface {
                    MediaControlSheetContent(
                        mediaItem = uiMediaItem,
                        isPlaying = isPlaying,
                        onPlayPauseClick = onPlayPauseClick,
                        modifier = Modifier.graphicsLayer {
                            alpha = mediaControlSheetState.partialToFullProgress
                        }
                    )
                }
            }
        }
    }
}
