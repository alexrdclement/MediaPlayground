package com.alexrdclement.mediaplayground.feature.audio.library

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContent
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContentState
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewAlbumsUi1
import com.alexrdclement.mediaplayground.ui.util.PreviewTracksUi1
import com.alexrdclement.palette.components.util.plus
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.components.layout.Scaffold
import com.alexrdclement.palette.components.layout.TopBar
import com.alexrdclement.palette.theme.PaletteTheme
import kotlinx.coroutines.flow.flowOf

private const val MediaPickerAudioMimeType = "audio/*"

@Composable
fun AudioLibraryScreen(
    onNavigateToPlayer: (MediaItem) -> Unit,
    onNavigateToAlbum: (Album) -> Unit,
    viewModel: AudioLibraryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(AudioLibraryUiState.InitialState)
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) {
        viewModel.onMediaImportItemSelected(it)
    }
    AudioLibraryScreen(
        uiState = uiState,
        onImportClick = {
            mediaPickerLauncher.launch(MediaPickerAudioMimeType)
        },
        onItemClick = { mediaItemUi ->
            viewModel.onItemClick(mediaItemUi)

            when (val mediaItem = mediaItemUi.mediaItem) {
                is Album -> {
                    onNavigateToAlbum(mediaItem)
                }

                is Track -> {
                    if (mediaItem.isPlayable) {
                        onNavigateToPlayer(mediaItem)
                    }
                }
            }
        },
        onItemPlayPauseClick = viewModel::onPlayPauseClick,
    )
}

@Composable
fun AudioLibraryScreen(
    uiState: AudioLibraryUiState,
    onImportClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = {
                    Text(
                        text = "Audio Library",
                        style = PaletteTheme.styles.text.headline,
                    )
                }
            )
        },
    ) { innerPadding ->
        when (uiState) {
            AudioLibraryUiState.InitialState -> {}
            is AudioLibraryUiState.ContentReady -> ContentReady(
                uiState = uiState,
                onImportClick = onImportClick,
                onItemClick = onItemClick,
                onItemPlayPauseClick = onItemPlayPauseClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
fun ContentReady(
    uiState: AudioLibraryUiState.ContentReady,
    onImportClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentPadding = PaddingValues(horizontal = PaletteTheme.spacing.medium)
        .plus(horizontal = WindowInsets.navigationBars.asPaddingValues())
        .plus(horizontal = WindowInsets.displayCutout.asPaddingValues())
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        LocalContent(
            localContentState = uiState.localContentState,
            onImportClick = onImportClick,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            contentPadding = contentPadding,
        )
        Spacer(
            modifier = Modifier
                .mediaControlSheetPadding(isMediaItemLoaded = uiState.isMediaItemLoaded)
                .navigationBarsPadding()
        )
    }
}

@Preview
@Composable
internal fun PreviewLibraryScreen() {
    PaletteTheme {
        val uiState = AudioLibraryUiState.ContentReady(
            localContentState = LocalContentState.Content(
                tracks = flowOf(PagingData.from(PreviewTracksUi1)),
                albums = flowOf(PagingData.from(PreviewAlbumsUi1)),
            ),
            isMediaItemLoaded = false,
        )
        AudioLibraryScreen(
            uiState = uiState,
            onImportClick = {},
            onItemClick = {},
            onItemPlayPauseClick = {},
        )
    }
}
