package com.alexrdclement.mediaplayground.feature.artist.delete

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.palette.components.layout.dialog.DeleteConfirmationDialogContent
import com.alexrdclement.palette.components.layout.dialog.IndeterminateProgressDialogContent
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun ArtistDeleteScreen(
    artistIdValue: String,
    displayName: String,
    onNavigateBack: () -> Unit,
) {
    val viewModel = assistedMetroViewModel<ArtistDeleteViewModel, ArtistDeleteViewModel.Factory>(
        key = artistIdValue,
    ) { create(artistIdValue) }
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
