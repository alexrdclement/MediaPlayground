package com.alexrdclement.mediaplayground.feature.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.media.model.audio.Image
import com.alexrdclement.mediaplayground.media.model.audio.ImageId
import com.alexrdclement.mediaplayground.ui.components.MediaItemArtwork
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
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
fun ImageMetadataScreen(
    imageId: ImageId,
    onNavigateBack: () -> Unit,
) {
    val viewModel: ImageMetadataViewModel = assistedMetroViewModel<ImageMetadataViewModel, ImageMetadataViewModel.Factory>(
        key = imageId.value,
    ) {
        create(imageId.value)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(ImageMetadataUiState.Loading)
    LaunchedEffect(Unit) {
        viewModel.savedEvent.collect { onNavigateBack() }
    }
    ImageMetadataScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSaveClick = viewModel::onSaveClick,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImageMetadataScreen(
    uiState: ImageMetadataUiState,
    onNavigateBack: () -> Unit,
    onSaveClick: (notes: String?) -> Unit,
) {
    val notesState = rememberTextFieldState()
    LaunchedEffect((uiState as? ImageMetadataUiState.Loaded)?.image?.id) {
        val loaded = uiState as? ImageMetadataUiState.Loaded ?: return@LaunchedEffect
        notesState.edit { replace(0, length, loaded.image.notes ?: "") }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = { Text("Image", style = PaletteTheme.styles.text.headline) },
                navButton = { BackNavigationButton(onClick = onNavigateBack) },
            )
        },
        floatingAction = {
            when (uiState) {
                is ImageMetadataUiState.Loaded -> {
                    if (WindowInsets.isImeVisible) return@Scaffold
                    FloatingAction(
                        modifier = Modifier
                            .fillMaxWidth()
                            .mediaControlSheetPadding(uiState.isMediaItemLoaded)
                    ) {
                        Button(
                            style = ButtonStyleToken.Primary,
                            onClick = { onSaveClick(notesState.text.toString().ifBlank { null }) },
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
            ImageMetadataUiState.Loading -> IndeterminateProgressIndicator()
            ImageMetadataUiState.Error -> Text("Failed to load image.")
            is ImageMetadataUiState.Loaded -> LoadedContent(
                state = uiState,
                notesState = notesState,
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun LoadedContent(
    state: ImageMetadataUiState.Loaded,
    notesState: TextFieldState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.medium),
        contentPadding = contentPadding.plus(PaletteTheme.spacing.medium),
        modifier = modifier,
    ) {
        item {
            MediaItemArtwork(
                imageUrl = state.image.uri,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small)) {
                Text("URI", style = PaletteTheme.styles.text.titleMedium)
                Text(state.image.uri, style = PaletteTheme.styles.text.bodyMedium)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small)) {
                Text("Dimensions", style = PaletteTheme.styles.text.titleMedium)
                val dimensions = when {
                    state.image.widthPx != null && state.image.heightPx != null ->
                        "${state.image.widthPx} \u00d7 ${state.image.heightPx}"
                    else -> "Unknown"
                }
                Text(dimensions, style = PaletteTheme.styles.text.bodyMedium)
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

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        ImageMetadataScreen(
            uiState = ImageMetadataUiState.Loaded(
                image = Image(id = ImageId("1"), uri = "file:/images/1.jpg"),
            ),
            onNavigateBack = {},
            onSaveClick = {},
        )
    }
}
