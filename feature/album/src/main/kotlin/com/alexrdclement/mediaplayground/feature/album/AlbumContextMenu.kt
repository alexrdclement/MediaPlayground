package com.alexrdclement.mediaplayground.feature.album

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.menu.ContextMenu
import com.embarrasdf.palette.components.menu.DropdownMenuItem
import com.embarrasdf.palette.theme.PaletteTheme

@Composable
fun AlbumContextMenu(
    expanded: Boolean,
    offset: Offset,
    onDismissRequest: () -> Unit,
    onNavigateToMetadata: () -> Unit,
    onNavigateToDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ContextMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = offset,
        modifier = modifier,
        style = PaletteTheme.component.menu.dropdownMenu,
    ) {
        DropdownMenuItem(
            text = { Text(text = "Metadata", style = PaletteTheme.component.core.text.bodyMedium) },
            onClick = {
                onDismissRequest()
                onNavigateToMetadata()
            },
            style = PaletteTheme.component.menu.dropdownMenuItem,
        )
        DropdownMenuItem(
            text = { Text(text = "Delete", style = PaletteTheme.component.core.text.bodyMedium) },
            onClick = {
                onDismissRequest()
                onNavigateToDelete()
            },
            style = PaletteTheme.component.menu.dropdownMenuItem,
        )
    }
}
