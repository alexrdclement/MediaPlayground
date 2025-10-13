package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.alexrdclement.uiplayground.components.core.Text
import com.alexrdclement.uiplayground.theme.PlaygroundTheme

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
        verticalArrangement = Arrangement.spacedBy(PlaygroundTheme.spacing.small),
        modifier = modifier
    ) {
        Text(
            text = title,
            style = PlaygroundTheme.typography.titleLarge.merge(
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
                style = PlaygroundTheme.typography.bodyLarge.merge(
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
    PlaygroundTheme {
        TitleArtistBlock(
            title = "Song Title",
            artists = "Artist Name"
        )
    }
}
