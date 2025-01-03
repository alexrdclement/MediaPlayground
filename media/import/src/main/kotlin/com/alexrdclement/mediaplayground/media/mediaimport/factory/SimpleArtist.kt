package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleArtist
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist

private const val UnknownArtistName = "Unknown artist"

internal suspend fun makeSimpleArtist(
    mediaMetadata: MediaMetadata,
    getArtistByName: suspend (String) -> SimpleArtist?,
): SimpleArtist {
    val artistName = mediaMetadata.artistName ?: UnknownArtistName
    return getArtistByName(artistName) ?: mediaMetadata.toSimpleArtist(artistName)
}
