package com.alexrdclement.ui.components.spotify

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.ui.theme.MediaPlaygroundTheme

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
        contentPadding = when (style) {
            SpotifyAuthButtonStyle.Compact -> PaddingValues(horizontal = 16.dp)
            SpotifyAuthButtonStyle.Default -> ButtonDefaults.ContentPadding
        },
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
