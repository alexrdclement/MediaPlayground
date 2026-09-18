package com.alexrdclement.mediaplayground.feature.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
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
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.ui.components.MediaItemArtwork
import com.alexrdclement.mediaplayground.ui.components.metadata.MetadataContentStyle
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
import com.alexrdclement.mediaplayground.ui.theme.component.layout.metadataContent
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.IndeterminateProgressIndicator
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextField
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.components.layout.FloatingAction
import com.embarrasdf.palette.components.util.plus
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.components.layout.TopBar
import com.embarrasdf.palette.theme.components.navigation.BackNavigationButton
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun ImageMetadataScreen(
    imageId: ImageId,
    onNavigateBack: () -> Unit,
    onNavigateToDelete: (displayName: String) -> Unit = {},
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
    LaunchedEffect(Unit) {
        viewModel.deletedEvent.collect { onNavigateBack() }
    }
    ImageMetadataScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSaveClick = viewModel::onSaveClick,
        onNavigateToDelete = onNavigateToDelete,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImageMetadataScreen(
    uiState: ImageMetadataUiState,
    onNavigateBack: () -> Unit,
    onSaveClick: (notes: String?) -> Unit,
    onNavigateToDelete: (displayName: String) -> Unit = {},
) {
    val notesState = rememberTextFieldState()
    LaunchedEffect((uiState as? ImageMetadataUiState.Loaded)?.image?.id) {
        val loaded = uiState as? ImageMetadataUiState.Loaded ?: return@LaunchedEffect
        notesState.edit { replace(0, length, loaded.image.notes ?: "") }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = { Text("Image", style = PaletteTheme.component.core.text.headline) },
                navButton = { BackNavigationButton(onClick = onNavigateBack) },
                actions = if (uiState is ImageMetadataUiState.Loaded) {
                    {
                        Button(
                            style = PaletteTheme.component.core.button.secondary,
                            onClick = { onNavigateToDelete(uiState.image.uri) },
                        ) {
                            Text("Delete", style = PaletteTheme.component.core.text.labelLarge.copy(color = PaletteTheme.semantic.color.secondary))
                        }
                    }
                } else null,
            )
        },
        floatingAction = {
            when (uiState) {
                is ImageMetadataUiState.Loaded -> {
                    if (WindowInsets.isImeVisible) return@Scaffold
                    FloatingAction(
                        modifier = Modifier
                            .fillMaxWidth()
                            .mediaControlSheetPadding(uiState.isMediaItemLoaded),
                        style = PaletteTheme.component.layout.floatingAction,
                    ) {
                        Button(
                            style = PaletteTheme.component.core.button.primary,
                            onClick = { onSaveClick(notesState.text.toString().ifBlank { null }) },
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
    ) { innerPadding ->
        val contentStyle = PaletteTheme.component.layout.metadataContent
        when (uiState) {
            ImageMetadataUiState.Loading -> IndeterminateProgressIndicator(style = PaletteTheme.component.core.progressIndicator)
            ImageMetadataUiState.Error -> Text("Failed to load image.", style = PaletteTheme.component.core.text.bodyMedium)
            is ImageMetadataUiState.Loaded -> LoadedContent(
                state = uiState,
                notesState = notesState,
                style = contentStyle.copy(
                    contentPadding = contentStyle.contentPadding.plus(innerPadding),
                ),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun MetadataField(
    label: String,
    value: String?,
) {
    Column(verticalArrangement = Arrangement.spacedBy(PaletteTheme.semantic.dimension.spacing.small)) {
        Text(label, style = PaletteTheme.component.core.text.titleMedium)
        Text(value ?: "Unknown", style = PaletteTheme.component.core.text.bodyMedium)
    }
}

@Composable
private fun LoadedContent(
    state: ImageMetadataUiState.Loaded,
    notesState: TextFieldState,
    modifier: Modifier = Modifier,
    style: MetadataContentStyle = MetadataContentStyle(),
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(style.contentSpacing),
        contentPadding = style.contentPadding,
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
            MetadataField(label = "URI", value = state.image.uri)
        }
        item {
            MetadataField(
                label = "Dimensions",
                value = if (state.image.widthPx != null && state.image.heightPx != null)
                    "${state.image.widthPx} \u00d7 ${state.image.heightPx}"
                else null,
            )
        }
        item {
            MetadataField(
                label = "Date Taken",
                value = state.image.dateTimeOriginal,
            )
        }
        item {
            val cameraMake = state.image.cameraMake
            val cameraModel = state.image.cameraModel
            MetadataField(
                label = "Camera",
                value = listOfNotNull(cameraMake, cameraModel).joinToString(" ").ifEmpty { null },
            )
        }
        item {
            val lat = state.image.gpsLatitude
            val lon = state.image.gpsLongitude
            MetadataField(
                label = "Location",
                value = if (lat != null && lon != null) "%.6f, %.6f".format(lat, lon) else null,
            )
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
