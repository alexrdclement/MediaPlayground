package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.Indication
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.ui.theme.component.media.titleArtistBlock
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.theme.PaletteTheme

data class TitleArtistBlockStyle(
    val contentSpacing: Dp = 8.dp,
    val titleMaxLines: Int = 1,
    val artistMaxLines: Int = 1,
    val titleStyle: TextStyle = TextStyle(),
    val artistStyle: TextStyle = TextStyle(),
    val indication: Indication? = null,
)

@Composable
fun TitleArtistBlock(
    title: String,
    artists: String?,
    onTitleLongClick: (Offset) -> Unit,
    onArtistsLongClick: (Offset) -> Unit,
    modifier: Modifier = Modifier,
    style: TitleArtistBlockStyle = TitleArtistBlockStyle(),
    titleOverlay: @Composable BoxScope.() -> Unit = {},
    artistsOverlay: @Composable BoxScope.() -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(style.contentSpacing),
        modifier = modifier
    ) {
        Box {
            var titleTouchPosition by remember { mutableStateOf(Offset.Zero) }
            Text(
                text = title,
                style = style.titleStyle,
                maxLines = style.titleMaxLines,
                modifier = Modifier
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            awaitFirstDown(requireUnconsumed = false).also { titleTouchPosition = it.position }
                        }
                    }
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = style.indication,
                        onClick = {},
                        onLongClick = { onTitleLongClick(titleTouchPosition) },
                    )
                    .then(
                        if (style.titleMaxLines > 1) Modifier else Modifier.basicMarquee()
                    )
            )
            titleOverlay()
        }
        if (!artists.isNullOrEmpty()) {
            Box {
                var artistsTouchPosition by remember { mutableStateOf(Offset.Zero) }
                Text(
                    text = artists,
                    style = style.artistStyle,
                    maxLines = style.artistMaxLines,
                    modifier = Modifier
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                awaitFirstDown(requireUnconsumed = false)
                                    .also { artistsTouchPosition = it.position }
                            }
                        }
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = style.indication,
                            onClick = {},
                            onLongClick = { onArtistsLongClick(artistsTouchPosition) },
                        )
                        .then(
                            if (style.artistMaxLines > 1) Modifier else Modifier.basicMarquee()
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
            style = PaletteTheme.component.media.titleArtistBlock,
        )
    }
}
