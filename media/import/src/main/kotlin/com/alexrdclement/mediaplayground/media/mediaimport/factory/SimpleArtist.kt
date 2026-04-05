package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleArtist
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleArtist

private const val UnknownArtistName = "Unknown artist"

internal suspend fun makeSimpleArtist(
    mediaMetadata: MediaMetadata.Audio,
    getArtistByName: suspend (String) -> SimpleArtist?,
): SimpleArtist {
    val artistName = mediaMetadata.artistName ?: UnknownArtistName
    return getArtistByName(artistName) ?: mediaMetadata.toSimpleArtist(artistName)
}
