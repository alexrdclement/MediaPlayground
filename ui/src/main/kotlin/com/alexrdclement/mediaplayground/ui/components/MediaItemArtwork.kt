package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import coil3.compose.AsyncImage
import com.alexrdclement.mediaplayground.ui.shared.theme.DisabledAlpha

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
                .aspectRatio(1f)
                .alpha(if (isEnabled) 1f else DisabledAlpha)
        )
    } else {
        // TODO: fallback image
    }
}
