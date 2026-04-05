package com.alexrdclement.mediaplayground.feature.image.delete

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.palette.components.layout.dialog.DeleteConfirmationDialogContent
import com.alexrdclement.palette.components.layout.dialog.IndeterminateProgressDialogContent
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun ImageDeleteScreen(
    imageId: ImageId,
    displayName: String,
    onNavigateBack: () -> Unit,
) {
    val viewModel = assistedMetroViewModel<ImageDeleteViewModel, ImageDeleteViewModel.Factory>(
        key = "delete_${imageId.value}",
    ) { create(imageId.value) }
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
