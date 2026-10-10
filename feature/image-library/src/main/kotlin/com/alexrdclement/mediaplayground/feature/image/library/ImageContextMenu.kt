package com.alexrdclement.mediaplayground.feature.image.library

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.components.menu.ContextMenu
import com.alexrdclement.palette.components.menu.DropdownMenuItem
import com.alexrdclement.palette.theme.PaletteTheme

@Composable
internal fun ImageContextMenu(
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
