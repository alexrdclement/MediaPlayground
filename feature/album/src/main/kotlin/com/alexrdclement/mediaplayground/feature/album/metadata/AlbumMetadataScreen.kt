package com.alexrdclement.mediaplayground.feature.album.metadata

import androidx.compose.foundation.clickable
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
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.audio.Image
import com.alexrdclement.mediaplayground.media.model.audio.ImageId
import com.alexrdclement.mediaplayground.media.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
import com.alexrdclement.mediaplayground.ui.util.PreviewAlbum1
import com.alexrdclement.palette.components.core.Button
import com.alexrdclement.palette.components.core.IndeterminateProgressIndicator
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
fun AlbumMetadataScreen(
    albumId: AlbumId,
    onNavigateBack: () -> Unit,
    onNavigateToArtistMetadata: (artistId: String) -> Unit,
    onNavigateToImageMetadata: (imageId: ImageId) -> Unit,
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
    AlbumMetadataScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSaveClick = viewModel::onSaveClick,
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
    onNavigateToArtistMetadata: (artistId: String) -> Unit,
    onNavigateToImageMetadata: (imageId: ImageId) -> Unit,
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
                title = { Text("Album", style = PaletteTheme.styles.text.headline) },
                navButton = { BackNavigationButton(onClick = onNavigateBack) },
            )
        },
        floatingAction = {
            when (uiState) {
                is AlbumMetadataUiState.Loaded -> {
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
            AlbumMetadataUiState.Loading -> IndeterminateProgressIndicator()
            AlbumMetadataUiState.Error -> Text("Failed to load album.")
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
    onNavigateToImageMetadata: (imageId: ImageId) -> Unit,
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
        if (state.album.artists.isNotEmpty()) {
            item {
                Text("Artists", style = PaletteTheme.styles.text.titleMedium)
            }
            items(state.album.artists, key = { it.id }) { artist ->
                ArtistRow(
                    artist = artist,
                    onClick = { onNavigateToArtistMetadata(artist.id) },
                )
            }
        }
        if (state.album.images.isNotEmpty()) {
            item {
                Text("Images", style = PaletteTheme.styles.text.titleMedium)
            }
            items(state.album.images, key = { it.id.value }) { image ->
                ImageRow(
                    image = image,
                    onClick = { onNavigateToImageMetadata(image.id) },
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
    artist: SimpleArtist,
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = PaletteTheme.spacing.small),
    ) {
        Text(
            text = artist.name ?: "Unknown Artist",
            style = PaletteTheme.styles.text.bodyMedium,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Edit \u2192", style = PaletteTheme.styles.text.bodyMedium)
    }
}

@Composable
private fun ImageRow(
    image: Image,
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = PaletteTheme.spacing.small),
    ) {
        Text(
            text = image.uri,
            style = PaletteTheme.styles.text.bodyMedium,
            maxLines = 1,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("View \u2192", style = PaletteTheme.styles.text.bodyMedium)
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
            onNavigateToArtistMetadata = {},
            onNavigateToImageMetadata = {},
        )
    }
}
