package com.alexrdclement.mediaplayground.mediacontrolsheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.ui.components.mediacontrolsheet.MediaControlSheet
import com.alexrdclement.uiplayground.components.MediaControlSheetState

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
