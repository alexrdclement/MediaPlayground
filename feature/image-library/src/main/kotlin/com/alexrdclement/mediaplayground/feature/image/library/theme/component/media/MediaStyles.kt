package com.alexrdclement.mediaplayground.feature.image.library.theme.component.media

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.alexrdclement.mediaplayground.feature.image.library.ImageGridStyle
import com.alexrdclement.mediaplayground.feature.image.library.ImageLibraryEmptyContentStyle
import com.alexrdclement.mediaplayground.ui.theme.component.media.mediaItemGridMinItemWidth
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.media.MediaStyles

val MediaStyles.imageLibraryEmptyContent: ImageLibraryEmptyContentStyle
    @Composable get() = ImageLibraryEmptyContentStyle(
        contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.none),
        buttonStyle = PaletteTheme.component.core.button.primary,
        buttonTextStyle = PaletteTheme.component.core.text.bodyMedium.copy(
            color = PaletteTheme.semantic.color.onPrimary,
        ),
    )

val MediaStyles.imageGrid: ImageGridStyle
    @Composable get() = ImageGridStyle(
        contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.none),
        minItemWidth = mediaItemGridMinItemWidth,
        itemSpacing = PaletteTheme.semantic.dimension.spacing.small,
        indication = PaletteTheme.semantic.indication,
    )
