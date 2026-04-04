package com.alexrdclement.mediaplayground.feature.image.library

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexrdclement.mediaplayground.media.model.image.Image
import com.alexrdclement.mediaplayground.media.model.image.ImageId
import com.alexrdclement.mediaplayground.ui.components.MediaItemArtwork
import com.alexrdclement.palette.components.core.Button
import com.alexrdclement.palette.components.core.ButtonDefaults
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.components.layout.Scaffold
import com.alexrdclement.palette.components.layout.TopBar
import com.alexrdclement.palette.components.util.plus
import com.alexrdclement.palette.theme.PaletteTheme
import com.alexrdclement.palette.theme.styles.ButtonStyleToken
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.flow.flowOf

private const val MediaPickerImageMimeType = "image/*"

@Composable
fun ImageLibraryScreen(
    onNavigateToImageMetadata: (imageIdValue: String) -> Unit = {},
    onNavigateToImageDelete: (imageId: String, displayName: String) -> Unit = { _, _ -> },
    viewModel: ImageLibraryViewModel = metroViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(ImageLibraryUiState.Loading)
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) {
        viewModel.onImportItemsSelected(it)
    }
    ImageLibraryScreen(
        uiState = uiState,
        onImportClick = { mediaPickerLauncher.launch(MediaPickerImageMimeType) },
        onNavigateToImageMetadata = onNavigateToImageMetadata,
        onNavigateToImageDelete = onNavigateToImageDelete,
    )
}

@Composable
fun ImageLibraryScreen(
    uiState: ImageLibraryUiState,
    onImportClick: () -> Unit,
    onNavigateToImageMetadata: (imageIdValue: String) -> Unit = {},
    onNavigateToImageDelete: (imageId: String, displayName: String) -> Unit = { _, _ -> },
) {
    Scaffold(
        topBar = {
            TopBar(
                title = {
                    Text(
                        text = "Image Library",
                        style = PaletteTheme.styles.text.headline,
                    )
                },
                actions = {
                    when (uiState) {
                        ImageLibraryUiState.Loading -> {}
                        ImageLibraryUiState.Empty -> {}
                        is ImageLibraryUiState.Content -> {
                            Button(
                                onClick = onImportClick,
                                contentPadding = ButtonDefaults.ContentPaddingDefault,
                                style = ButtonStyleToken.Secondary,
                                modifier = Modifier
                                    .wrapContentSize()
                            ) {
                                Text(
                                    text = "Import",
                                    style = PaletteTheme.styles.text.bodySmall,
                                )
                            }
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        when (uiState) {
            ImageLibraryUiState.Loading -> {}
            ImageLibraryUiState.Empty -> EmptyContent(
                onImportClick = onImportClick,
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
            )
            is ImageLibraryUiState.Content -> ImageGrid(
                uiState = uiState,
                onNavigateToImageMetadata = onNavigateToImageMetadata,
                onNavigateToImageDelete = onNavigateToImageDelete,
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun EmptyContent(
    onImportClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(contentPadding),
    ) {
        Button(onClick = onImportClick) {
            Text("Import local images")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageGrid(
    uiState: ImageLibraryUiState.Content,
    onNavigateToImageMetadata: (imageIdValue: String) -> Unit,
    onNavigateToImageDelete: (imageId: String, displayName: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val images = uiState.images.collectAsLazyPagingItems()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        contentPadding = contentPadding.plus(WindowInsets.navigationBars.asPaddingValues()),
        horizontalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small),
        modifier = modifier,
    ) {
        items(images.itemCount) { index ->
            val image = images[index] ?: return@items
            var dropdownExpanded by remember { mutableStateOf(false) }
            var touchOffset by remember { mutableStateOf(Offset.Zero) }
            Box {
                MediaItemArtwork(
                    imageUrl = image.uri,
                    modifier = Modifier
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                awaitFirstDown(requireUnconsumed = false).also { touchOffset = it.position }
                            }
                        }
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = PaletteTheme.indication,
                            onClick = {},
                            onLongClick = { dropdownExpanded = true },
                        ),
                )
                ImageContextMenu(
                    expanded = dropdownExpanded,
                    offset = touchOffset,
                    onDismissRequest = { dropdownExpanded = false },
                    onNavigateToMetadata = { onNavigateToImageMetadata(image.id.value) },
                    onNavigateToDelete = { onNavigateToImageDelete(image.id.value, image.uri) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun EmptyPreview() {
    PaletteTheme {
        ImageLibraryScreen(
            uiState = ImageLibraryUiState.Empty,
            onImportClick = {},
        )
    }
}

@Preview
@Composable
private fun ContentPreview() {
    PaletteTheme {
        val image = Image(id = ImageId("1"), uri = "file:/images/1.jpg")
        ImageLibraryScreen(
            uiState = ImageLibraryUiState.Content(
                images = flowOf(PagingData.from(listOf(image))),
            ),
            onImportClick = {},
        )
    }
}
