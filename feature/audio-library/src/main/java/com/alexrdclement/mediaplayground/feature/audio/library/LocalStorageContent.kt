package com.alexrdclement.mediaplayground.feature.audio.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alexrdclement.ui.theme.MediaPlaygroundTheme

@Composable
internal fun LocalStorageContent(
    onImportClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(
            onClick = onImportClick,
        ) {
            Text("Import local audio")
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MediaPlaygroundTheme {
        LocalStorageContent(
            onImportClick = {},
        )
    }
}
