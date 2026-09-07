package com.alexrdclement.mediaplayground.feature.audio.library.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.theme.PaletteTheme

@Composable
fun AudioLibraryContent(
    headerText: String,
    headerPadding: PaddingValues = PaddingValues(PaletteTheme.semantic.dimension.spacing.none),
    headerAction: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.semantic.dimension.spacing.small),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PaletteTheme.semantic.dimension.spacing.small)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = 40.dp) // Match min button height
                .padding(headerPadding)
        ) {
            Text(
                text = headerText,
                style = PaletteTheme.component.core.text.titleLarge,
            )
            headerAction?.invoke()
        }
        content()
    }
}
