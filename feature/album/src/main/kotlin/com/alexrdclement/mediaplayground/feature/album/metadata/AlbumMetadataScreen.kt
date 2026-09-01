package com.alexrdclement.mediaplayground.feature.album.metadata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.SimpleArtist
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
import com.alexrdclement.mediaplayground.ui.util.PreviewAlbum1
import com.alexrdclement.palette.components.core.Button
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.components.core.IndeterminateProgressIndicator
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.components.core.TextField
import com.alexrdclement.palette.components.layout.FloatingAction
import com.alexrdclement.palette.components.layout.Scaffold
import com.alexrdclement.palette.components.layout.TopBar
import com.alexrdclement.palette.components.navigation.BackNavigationButton
import com.alexrdclement.palette.components.util.plus
import com.alexrdclement.palette.theme.PaletteTheme
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun AlbumMetadataScreen(
    albumId: AlbumId,
    onNavigateBack: () -> Unit,
    onNavigateToDelete: (displayName: String) -> Unit = {},
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
    onNavigateToImageMetadata: (imageIdValue: String) -> Unit = {},
) {
    val viewModel: AlbumMetadataViewModel = assistedMetroViewModel<AlbumMetadataViewModel, AlbumMetadataViewModel.Factory>(
        key = albumId.value,
    ) {
        create(albumId.value)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(AlbumMetadataUiState.Loading)
    LaunchedEffect(Unit) {
        viewModel.savedEvent.collect { onNavigateBack() }
    }
    LaunchedEffect(Unit) {
        viewModel.deletedEvent.collect { onNavigateBack() }
    }
    AlbumMetadataScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSaveClick = viewModel::onSaveClick,
        onNavigateToDelete = onNavigateToDelete,
        onNavigateToArtistMetadata = onNavigateToArtistMetadata,
        onNavigateToImageMetadata = onNavigateToImageMetadata,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlbumMetadataScreen(
    uiState: AlbumMetadataUiState,
    onNavigateBack: () -> Unit,
    onSaveClick: (title: String, notes: String?) -> Unit,
    onNavigateToDelete: (displayName: String) -> Unit = {},
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
    onNavigateToImageMetadata: (imageIdValue: String) -> Unit = {},
) {
    val titleState = rememberTextFieldState()
    val notesState = rememberTextFieldState()
    LaunchedEffect((uiState as? AlbumMetadataUiState.Loaded)?.album?.id) {
        val loaded = uiState as? AlbumMetadataUiState.Loaded ?: return@LaunchedEffect
        titleState.edit { replace(0, length, loaded.album.title) }
        notesState.edit { replace(0, length, loaded.album.notes ?: "") }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = { Text("Album", style = PaletteTheme.component.core.text.headline) },
                navButton = { BackNavigationButton(onClick = onNavigateBack, style = PaletteTheme.component.navigation.backNavigationButton) },
                actions = if (uiState is AlbumMetadataUiState.Loaded) {
                    {
                        Button(
                            style = PaletteTheme.component.core.button.secondary,
                            onClick = { onNavigateToDelete(uiState.album.title) },
                        ) {
                            Text("Delete", style = PaletteTheme.component.core.text.labelLarge)
                        }
                    }
                } else null,
                style = PaletteTheme.component.layout.topBar,
            )
        },
        floatingAction = {
            when (uiState) {
                is AlbumMetadataUiState.Loaded -> {
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
                                style = PaletteTheme.component.core.text.labelLarge,
                            )
                        }
                    }
                }
                else -> Unit
            }
        },
        style = PaletteTheme.component.layout.scaffold,
    ) { innerPadding ->
        when (uiState) {
            AlbumMetadataUiState.Loading -> IndeterminateProgressIndicator(style = PaletteTheme.component.core.progressIndicator)
            AlbumMetadataUiState.Error -> Text("Failed to load album.", style = PaletteTheme.component.core.text.bodyMedium)
            is AlbumMetadataUiState.Loaded -> LoadedContent(
                state = uiState,
                titleState = titleState,
                notesState = notesState,
                onNavigateToArtistMetadata = onNavigateToArtistMetadata,
                onNavigateToImageMetadata = onNavigateToImageMetadata,
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun LoadedContent(
    state: AlbumMetadataUiState.Loaded,
    titleState: TextFieldState,
    notesState: TextFieldState,
    onNavigateToArtistMetadata: (artistId: String) -> Unit,
    onNavigateToImageMetadata: (imageIdValue: String) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.semantic.dimension.spacing.medium),
        contentPadding = contentPadding.plus(PaletteTheme.semantic.dimension.spacing.medium),
        modifier = modifier,
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(PaletteTheme.semantic.dimension.spacing.small)) {
                Text("Title", style = PaletteTheme.component.core.text.titleMedium)
                TextField(
                    state = titleState,
                    style = PaletteTheme.component.core.textField,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        if (state.album.artists.isNotEmpty()) {
            item {
                Text("Artists", style = PaletteTheme.component.core.text.titleMedium)
            }
            items(state.album.artists, key = { it.id }) { artist ->
                ArtistRow(
                    artist = artist,
                    onNavigateToMetadata = { onNavigateToArtistMetadata(artist.id) },
                )
            }
        }
        if (state.album.images.isNotEmpty()) {
            item {
                Text("Images", style = PaletteTheme.component.core.text.titleMedium)
            }
            items(state.album.images, key = { it.id.value }) { image ->
                ImageRow(
                    image = image,
                    onNavigateToMetadata = { onNavigateToImageMetadata(image.id.value) },
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(PaletteTheme.semantic.dimension.spacing.small)) {
                Text("Notes", style = PaletteTheme.component.core.text.titleMedium)
                TextField(
                    state = notesState,
                    style = PaletteTheme.component.core.textField,
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = PaletteTheme.semantic.dimension.spacing.small),
        ) {
            Text(
                text = artist.name ?: "Unknown Artist",
                style = PaletteTheme.component.core.text.bodyMedium,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit \u2192", style = PaletteTheme.component.core.text.bodyMedium)
        }
    }
}

@Composable
private fun ImageRow(
    image: Image,
    onNavigateToMetadata: () -> Unit,
) {
    Surface(
        onClick = onNavigateToMetadata,
        modifier = Modifier.fillMaxWidth(),
        style = PaletteTheme.component.core.surface.default,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = PaletteTheme.semantic.dimension.spacing.small),
        ) {
            Text(
                text = image.uri,
                style = PaletteTheme.component.core.text.bodyMedium,
                maxLines = 1,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("View \u2192", style = PaletteTheme.component.core.text.bodyMedium)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        val uiState = AlbumMetadataUiState.Loaded(
            album = PreviewAlbum1,
        )
        AlbumMetadataScreen(
            uiState = uiState,
            onNavigateBack = {},
            onSaveClick = { _, _ -> },
        )
    }
}
