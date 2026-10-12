package com.alexrdclement.mediaplayground.feature.image.delete

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.embarrasdf.palette.components.layout.dialog.DeleteConfirmationDialogContent
import com.embarrasdf.palette.components.layout.dialog.IndeterminateProgressDialogContent
import com.embarrasdf.palette.theme.PaletteTheme
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

    val currentOnNavigateBack by rememberUpdatedState(onNavigateBack)
    SideEffect(deleteState) {
        if (deleteState is DeleteState.Deleted) currentOnNavigateBack()
    }

    when (deleteState) {
        DeleteState.Confirming -> DeleteConfirmationDialogContent(
            contentTitle = displayName,
            onConfirm = viewModel::onDeleteConfirmed,
            onDismissRequest = onNavigateBack,
            style = PaletteTheme.component.layout.dialogContent,
        )
        DeleteState.Deleting, DeleteState.Deleted -> IndeterminateProgressDialogContent(
            title = "Deleting",
            style = PaletteTheme.component.layout.progressDialogContent,
        )
    }
}
