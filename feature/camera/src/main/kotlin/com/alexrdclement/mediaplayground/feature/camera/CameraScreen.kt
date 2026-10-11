package com.alexrdclement.mediaplayground.feature.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alexrdclement.mediaplayground.media.ui.CameraPreview
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.core.Surface
import com.mohamedrejeb.calf.permissions.Camera
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.PermissionStatus
import com.mohamedrejeb.calf.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(modifier: Modifier = Modifier) {
    val cameraPermissionState = rememberPermissionState(Permission.Camera)

    Surface(
        modifier = modifier
            .fillMaxSize(),
    ) {
        when (cameraPermissionState.status) {
            is PermissionStatus.Denied -> {
                Box(
                    contentAlignment = Alignment.Center,
                    // safeDrawing rather than statusBars: insetting only the top shifts the
                    // centered content down by half the status bar height.
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                ) {
                    Button(
                        onClick = cameraPermissionState::launchPermissionRequest,
                        style = PaletteTheme.component.core.button.primary,
                    ) {
                        Text(
                            text = "Request permission",
                            style = PaletteTheme.component.core.text.bodyMedium.copy(
                                color = PaletteTheme.semantic.color.onPrimary,
                            ),
                        )
                    }
                }
            }
            PermissionStatus.Granted -> {
                CameraPreview(
                    onReady = {},
                    onError = {},
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }
        }
    }
}
