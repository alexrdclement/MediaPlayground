package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alexrdclement.uiplayground.components.core.Button
import com.alexrdclement.uiplayground.components.core.ButtonDefaults
import com.alexrdclement.uiplayground.components.core.Text
import com.alexrdclement.uiplayground.theme.PlaygroundTheme

enum class AuthButtonStyle {
    Compact,
    Default,
}

@Composable
fun AuthButton(
    isLoggedIn: Boolean,
    onClick: () -> Unit,
    style: AuthButtonStyle = AuthButtonStyle.Default,
) {
    Button(
        onClick = onClick,
        contentPadding = when (style) {
            AuthButtonStyle.Compact -> ButtonDefaults.ContentPaddingCompact
            AuthButtonStyle.Default -> ButtonDefaults.ContentPaddingDefault
        },
        modifier = Modifier.wrapContentSize(),
    ) {
        Text(
            text = if (!isLoggedIn) "Log In" else "Log Out",
            style = when (style) {
                AuthButtonStyle.Compact -> PlaygroundTheme.typography.labelMedium
                AuthButtonStyle.Default -> PlaygroundTheme.typography.labelLarge
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    PlaygroundTheme {
        AuthButton(
            isLoggedIn = false,
            onClick = {},
            style = AuthButtonStyle.Default,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CompactPreview() {
    PlaygroundTheme {
        AuthButton(
            isLoggedIn = false,
            onClick = {},
            style = AuthButtonStyle.Compact,
        )
    }
}
