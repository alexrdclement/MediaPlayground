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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.media.model.SimpleArtist
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.IndeterminateProgressIndicator
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextField
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.components.layout.FloatingAction
import com.embarrasdf.palette.components.layout.Scaffold
import com.embarrasdf.palette.components.layout.TopBar
import com.embarrasdf.palette.components.navigation.BackNavigationButton
import com.embarrasdf.palette.components.util.plus
import com.alexrdclement.mediaplayground.ui.components.metadata.MetadataContentStyle
import com.alexrdclement.mediaplayground.ui.theme.component.layout.metadataContent
import com.embarrasdf.palette.theme.PaletteTheme
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

    LaunchedEffect(Unit) {
        viewModel.savedEvent.collect { onNavigateBack() }
    }
    LaunchedEffect(Unit) {
        viewModel.deletedEvent.collect { onNavigateBack() }
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
    onSaveClick: (title: String, trackNumber: Int?, notes: String?) -> Unit,
    onNavigateToDelete: (displayName: String) -> Unit = {},
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
) {
    val titleState = rememberTextFieldState()
    val trackNumberState = rememberTextFieldState()
    val notesState = rememberTextFieldState()
    LaunchedEffect((uiState as? TrackMetadataUiState.Loaded)?.track?.id) {
        val loaded = uiState as? TrackMetadataUiState.Loaded ?: return@LaunchedEffect
        titleState.edit { replace(0, length, loaded.track.title) }
        trackNumberState.edit { replace(0, length, loaded.track.trackNumber?.toString() ?: "") }
        notesState.edit { replace(0, length, loaded.track.notes ?: "") }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = { Text("Track", style = PaletteTheme.component.core.text.headline) },
                navButton = { BackNavigationButton(onClick = onNavigateBack, style = PaletteTheme.component.navigation.backNavigationButton) },
                actions = if (uiState is TrackMetadataUiState.Loaded) {
                    {
                        Button(
                            style = PaletteTheme.component.core.button.secondary,
                            onClick = { onNavigateToDelete(uiState.track.title) },
                        ) {
                            Text("Delete", style = PaletteTheme.component.core.text.labelLarge.copy(color = PaletteTheme.semantic.color.secondary))
                        }
                    }
                } else null,
                style = PaletteTheme.component.layout.topBar,
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
                                    trackNumberState.text.toString().toIntOrNull(),
                                    notesState.text.toString().ifBlank { null },
                                )
                            },
                            enabled = !uiState.isSaving,
                            modifier = Modifier
                                .padding(PaletteTheme.semantic.dimension.spacing.medium),
                        ) {
                            Text(
                                text = if (uiState.isSaving) "Saving\u2026" else "Save",
                                style = PaletteTheme.component.core.text.labelLarge.copy(color = PaletteTheme.semantic.color.onPrimary),
                            )
                        }
                    }
                }
                else -> Unit
            }
        },
        style = PaletteTheme.component.layout.scaffold,
    ) { innerPadding ->
        val contentStyle = PaletteTheme.component.layout.metadataContent
        when (uiState) {
            TrackMetadataUiState.Loading -> IndeterminateProgressIndicator(style = PaletteTheme.component.core.progressIndicator)
            TrackMetadataUiState.Error -> Text("Failed to load track.", style = PaletteTheme.component.core.text.bodyMedium)
            is TrackMetadataUiState.Loaded -> LoadedContent(
                state = uiState,
                titleState = titleState,
                trackNumberState = trackNumberState,
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
    trackNumberState: TextFieldState,
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
                Text("Title", style = style.labelStyle)
                TextField(
                    state = titleState,
                    style = style.textFieldStyle,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(style.sectionSpacing)) {
                Text("Track Number", style = style.labelStyle)
                TextField(
                    state = trackNumberState,
                    style = style.textFieldStyle,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        if (state.track.artists.isNotEmpty()) {
            item {
                Text("Artists", style = style.labelStyle)
            }
            items(state.track.artists, key = { it.id }) { artist ->
                ArtistRow(
                    artist = artist,
                    onNavigateToMetadata = { onNavigateToArtistMetadata(artist.id) },
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(style.sectionSpacing)) {
                Text("Notes", style = style.labelStyle)
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
    artist: SimpleArtist,
    onNavigateToMetadata: () -> Unit,
) {
    Surface(
        onClick = onNavigateToMetadata,
        modifier = Modifier.fillMaxWidth(),
        style = PaletteTheme.component.core.surface.default,
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
            onSaveClick = { _, _, _ -> },
        )
    }
}
