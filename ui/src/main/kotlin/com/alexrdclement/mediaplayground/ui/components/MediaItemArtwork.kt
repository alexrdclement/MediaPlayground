package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import coil3.compose.AsyncImage
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.palette.theme.PaletteTheme

@Composable
fun MediaItemArtwork(
    uri: MediaAssetUri?,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    if (uri != null) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = modifier
                .alpha(if (isEnabled) 1f else PaletteTheme.colorScheme.disabledContentAlpha)
        )
    } else {
        // TODO: fallback image
    }
}
