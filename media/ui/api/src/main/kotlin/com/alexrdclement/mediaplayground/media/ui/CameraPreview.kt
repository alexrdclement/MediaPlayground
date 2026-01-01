package com.alexrdclement.mediaplayground.media.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CameraPreview(
    onReady: () -> Unit,
    onError: (Exception) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO KMP: expect/actual
    AndroidCameraPreview(
        onReady = onReady,
        onError = onError,
        modifier = modifier,
    )
}
