package com.alexrdclement.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.ui.theme.MediaPlaygroundTheme

enum class MediaSource {
    DeviceAudio,
    DeviceVideo,
    Spotify,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaSourcePickerBottomSheet(
    onDismissRequest: () -> Unit,
    onMediaSourcePicked: (MediaSource) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        MediaSourcePicker(onMediaSourcePicked)
    }
}

@Composable
fun MediaSourcePicker(
    onMediaTypePicked: (MediaSource) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Pick media source",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text("Audio", fontWeight = FontWeight.Medium)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onMediaTypePicked(MediaSource.DeviceAudio) }
            ) {
                Text("Files")
            }
            Button(
                onClick = { onMediaTypePicked(MediaSource.Spotify) }
            ) {
                Text("Spotify")
            }
        }

        Text("Video", fontWeight = FontWeight.Medium)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onMediaTypePicked(MediaSource.DeviceVideo) }
            ) {
                Text("Files")
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MediaPlaygroundTheme {
        Surface {
            MediaSourcePicker(
                onMediaTypePicked = {},
            )
        }
    }
}
