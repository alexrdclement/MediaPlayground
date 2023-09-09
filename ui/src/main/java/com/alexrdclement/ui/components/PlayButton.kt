package com.alexrdclement.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.alexrdclement.ui.shared.theme.DisabledAlpha

@Composable
fun PlayButton(
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Image(
        imageVector = Icons.Default.PlayArrow,
        contentDescription = "Play",
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.primary.copy(
                    alpha = if (isEnabled) 1f else DisabledAlpha,
                ),
                shape = CircleShape,
            )
            .clip(CircleShape)
            .alpha(if (isEnabled) 1f else DisabledAlpha)
            .clickable(enabled = isEnabled) { onClick() }
    )
}
