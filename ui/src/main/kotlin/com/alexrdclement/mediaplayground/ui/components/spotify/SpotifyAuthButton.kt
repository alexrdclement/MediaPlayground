package com.alexrdclement.mediaplayground.ui.components.spotify

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alexrdclement.mediaplayground.ui.theme.ButtonSpace
import com.alexrdclement.mediaplayground.ui.theme.MediaPlaygroundTheme

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
    OutlinedButton(
        onClick = onClick,
        contentPadding = when (style) {
            SpotifyAuthButtonStyle.Compact -> ButtonSpace.ContentPaddingCompact
            SpotifyAuthButtonStyle.Default -> ButtonSpace.ContentPaddingDefault
        },
        modifier = Modifier.wrapContentSize(),
    ) {
        Text(
            text = if (!isLoggedIn) "Log In" else "Log Out",
            style = when (style) {
                SpotifyAuthButtonStyle.Compact -> MaterialTheme.typography.labelMedium
                SpotifyAuthButtonStyle.Default -> MaterialTheme.typography.labelLarge
            }
        )
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    MediaPlaygroundTheme {
        SpotifyAuthButton(
            isLoggedIn = false,
            onClick = { /*TODO*/ },
            style = SpotifyAuthButtonStyle.Default,
        )
    }
}

@Preview
@Composable
private fun CompactPreview() {
    MediaPlaygroundTheme {
        SpotifyAuthButton(
            isLoggedIn = false,
            onClick = { /*TODO*/ },
            style = SpotifyAuthButtonStyle.Compact,
        )
    }
}
