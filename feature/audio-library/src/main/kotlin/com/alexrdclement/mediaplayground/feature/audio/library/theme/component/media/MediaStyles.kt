package com.alexrdclement.mediaplayground.feature.audio.library.theme.component.media

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.alexrdclement.mediaplayground.feature.audio.library.ContentReadyStyle
import com.alexrdclement.mediaplayground.feature.audio.library.content.AudioLibraryContentStyle
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContentStyle
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalEmptyContentStyle
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalMediaContentStyle
import com.alexrdclement.mediaplayground.ui.theme.component.media.mediaItemRow
import com.embarrasdf.palette.components.core.ButtonDefaults
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.media.MediaStyles

val MediaStyles.audioLibraryContent: AudioLibraryContentStyle
    @Composable get() = AudioLibraryContentStyle(
        contentPadding = PaddingValues(
            vertical = PaletteTheme.semantic.dimension.spacing.small,
        ),
        contentSpacing = PaletteTheme.semantic.dimension.spacing.small,
        headerPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.none),
        headerMinHeight = ButtonDefaults.MinHeight,
        headerTextStyle = PaletteTheme.component.core.text.titleLarge,
    )

val MediaStyles.localEmptyContent: LocalEmptyContentStyle
    @Composable get() = LocalEmptyContentStyle(
        contentPadding = PaddingValues(
            vertical = PaletteTheme.semantic.dimension.spacing.small,
        ),
        buttonStyle = PaletteTheme.component.core.button.primary,
        buttonTextStyle = PaletteTheme.component.core.text.bodyMedium.copy(
            color = PaletteTheme.semantic.color.onPrimary,
        ),
    )

val MediaStyles.localMediaContent: LocalMediaContentStyle
    @Composable get() = LocalMediaContentStyle(
        contentSpacing = PaletteTheme.semantic.dimension.spacing.medium,
        itemRowStyle = mediaItemRow,
    )

val MediaStyles.localContent: LocalContentStyle
    @Composable get() = LocalContentStyle(
        contentPadding = PaddingValues(
            horizontal = PaletteTheme.semantic.dimension.spacing.medium,
        ),
        headerStyle = audioLibraryContent,
        importButtonStyle = PaletteTheme.component.core.button.secondary,
        importButtonTextStyle = PaletteTheme.component.core.text.bodySmall.copy(
            color = PaletteTheme.semantic.color.secondary,
        ),
        emptyContentStyle = localEmptyContent,
        mediaContentStyle = localMediaContent,
    )

val MediaStyles.contentReady: ContentReadyStyle
    @Composable get() = ContentReadyStyle(
        contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.none),
        contentSpacing = PaletteTheme.semantic.dimension.spacing.small,
        localContentStyle = localContent,
    )
