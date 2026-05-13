package com.alexrdclement.mediaplayground.feature.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.AudioItem
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.alexrdclement.mediaplayground.ui.components.metadata.MetadataContentStyle
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
import com.alexrdclement.mediaplayground.ui.theme.component.layout.metadataContent
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.IndeterminateProgressIndicator
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextField
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.components.layout.FloatingAction
import com.embarrasdf.palette.components.util.plus
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.core.Surface
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.components.layout.TopBar
import com.embarrasdf.palette.theme.components.navigation.BackNavigationButton
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun TrackMetadataScreen(
    trackId: TrackId,
    onNavigateBack: () -> Unit,
    onNavigateToDelete: (displayName: String) -> Unit = {},
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
) {
    val viewModel = assistedMetroViewModel<TrackMetadataViewModel, TrackMetadataViewModel.Factory>(
        key = trackId.value,
    ) {
        create(trackId.value)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(TrackMetadataUiState.Loading)
    val currentOnNavigateBack by rememberUpdatedState(onNavigateBack)

    LaunchedEffect(Unit) {
        viewModel.savedEvent.collect { currentOnNavigateBack() }
    }
    LaunchedEffect(Unit) {
        viewModel.deletedEvent.collect { currentOnNavigateBack() }
    }
    TrackMetadataScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSaveClick = viewModel::onSaveClick,
        onNavigateToDelete = onNavigateToDelete,
        onNavigateToArtistMetadata = onNavigateToArtistMetadata,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrackMetadataScreen(
    uiState: TrackMetadataUiState,
    onNavigateBack: () -> Unit,
    onSaveClick: (title: String, notes: String?) -> Unit,
    onNavigateToDelete: (displayName: String) -> Unit = {},
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
) {
    val titleState = rememberTextFieldState()
    val notesState = rememberTextFieldState()
    LaunchedEffect((uiState as? TrackMetadataUiState.Loaded)?.track?.id) {
        val loaded = uiState as? TrackMetadataUiState.Loaded ?: return@LaunchedEffect
        titleState.edit { replace(0, length, loaded.track.title) }
        notesState.edit { replace(0, length, loaded.track.notes ?: "") }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                title = { Text(text = "Track", style = PaletteTheme.component.core.text.headline) },
                navButton = { BackNavigationButton(onClick = onNavigateBack) },
                actions = if (uiState is TrackMetadataUiState.Loaded) {
                    {
                        Button(
                            style = PaletteTheme.component.core.button.secondary,
                            onClick = { onNavigateToDelete(uiState.track.title) },
                        ) {
                            Text(
                                text = "Delete",
                                style = PaletteTheme.component.core.text.labelLarge.copy(
                                    color = PaletteTheme.semantic.color.secondary,
                                ),
                            )
                        }
                    }
                } else {
                    null
                },
            )
        },
        floatingAction = {
            when (uiState) {
                is TrackMetadataUiState.Loaded -> {
                    if (WindowInsets.isImeVisible) return@Scaffold
                    FloatingAction(
                        modifier = Modifier
                            .fillMaxWidth()
                            .mediaControlSheetPadding(uiState.isMediaItemLoaded),
                        style = PaletteTheme.component.layout.floatingAction,
                    ) {
                        Button(
                            style = PaletteTheme.component.core.button.primary,
                            onClick = {
                                onSaveClick(
                                    titleState.text.toString(),
                                    notesState.text.toString().ifBlank { null },
                                )
                            },
                            enabled = !uiState.isSaving,
                            modifier = Modifier
                                .padding(PaletteTheme.semantic.dimension.spacing.medium),
                        ) {
                            Text(
                                text = if (uiState.isSaving) "Saving\u2026" else "Save",
                                style = PaletteTheme.component.core.text.labelLarge.copy(
                                    color = PaletteTheme.semantic.color.onPrimary,
                                ),
                            )
                        }
                    }
                }
                else -> Unit
            }
        },
    ) { innerPadding ->
        val contentStyle = PaletteTheme.component.layout.metadataContent
        when (uiState) {
            TrackMetadataUiState.Loading -> IndeterminateProgressIndicator(
                style = PaletteTheme.component.core.progressIndicator,
            )
            TrackMetadataUiState.Error -> Text(
                text = "Failed to load track.",
                style = PaletteTheme.component.core.text.bodyMedium,
            )
            is TrackMetadataUiState.Loaded -> LoadedContent(
                state = uiState,
                titleState = titleState,
                notesState = notesState,
                onNavigateToArtistMetadata = onNavigateToArtistMetadata,
                style = contentStyle.copy(
                    contentPadding = contentStyle.contentPadding.plus(innerPadding),
                ),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun LoadedContent(
    state: TrackMetadataUiState.Loaded,
    titleState: TextFieldState,
    notesState: TextFieldState,
    onNavigateToArtistMetadata: (artistId: String) -> Unit,
    modifier: Modifier = Modifier,
    style: MetadataContentStyle = MetadataContentStyle(),
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(style.contentSpacing),
        contentPadding = style.contentPadding,
        modifier = modifier,
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(style.sectionSpacing)) {
                Text(text = "Title", style = style.labelStyle)
                TextField(
                    state = titleState,
                    style = style.textFieldStyle,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        val trackArtists = (state.track as? AudioItem)?.artists.orEmpty()
        if (trackArtists.isNotEmpty()) {
            item {
                Text(text = "Artists", style = style.labelStyle)
            }
            items(trackArtists, key = { it.id.value }) { artist ->
                ArtistRow(
                    artist = artist,
                    onNavigateToMetadata = { onNavigateToArtistMetadata(artist.id.value) },
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(style.sectionSpacing)) {
                Text(text = "Notes", style = style.labelStyle)
                TextField(
                    state = notesState,
                    style = style.textFieldStyle,
                    lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 5),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ArtistRow(
    artist: Artist,
    onNavigateToMetadata: () -> Unit,
) {
    Surface(
        onClick = onNavigateToMetadata,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(PaletteTheme.semantic.dimension.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = PaletteTheme.semantic.dimension.spacing.small),
        ) {
            Text(
                text = artist.name ?: "Unknown Artist",
                style = PaletteTheme.component.core.text.bodyMedium,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "Edit \u2192",
                style = PaletteTheme.component.core.text.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        TrackMetadataScreen(
            uiState = TrackMetadataUiState.Loaded(
                track = PreviewTrack1,
            ),
            onNavigateBack = {},
            onSaveClick = { _, _ -> },
        )
    }
}
