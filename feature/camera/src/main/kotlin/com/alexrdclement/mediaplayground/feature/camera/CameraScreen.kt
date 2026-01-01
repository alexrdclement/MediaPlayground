package com.alexrdclement.mediaplayground.feature.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.ui.CameraPreview
import com.alexrdclement.palette.components.core.Button
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.theme.PaletteTheme
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.PermissionStatus
import com.mohamedrejeb.calf.permissions.rememberPermissionState

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
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
        color = PaletteTheme.colorScheme.surface,
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
                    ) {
                        Text("Request permission")
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
