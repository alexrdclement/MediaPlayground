package com.alexrdclement.mediaplayground.feature.audio.library

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.menu.ContextMenu
import com.embarrasdf.palette.components.menu.DropdownMenuItem
import com.embarrasdf.palette.theme.PaletteTheme

@Composable
internal fun AlbumContextMenu(
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
        style = PaletteTheme.component.menu.dropdownMenu,
    ) {
        DropdownMenuItem(
            text = { Text("Metadata", style = PaletteTheme.component.core.text.bodyMedium) },
            onClick = {
                onDismissRequest()
                onNavigateToMetadata()
            },
            style = PaletteTheme.component.menu.dropdownMenu.itemStyle,
        )
        DropdownMenuItem(
            text = { Text("Delete", style = PaletteTheme.component.core.text.bodyMedium) },
            onClick = {
                onDismissRequest()
                onNavigateToDelete()
            },
            style = PaletteTheme.component.menu.dropdownMenu.itemStyle,
        )
    }
}
