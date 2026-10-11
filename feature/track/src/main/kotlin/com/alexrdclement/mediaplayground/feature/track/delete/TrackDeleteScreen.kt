package com.alexrdclement.mediaplayground.feature.track.delete

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.embarrasdf.palette.components.layout.dialog.DeleteConfirmationDialogContent
import com.embarrasdf.palette.components.layout.dialog.IndeterminateProgressDialogContent
import com.embarrasdf.palette.theme.PaletteTheme
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun TrackDeleteScreen(
    trackId: TrackId,
    displayName: String,
    onNavigateBack: () -> Unit,
) {
    val viewModel = assistedMetroViewModel<TrackDeleteViewModel, TrackDeleteViewModel.Factory>(
        key = "delete_${trackId.value}",
    ) { create(trackId.value) }
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()

    val currentOnNavigateBack by rememberUpdatedState(onNavigateBack)
    DisposableEffect(deleteState) {
        if (deleteState is DeleteState.Deleted) currentOnNavigateBack()
        onDispose {}
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
