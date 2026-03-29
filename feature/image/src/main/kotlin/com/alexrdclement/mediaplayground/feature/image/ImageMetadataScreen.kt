package com.alexrdclement.mediaplayground.feature.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.audio.Image
import com.alexrdclement.mediaplayground.ui.components.MediaItemArtwork
import com.alexrdclement.palette.components.core.IndeterminateProgressIndicator
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.components.layout.Scaffold
import com.alexrdclement.palette.components.layout.TopBar
import com.alexrdclement.palette.components.navigation.BackNavigationButton
import com.alexrdclement.palette.components.util.plus
import com.alexrdclement.palette.theme.PaletteTheme
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun ImageMetadataScreen(
    albumId: AlbumId,
    imageIndex: Int,
    onNavigateBack: () -> Unit,
) {
    val viewModel: ImageMetadataViewModel = assistedMetroViewModel<ImageMetadataViewModel, ImageMetadataViewModel.Factory>(
        key = "${albumId.value}:${imageIndex}",
    ) {
        create(albumId.value, imageIndex)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(ImageMetadataUiState.Loading)
    ImageMetadataScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun ImageMetadataScreen(
    uiState: ImageMetadataUiState,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = { Text("Image", style = PaletteTheme.styles.text.headline) },
                navButton = { BackNavigationButton(onClick = onNavigateBack) },
            )
        },
    ) { innerPadding ->
        when (uiState) {
            ImageMetadataUiState.Loading -> IndeterminateProgressIndicator()
            ImageMetadataUiState.Error -> Text("Failed to load image.")
            is ImageMetadataUiState.Loaded -> LoadedContent(
                image = uiState.image,
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun LoadedContent(
    image: Image,
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
                imageUrl = image.uri,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small)) {
                Text("URI", style = PaletteTheme.styles.text.titleMedium)
                Text(image.uri, style = PaletteTheme.styles.text.bodyMedium)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small)) {
                Text("Dimensions", style = PaletteTheme.styles.text.titleMedium)
                val dimensions = when {
                    image.widthPx != null && image.heightPx != null ->
                        "${image.widthPx} \u00d7 ${image.heightPx}"
                    else -> "Unknown"
                }
                Text(dimensions, style = PaletteTheme.styles.text.bodyMedium)
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
                image = Image(uri = "file:/1/1.jpg"),
            ),
            onNavigateBack = {},
        )
    }
}
