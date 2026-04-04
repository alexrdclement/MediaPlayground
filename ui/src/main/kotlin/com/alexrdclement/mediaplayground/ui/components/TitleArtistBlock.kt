package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.theme.PaletteTheme
import com.alexrdclement.palette.theme.styles.copy

@Composable
fun TitleArtistBlock(
    title: String,
    artists: String?,
    onTitleLongClick: (Offset) -> Unit,
    onArtistsLongClick: (Offset) -> Unit,
    modifier: Modifier = Modifier,
    titleMaxLines: Int = 1,
    artistMaxLines: Int = 1,
    titleTextAlign: TextAlign = TextAlign.Center,
    artistTextAlign: TextAlign = TextAlign.Center,
    titleOverlay: @Composable BoxScope.() -> Unit = {},
    artistsOverlay: @Composable BoxScope.() -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small),
        modifier = modifier
    ) {
        Box {
            var titleTouchPosition by remember { mutableStateOf(Offset.Zero) }
            Text(
                text = title,
                style = PaletteTheme.styles.text.titleLarge.copy(
                    textAlign = titleTextAlign,
                ),
                maxLines = titleMaxLines,
                modifier = Modifier
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            awaitFirstDown(requireUnconsumed = false).also { titleTouchPosition = it.position }
                        }
                    }
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = PaletteTheme.indication,
                        onClick = {},
                        onLongClick = { onTitleLongClick(titleTouchPosition) },
                    )
                    .then(
                        if (titleMaxLines > 1) Modifier else Modifier.basicMarquee()
                    )
            )
            titleOverlay()
        }
        if (!artists.isNullOrEmpty()) {
            Box {
                var artistsTouchPosition by remember { mutableStateOf(Offset.Zero) }
                Text(
                    text = artists,
                    style = PaletteTheme.styles.text.bodyLarge.copy(
                        textAlign = artistTextAlign,
                    ),
                    maxLines = artistMaxLines,
                    modifier = Modifier
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                awaitFirstDown(requireUnconsumed = false)
                                    .also { artistsTouchPosition = it.position }
                            }
                        }
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = PaletteTheme.indication,
                            onClick = {},
                            onLongClick = { onArtistsLongClick(artistsTouchPosition) },
                        )
                        .then(
                            if (artistMaxLines > 1) Modifier else Modifier.basicMarquee()
                        )
                )
                artistsOverlay()
            }
        }
    }
}

@Preview
@Composable
fun MediaItemTitleArtistPreview() {
    PaletteTheme {
        TitleArtistBlock(
            title = "Song Title",
            artists = "Artist Name",
            onTitleLongClick = {},
            onArtistsLongClick = {},
        )
    }
}
