package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MediaPickerButton(
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(text = "Pick Media")
    }
}
