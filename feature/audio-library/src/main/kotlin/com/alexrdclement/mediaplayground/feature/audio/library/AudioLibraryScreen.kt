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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContent
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContentState
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContentStyle
import com.alexrdclement.mediaplayground.feature.audio.library.theme.component.media.contentReady
import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.MediaItem
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewAlbumsUi1
import com.alexrdclement.mediaplayground.ui.util.PreviewTracksUi1
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.util.plus
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.components.layout.TopBar
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.flow.flowOf

private const val MediaPickerAudioMimeType = "audio/*"

@Composable
fun AudioLibraryScreen(
    onNavigateToPlayer: (MediaItem) -> Unit,
    onNavigateToAlbum: (Album) -> Unit,
    onNavigateToAlbumMetadata: (albumIdValue: String) -> Unit = {},
    onNavigateToAlbumDelete: (albumId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToTrackMetadata: (trackIdValue: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
    viewModel: AudioLibraryViewModel = metroViewModel(),
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
        onNavigateToAlbumMetadata = onNavigateToAlbumMetadata,
        onNavigateToAlbumDelete = onNavigateToAlbumDelete,
        onNavigateToTrackMetadata = onNavigateToTrackMetadata,
        onNavigateToTrackDelete = onNavigateToTrackDelete,
    )
}

@Composable
fun AudioLibraryScreen(
    uiState: AudioLibraryUiState,
    onImportClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    onNavigateToAlbumMetadata: (albumIdValue: String) -> Unit = {},
    onNavigateToAlbumDelete: (albumId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToTrackMetadata: (trackIdValue: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
) {
    Scaffold(
        topBar = {
            TopBar(
                title = {
                    Text(
                        text = "Audio Library",
                        style = PaletteTheme.component.core.text.headline,
                    )
                },
            )
        },
    ) { innerPadding ->
        val contentStyle = PaletteTheme.component.media.contentReady
        when (uiState) {
            AudioLibraryUiState.InitialState -> {}
            is AudioLibraryUiState.ContentReady -> ContentReady(
                uiState = uiState,
                onImportClick = onImportClick,
                onItemClick = onItemClick,
                onItemPlayPauseClick = onItemPlayPauseClick,
                onNavigateToAlbumMetadata = onNavigateToAlbumMetadata,
                onNavigateToAlbumDelete = onNavigateToAlbumDelete,
                onNavigateToTrackMetadata = onNavigateToTrackMetadata,
                onNavigateToTrackDelete = onNavigateToTrackDelete,
                style = contentStyle.copy(
                    contentPadding = contentStyle.contentPadding.plus(innerPadding),
                ),
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

data class ContentReadyStyle(
    val contentPadding: PaddingValues = PaddingValues(0.dp),
    val contentSpacing: Dp = 8.dp,
    val localContentStyle: LocalContentStyle = LocalContentStyle(),
)

@Composable
fun ContentReady(
    uiState: AudioLibraryUiState.ContentReady,
    onImportClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    modifier: Modifier = Modifier,
    style: ContentReadyStyle = ContentReadyStyle(),
    onNavigateToAlbumMetadata: (albumIdValue: String) -> Unit = {},
    onNavigateToAlbumDelete: (albumId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToTrackMetadata: (trackIdValue: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(style.contentSpacing),
        // Padding applied inside the scroll so content scrolls under the top bar, matching the
        // overlay layout Scaffold's content padding is meant for.
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(style.contentPadding)
    ) {
        LocalContent(
            localContentState = uiState.localContentState,
            onImportClick = onImportClick,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            onNavigateToAlbumMetadata = onNavigateToAlbumMetadata,
            onNavigateToAlbumDelete = onNavigateToAlbumDelete,
            onNavigateToTrackMetadata = onNavigateToTrackMetadata,
            onNavigateToTrackDelete = onNavigateToTrackDelete,
            style = style.localContentStyle.copy(
                contentPadding = style.localContentStyle.contentPadding
                    .plus(horizontal = WindowInsets.navigationBars.asPaddingValues())
                    .plus(horizontal = WindowInsets.displayCutout.asPaddingValues()),
            ),
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
