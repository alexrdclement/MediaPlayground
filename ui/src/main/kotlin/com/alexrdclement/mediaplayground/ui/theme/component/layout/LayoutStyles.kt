package com.alexrdclement.mediaplayground.ui.theme.component.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.alexrdclement.mediaplayground.ui.components.metadata.MetadataContentStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.layout.LayoutStyles

val LayoutStyles.metadataContent: MetadataContentStyle
    @Composable get() = MetadataContentStyle(
        contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.medium),
        contentSpacing = PaletteTheme.semantic.dimension.spacing.medium,
        sectionSpacing = PaletteTheme.semantic.dimension.spacing.small,
        labelStyle = PaletteTheme.component.core.text.titleMedium,
        textFieldStyle = PaletteTheme.component.core.textField,
    )
