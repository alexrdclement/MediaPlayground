package com.alexrdclement.mediaplayground.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.ui.R
import com.alexrdclement.palette.components.media.model.Artist

@Composable
@JvmName("artistNamesOrDefaultArtist")
fun artistNamesOrDefault(artists: List<Artist>): String {
    return artistNamesOrDefault(artists.map { it.name})
}

@Composable
@JvmName("artistNamesOrDefaultSimpleArtist")
fun artistNamesOrDefault(artists: List<SimpleArtist>): String {
    return artistNamesOrDefault(artists.map { it.name })
}

@Composable
fun artistNamesOrDefault(artists: List<String?>): String {
    return when {
        artists.isEmpty() -> stringResource(id = R.string.artist_name_fallback)
        artists.all { it == null } -> stringResource(id = R.string.artists_name_fallback)
        else -> artists.mapNotNull { it }.joinToString()
    }
}
