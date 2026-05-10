package com.alexrdclement.mediaplayground.feature.album

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.ui.components.MediaItemArtwork
import com.alexrdclement.mediaplayground.ui.components.TitleArtistBlock
import com.alexrdclement.mediaplayground.ui.components.track.TrackListItem
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPaddingValues
import com.alexrdclement.mediaplayground.ui.model.TrackUi
import com.alexrdclement.mediaplayground.ui.util.PreviewAlbum1
import com.alexrdclement.mediaplayground.ui.util.artistNamesOrDefault
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.components.media.PlayPauseButton
import com.alexrdclement.palette.components.util.plus
import com.alexrdclement.palette.theme.PaletteTheme
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun AlbumScreen(
    albumId: AlbumId,
    onNavigateBack: () -> Unit = {},
    onNavigateToAlbumMetadata: () -> Unit = {},
    onNavigateToAlbumDelete: (displayName: String) -> Unit = {},
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
    onNavigateToArtistDelete: (artistId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToTrackMetadata: (trackId: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
) {
    val viewModel: AlbumViewModel = assistedMetroViewModel<AlbumViewModel, AlbumViewModel.Factory>(
        key = albumId.value,
    ) {
        create(albumId.value)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(AlbumUiState.Loading)
    AlbumScreen(
        uiState = uiState,
        albumIdValue = albumId.value,
        onNavigateBack = onNavigateBack,
        onAlbumPlayPauseClick = viewModel::onAlbumPlayPauseClick,
        onTrackClick = viewModel::onTrackClick,
        onTrackPlayPauseClick = viewModel::onTrackPlayPauseClick,
        onNavigateToAlbumMetadata = onNavigateToAlbumMetadata,
        onNavigateToAlbumDelete = onNavigateToAlbumDelete,
        onNavigateToArtistMetadata = onNavigateToArtistMetadata,
        onNavigateToArtistDelete = onNavigateToArtistDelete,
        onNavigateToTrackMetadata = onNavigateToTrackMetadata,
        onNavigateToTrackDelete = onNavigateToTrackDelete,
    )
}

@Composable
fun AlbumScreen(
    uiState: AlbumUiState,
    albumIdValue: String = "",
    onNavigateBack: () -> Unit = {},
    onAlbumPlayPauseClick: () -> Unit,
    onTrackClick: (TrackUi) -> Unit,
    onTrackPlayPauseClick: (TrackUi) -> Unit,
    onNavigateToAlbumMetadata: () -> Unit = {},
    onNavigateToAlbumDelete: (displayName: String) -> Unit = {},
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
    onNavigateToArtistDelete: (artistId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToTrackMetadata: (trackId: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
) {
    LaunchedEffect(uiState) {
        if (uiState is AlbumUiState.NotFound) onNavigateBack()
    }
    Surface {
        when (uiState) {
            AlbumUiState.Loading, AlbumUiState.NotFound -> {}
            is AlbumUiState.Success -> LoadedContent(
                state = uiState,
                albumIdValue = albumIdValue,
                onAlbumPlayPauseClick = onAlbumPlayPauseClick,
                onTrackClick = onTrackClick,
                onTrackPlayPauseClick = onTrackPlayPauseClick,
                onNavigateToAlbumMetadata = onNavigateToAlbumMetadata,
                onNavigateToAlbumDelete = onNavigateToAlbumDelete,
                onNavigateToArtistMetadata = onNavigateToArtistMetadata,
                onNavigateToArtistDelete = onNavigateToArtistDelete,
                onNavigateToTrackMetadata = onNavigateToTrackMetadata,
                onNavigateToTrackDelete = onNavigateToTrackDelete,
            )
        }
    }
}

@Composable
private fun LoadedContent(
    state: AlbumUiState.Success,
    albumIdValue: String,
    onAlbumPlayPauseClick: () -> Unit,
    onTrackClick: (TrackUi) -> Unit,
    onTrackPlayPauseClick: (TrackUi) -> Unit,
    onNavigateToAlbumMetadata: () -> Unit = {},
    onNavigateToAlbumDelete: (displayName: String) -> Unit = {},
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
    onNavigateToArtistDelete: (artistId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToTrackMetadata: (trackId: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
) {
    BoxWithConstraints {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small),
            contentPadding = WindowInsets.safeDrawing.asPaddingValues().plus(
                mediaControlSheetPaddingValues(state.isMediaItemLoaded),
            ),
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                MediaItemArtwork(
                    uri = state.imageUri,
                    modifier = Modifier
                        .heightIn(
                            max = with(LocalDensity.current) {
                                (this@BoxWithConstraints.constraints.maxHeight / 2f).toDp()
                            }
                        )
                        .aspectRatio(1f)
                )
            }
            item {
                var albumOptionsExpanded by remember { mutableStateOf(false) }
                var albumOptionsTouchOffset by remember { mutableStateOf(Offset.Zero) }
                var artistOptionsItem by remember { mutableStateOf<Pair<String, String>?>(null) }
                var artistOptionsTouchOffset by remember { mutableStateOf(Offset.Zero) }
                TitleArtistBlock(
                    title = state.title,
                    artists = artistNamesOrDefault(artists = state.artists),
                    onTitleLongClick = { offset ->
                        albumOptionsTouchOffset = offset
                        albumOptionsExpanded = true
                    },
                    onArtistsLongClick = { offset ->
                        state.artists.firstOrNull()?.let { artist ->
                            artistOptionsTouchOffset = offset
                            artistOptionsItem = Pair(artist.id.value, artist.name ?: "")
                        }
                    },
                    titleOverlay = {
                        AlbumContextMenu(
                            expanded = albumOptionsExpanded,
                            offset = albumOptionsTouchOffset,
                            onDismissRequest = { albumOptionsExpanded = false },
                            onNavigateToMetadata = onNavigateToAlbumMetadata,
                            onNavigateToDelete = { onNavigateToAlbumDelete(state.title) },
                        )
                    },
                    artistsOverlay = {
                        artistOptionsItem?.let { (idValue, displayName) ->
                            ArtistContextMenu(
                                expanded = true,
                                offset = artistOptionsTouchOffset,
                                onDismissRequest = { artistOptionsItem = null },
                                onNavigateToMetadata = { onNavigateToArtistMetadata(idValue) },
                                onNavigateToDelete = { onNavigateToArtistDelete(idValue, displayName) },
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = PaletteTheme.spacing.small)
                )
            }
            item {
                PlayPauseButton(
                    isPlaying = state.isAlbumPlaying,
                    isEnabled = state.isAlbumPlayable,
                    onClick = onAlbumPlayPauseClick,
                    modifier = Modifier
                        .size(72.dp)
                        .padding(vertical = PaletteTheme.spacing.small)
                )
            }
            items(
                state.tracks,
                key = { it.track.id.value }
            ) { trackUi ->
                var trackOptionsExpanded by remember { mutableStateOf(false) }
                var trackOptionsTouchOffset by remember { mutableStateOf(Offset.Zero) }
                Box {
                    TrackListItem(
                        track = trackUi.track,
                        subtitle = trackUi.subtitle,
                        isLoaded = trackUi.isLoaded,
                        isPlayable = trackUi.isPlayable,
                        isPlaying = trackUi.isPlaying,
                        onClick = { onTrackClick(trackUi) },
                        onPlayPauseClick = { onTrackPlayPauseClick(trackUi) },
                        onLongClick = { offset ->
                            trackOptionsTouchOffset = offset
                            trackOptionsExpanded = true
                        },
                    )
                    TrackContextMenu(
                        expanded = trackOptionsExpanded,
                        offset = trackOptionsTouchOffset,
                        onDismissRequest = { trackOptionsExpanded = false },
                        onNavigateToMetadata = { onNavigateToTrackMetadata(trackUi.track.id.value) },
                        onNavigateToDelete = { onNavigateToTrackDelete(trackUi.track.id.value, trackUi.track.title) },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        val album = PreviewAlbum1
        val tracks = album.items.mapIndexed { index, track ->
            TrackUi(
                track = track,
                subtitle = track.artists.joinToString { it.name ?: "" },
                isLoaded = index == 1,
                isPlaying = index == 1,
                isPlayable = true,
            )
        }
        val uiState = AlbumUiState.Success(
            imageUri = null,
            title = album.title,
            artists = album.artists,
            tracks = tracks,
            isAlbumPlayable = true,
            isAlbumPlaying = false,
            isMediaItemLoaded = false,
        )
        AlbumScreen(
            uiState = uiState,
            onAlbumPlayPauseClick = {},
            onTrackClick = {},
            onTrackPlayPauseClick = {},
        )
    }
}
