package com.alexrdclement.mediaplayground.feature.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.ui.CameraPreview
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.theme.PaletteTheme
import com.mohamedrejeb.calf.permissions.Camera
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.PermissionStatus
import com.mohamedrejeb.calf.permissions.rememberPermissionState
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = metroViewModel(),
) {
    val mediaSessionState = viewModel.mediaSessionState
    CameraScreen(
        mediaSessionState = mediaSessionState,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    mediaSessionState: MediaSessionState?,
) {
    val cameraPermissionState = rememberPermissionState(Permission.Camera)

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        style = PaletteTheme.component.core.surface.default,
    ) {
        when (cameraPermissionState.status) {
            is PermissionStatus.Denied -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) {
                    Button(
                        onClick = cameraPermissionState::launchPermissionRequest,
                        style = PaletteTheme.component.core.button.primary,
                    ) {
                        Text("Request permission", style = PaletteTheme.component.core.text.bodyMedium.copy(color = PaletteTheme.semantic.color.onPrimary))
                    }
                }
            }

            PermissionStatus.Granted -> {
                CameraPreview(
                    onReady = {},
                    onError = {},
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}
