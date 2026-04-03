package com.alexrdclement.mediaplayground.feature.album

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.components.menu.ContextMenu
import com.alexrdclement.palette.components.menu.DropdownMenuItem

@Composable
fun ArtistContextMenu(
    expanded: Boolean,
    offset: Offset,
    onDismissRequest: () -> Unit,
    onNavigateToMetadata: () -> Unit,
    onNavigateToDelete: () -> Unit,
) {
    ContextMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = offset,
    ) {
        DropdownMenuItem(
            text = { Text("Metadata") },
            onClick = {
                onDismissRequest()
                onNavigateToMetadata()
            },
        )
        DropdownMenuItem(
            text = { Text("Delete") },
            onClick = {
                onDismissRequest()
                onNavigateToDelete()
            },
        )
    }
}
