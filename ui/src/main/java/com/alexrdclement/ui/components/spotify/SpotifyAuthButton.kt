package com.alexrdclement.ui.components.spotify

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class SpotifyAuthButtonStyle {
    Compact,
    Default,
}

@Composable
fun SpotifyAuthButton(
    isLoggedIn: Boolean,
    onClick: () -> Unit,
    style: SpotifyAuthButtonStyle = SpotifyAuthButtonStyle.Default,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.wrapContentSize(),
    ) {
        Text(
            text = if (!isLoggedIn) "Log In" else "Log Out",
            style = when (style) {
                SpotifyAuthButtonStyle.Compact -> MaterialTheme.typography.bodySmall
                SpotifyAuthButtonStyle.Default -> MaterialTheme.typography.bodyMedium
            }
        )
    }
}
