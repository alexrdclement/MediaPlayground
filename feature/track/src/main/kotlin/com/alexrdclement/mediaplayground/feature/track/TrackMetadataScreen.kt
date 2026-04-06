package com.alexrdclement.mediaplayground.feature.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.alexrdclement.palette.components.core.Button
import com.alexrdclement.palette.components.core.IndeterminateProgressIndicator
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.components.core.TextField
import com.alexrdclement.palette.components.layout.FloatingAction
import com.alexrdclement.palette.components.layout.Scaffold
import com.alexrdclement.palette.components.layout.TopBar
import com.alexrdclement.palette.components.navigation.BackNavigationButton
import com.alexrdclement.palette.components.util.plus
import com.alexrdclement.palette.theme.PaletteTheme
import com.alexrdclement.palette.theme.styles.ButtonStyleToken
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
                title = { Text("Track", style = PaletteTheme.styles.text.headline) },
                navButton = { BackNavigationButton(onClick = onNavigateBack) },
                actions = if (uiState is TrackMetadataUiState.Loaded) {
                    {
                        Button(
                            style = ButtonStyleToken.Secondary,
                            onClick = { onNavigateToDelete(uiState.track.title) },
                        ) {
                            Text("Delete", style = PaletteTheme.styles.text.labelLarge)
                        }
                    }
                } else null,
            )
        },
        floatingAction = {
            when (uiState) {
                is TrackMetadataUiState.Loaded -> {
                    if (WindowInsets.isImeVisible) return@Scaffold
                    FloatingAction(
                        modifier = Modifier
                            .fillMaxWidth()
                            .mediaControlSheetPadding(uiState.isMediaItemLoaded)
                    ) {
                        Button(
                            style = ButtonStyleToken.Primary,
                            onClick = {
                                onSaveClick(
                                    titleState.text.toString(),
                                    trackNumberState.text.toString().toIntOrNull(),
                                    notesState.text.toString().ifBlank { null },
                                )
                            },
                            enabled = !uiState.isSaving,
                            modifier = Modifier
                                .padding(PaletteTheme.spacing.medium),
                        ) {
                            Text(
                                text = if (uiState.isSaving) "Saving\u2026" else "Save",
                                style = PaletteTheme.styles.text.labelLarge,
                            )
                        }
                    }
                }
                else -> Unit
            }
        },
    ) { innerPadding ->
        when (uiState) {
            TrackMetadataUiState.Loading -> IndeterminateProgressIndicator()
            TrackMetadataUiState.Error -> Text("Failed to load track.")
            is TrackMetadataUiState.Loaded -> LoadedContent(
                state = uiState,
                titleState = titleState,
                trackNumberState = trackNumberState,
                notesState = notesState,
                onNavigateToArtistMetadata = onNavigateToArtistMetadata,
                contentPadding = innerPadding,
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
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.medium),
        contentPadding = contentPadding.plus(PaletteTheme.spacing.medium),
        modifier = modifier,
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small)) {
                Text("Title", style = PaletteTheme.styles.text.titleMedium)
                TextField(
                    state = titleState,
                    textStyle = PaletteTheme.styles.text.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small)) {
                Text("Track Number", style = PaletteTheme.styles.text.titleMedium)
                TextField(
                    state = trackNumberState,
                    textStyle = PaletteTheme.styles.text.bodyMedium,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        if (state.track.artists.isNotEmpty()) {
            item {
                Text("Artists", style = PaletteTheme.styles.text.titleMedium)
            }
            items(state.track.artists, key = { it.id.value }) { artist ->
                ArtistRow(
                    artist = artist,
                    onNavigateToMetadata = { onNavigateToArtistMetadata(artist.id.value) },
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small)) {
                Text("Notes", style = PaletteTheme.styles.text.titleMedium)
                TextField(
                    state = notesState,
                    textStyle = PaletteTheme.styles.text.bodyMedium,
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = PaletteTheme.spacing.small),
        ) {
            Text(
                text = artist.name ?: "Unknown Artist",
                style = PaletteTheme.styles.text.bodyMedium,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Edit \u2192",
                style = PaletteTheme.styles.text.bodyMedium,
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
