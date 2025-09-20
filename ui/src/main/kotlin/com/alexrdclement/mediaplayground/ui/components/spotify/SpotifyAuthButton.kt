package com.alexrdclement.mediaplayground.ui.components.spotify

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alexrdclement.mediaplayground.ui.theme.ButtonSpace
import com.alexrdclement.uiplayground.components.core.Button
import com.alexrdclement.uiplayground.components.core.Text
import com.alexrdclement.uiplayground.theme.PlaygroundTheme

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
            SpotifyAuthButtonStyle.Compact -> ButtonSpace.ContentPaddingCompact
            SpotifyAuthButtonStyle.Default -> ButtonSpace.ContentPaddingDefault
        },
        modifier = Modifier.wrapContentSize(),
    ) {
        Text(
            text = if (!isLoggedIn) "Log In" else "Log Out",
            style = when (style) {
                SpotifyAuthButtonStyle.Compact -> PlaygroundTheme.typography.labelMedium
                SpotifyAuthButtonStyle.Default -> PlaygroundTheme.typography.labelLarge
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    PlaygroundTheme {
        SpotifyAuthButton(
            isLoggedIn = false,
            onClick = { /*TODO*/ },
            style = SpotifyAuthButtonStyle.Default,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CompactPreview() {
    PlaygroundTheme {
        SpotifyAuthButton(
            isLoggedIn = false,
            onClick = { /*TODO*/ },
            style = SpotifyAuthButtonStyle.Compact,
        )
    }
}
