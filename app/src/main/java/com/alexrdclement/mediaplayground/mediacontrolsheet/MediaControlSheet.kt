package com.alexrdclement.mediaplayground.mediacontrolsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.uiplayground.components.Artist
import com.alexrdclement.uiplayground.components.MediaControlSheetState
import com.alexrdclement.uiplayground.components.MediaItem
import kotlinx.coroutines.launch
import com.alexrdclement.uiplayground.components.MediaControlSheet as MediaControlSheetComponent

@Composable
fun MediaControlSheet(
   mediaControlSheetState: MediaControlSheetState
) {
    val viewModel = hiltViewModel<MediaControlSheetViewModel>()
    val loadedMediaItem by viewModel.loadedMediaItem.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = loadedMediaItem != null,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = Modifier
    ) {
        loadedMediaItem?.let { mediaItem ->
            // TODO: temp
            val artists by derivedStateOf { mediaItem.artists.map { Artist(name = it.name) } }
            val uiMediaItem by derivedStateOf { MediaItem(title = mediaItem.title, artists = artists) }

            MediaControlSheetComponent(
                mediaItem = uiMediaItem,
                isPlaying = isPlaying,
                onPlayPauseClick = viewModel::onPlayPauseClick,
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
                modifier = Modifier.systemBarsPadding()
            ) {
                Surface {
                    MediaControlSheetContent(
                        mediaItem = uiMediaItem,
                        isPlaying = isPlaying,
                        onPlayPauseClick = viewModel::onPlayPauseClick,
                        modifier = Modifier.graphicsLayer {
                            alpha = mediaControlSheetState.partialToFullProgress
                        }
                    )
                }
            }
        }
    }

}
