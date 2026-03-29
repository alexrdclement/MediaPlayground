package com.alexrdclement.mediaplayground.feature.artist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
import com.alexrdclement.mediaplayground.ui.util.PreviewSimpleArtist1
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
fun ArtistMetadataScreen(
    artistId: String,
    onNavigateBack: () -> Unit,
) {
    val viewModel: ArtistMetadataViewModel = assistedMetroViewModel<ArtistMetadataViewModel, ArtistMetadataViewModel.Factory>(
        key = artistId,
    ) {
        create(artistId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(ArtistMetadataUiState.Loading)
    LaunchedEffect(Unit) {
        viewModel.savedEvent.collect { onNavigateBack() }
    }
    ArtistMetadataScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSaveClick = viewModel::onSaveClick,
    )
}

@Composable
fun ArtistMetadataScreen(
    uiState: ArtistMetadataUiState,
    onNavigateBack: () -> Unit,
    onSaveClick: (name: String) -> Unit,
) {
    val nameState = rememberTextFieldState()
    LaunchedEffect((uiState as? ArtistMetadataUiState.Loaded)?.artist?.id) {
        val loaded = uiState as? ArtistMetadataUiState.Loaded ?: return@LaunchedEffect
        nameState.edit { replace(0, length, loaded.artist.name ?: "") }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = { Text("Artist", style = PaletteTheme.styles.text.headline) },
                navButton = { BackNavigationButton(onClick = onNavigateBack) },
            )
        },
        floatingAction = {
            when (uiState) {
                is ArtistMetadataUiState.Loaded -> {
                    FloatingAction(
                        modifier = Modifier
                            .fillMaxWidth()
                            .mediaControlSheetPadding(uiState.isMediaItemLoaded)
                    ) {
                        Button(
                            style = ButtonStyleToken.Primary,
                            onClick = { onSaveClick(nameState.text.toString()) },
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
            ArtistMetadataUiState.Loading -> IndeterminateProgressIndicator()
            ArtistMetadataUiState.Error -> Text("Failed to load artist.")
            is ArtistMetadataUiState.Loaded -> LoadedContent(
                state = uiState,
                nameState = nameState,
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun LoadedContent(
    state: ArtistMetadataUiState.Loaded,
    nameState: TextFieldState,
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
                Text("Name", style = PaletteTheme.styles.text.titleMedium)
                TextField(
                    state = nameState,
                    textStyle = PaletteTheme.styles.text.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        ArtistMetadataScreen(
            uiState = ArtistMetadataUiState.Loaded(
                artist = PreviewSimpleArtist1,
            ),
            onNavigateBack = {},
            onSaveClick = {},
        )
    }
}
