package com.alexrdclement.mediaplayground.media.ui

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.guava.await

@Composable
fun AndroidCameraPreview(
    onReady: () -> Unit,
    onError: (Exception) -> Unit,
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    preview: Preview = Preview.Builder().build(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var surfaceRequest by remember { mutableStateOf<SurfaceRequest?>(null) }

    LaunchedEffect(lifecycleOwner) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).await()

        preview.setSurfaceProvider { newSurfaceRequest ->
            onReady()
            surfaceRequest = newSurfaceRequest
        }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner = lifecycleOwner,
                cameraSelector = cameraSelector,
                preview,
            )
        } catch (e: Exception) {
            onError(e)
        }
    }

    surfaceRequest?.let { request ->
        CameraXViewfinder(
            surfaceRequest = request.also { surfaceRequest = it },
            modifier = modifier,
        )
    }
}
