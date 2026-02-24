package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.theme.PaletteTheme
import com.alexrdclement.palette.theme.styles.copy

@Composable
fun TitleArtistBlock(
    title: String,
    artists: String?,
    modifier: Modifier = Modifier,
    titleMaxLines: Int = 1,
    artistMaxLines: Int = 1,
    titleTextAlign: TextAlign = TextAlign.Center,
    artistTextAlign: TextAlign = TextAlign.Center,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small),
        modifier = modifier
    ) {
        Text(
            text = title,
            style = PaletteTheme.styles.text.titleLarge.copy(
                textAlign = titleTextAlign,
            ),
            maxLines = titleMaxLines,
            modifier = Modifier
                .then(
                    if (titleMaxLines > 1) {
                        Modifier
                    } else {
                        Modifier.basicMarquee()
                    }
                )
        )
        if (!artists.isNullOrEmpty()) {
            Text(
                text = artists,
                style = PaletteTheme.styles.text.bodyLarge.copy(
                    textAlign = artistTextAlign,
                ),
                maxLines = artistMaxLines,
                modifier = Modifier
                    .then(
                        if (artistMaxLines > 1) {
                            Modifier
                        } else {
                            Modifier.basicMarquee()
                        }
                    )
            )
        }
    }
}

@Preview
@Composable
fun MediaItemTitleArtistPreview() {
    PaletteTheme {
        TitleArtistBlock(
            title = "Song Title",
            artists = "Artist Name"
        )
    }
}
