package com.alexrdclement.mediaplayground.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.ui.R

@Composable
fun artistNamesOrDefault(artists: List<SimpleArtist>): String {
    return when {
        artists.isEmpty() -> stringResource(id = R.string.artist_name_fallback)
        artists.all { it.name == null } -> stringResource(id = R.string.artists_name_fallback)
        else -> artists.mapNotNull { it.name }.joinToString()
    }
}
