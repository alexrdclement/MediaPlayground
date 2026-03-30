package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import coil3.compose.AsyncImage
import com.alexrdclement.palette.theme.PaletteTheme

@Composable
fun MediaItemArtwork(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = modifier
                .alpha(if (isEnabled) 1f else PaletteTheme.colorScheme.disabledContentAlpha)
        )
    } else {
        // TODO: fallback image
    }
}
