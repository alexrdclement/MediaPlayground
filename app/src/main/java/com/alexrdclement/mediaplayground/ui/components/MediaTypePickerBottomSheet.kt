package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.PickMediaType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaTypePickerBottomSheet(
    onDismissRequest: () -> Unit,
    onMediaTypePicked: (PickMediaType) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Pick media type")
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onMediaTypePicked(PickMediaType.Audio) }
                ) {
                    Text("Audio")
                }
                Button(
                    onClick = { onMediaTypePicked(PickMediaType.Video) }
                ) {
                    Text("Video")
                }
            }
        }
    }
}
