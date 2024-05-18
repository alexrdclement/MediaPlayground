package com.alexrdclement.mediaplayground.feature.media.control

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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.largeImageUrl
import com.alexrdclement.mediaplayground.model.audio.thumbnailImageUrl
import com.alexrdclement.mediaplayground.ui.R
import com.alexrdclement.mediaplayground.ui.constants.MediaControlSheetPartialExpandHeight
import com.alexrdclement.uiplayground.components.MediaControlSheetState
import com.alexrdclement.uiplayground.components.model.Artist
import kotlinx.coroutines.launch
import com.alexrdclement.uiplayground.components.MediaControlSheet as MediaControlSheetComponent

@Composable
fun MediaControlSheet(
    mediaControlSheetState: MediaControlSheetState
) {
    val viewModel = hiltViewModel<MediaControlSheetViewModel>()
    val loadedMediaItem by viewModel.loadedMediaItem.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()

    MediaControlSheet(
        mediaControlSheetState = mediaControlSheetState,
        loadedMediaItem = loadedMediaItem,
        isPlaying = isPlaying,
        onPlayPauseClick = viewModel::onPlayPauseClick,
    )
}

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
            val fallbackArtistName = stringResource(id = R.string.artist_name_fallback)
            val artists by derivedStateOf { mediaItem.artists.map { Artist(name = it.name ?: fallbackArtistName) } }
            val uiMediaItem by derivedStateOf {
                com.alexrdclement.uiplayground.components.model.MediaItem(
                    artworkLargeUrl = mediaItem.largeImageUrl,
                    artworkThumbnailUrl = mediaItem.thumbnailImageUrl,
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
                        loadedMediaItem = uiMediaItem,
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
