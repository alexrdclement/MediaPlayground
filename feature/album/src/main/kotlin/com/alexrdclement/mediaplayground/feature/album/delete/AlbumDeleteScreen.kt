package com.alexrdclement.mediaplayground.feature.album.delete

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.palette.components.layout.dialog.DeleteConfirmationDialogContent
import com.alexrdclement.palette.components.layout.dialog.IndeterminateProgressDialogContent
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun AlbumDeleteScreen(
    albumId: AlbumId,
    displayName: String,
    onNavigateBack: () -> Unit,
) {
    val viewModel = assistedMetroViewModel<AlbumDeleteViewModel, AlbumDeleteViewModel.Factory>(
        key = "delete_${albumId.value}",
    ) { create(albumId.value) }
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()

    LaunchedEffect(deleteState) {
        if (deleteState is DeleteState.Deleted) onNavigateBack()
    }

    when (deleteState) {
        DeleteState.Confirming -> DeleteConfirmationDialogContent(
            contentTitle = displayName,
            onConfirm = viewModel::onDeleteConfirmed,
            onDismissRequest = onNavigateBack,
        )
        DeleteState.Deleting, DeleteState.Deleted -> IndeterminateProgressDialogContent(
            title = "Deleting",
        )
    }
}
