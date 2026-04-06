package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toArtist
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.Artist

private const val UnknownArtistName = "Unknown artist"

internal suspend fun makeArtist(
    mediaMetadata: MediaMetadata.Audio,
    getArtistByName: suspend (String) -> Artist?,
): Artist {
    val artistName = mediaMetadata.artistName ?: UnknownArtistName
    return getArtistByName(artistName) ?: mediaMetadata.toArtist(artistName)
}
