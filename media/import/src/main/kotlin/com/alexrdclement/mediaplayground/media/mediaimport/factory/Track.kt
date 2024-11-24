package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toImage
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleArtist
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlinx.io.files.Path

internal suspend fun makeTrack(
    mediaId: String,
    path: Path,
    mediaMetadata: MediaMetadata,
    getArtistByName: suspend (String) -> SimpleArtist?,
    getAlbumByTitleAndArtistId: suspend (String, String) -> SimpleAlbum?,
): Track {
    val simpleArtist = mediaMetadata.artistName?.let { getArtistByName(it) } ?: mediaMetadata.toSimpleArtist()
    val simpleAlbum = mediaMetadata.albumTitle?.let {
        getAlbumByTitleAndArtistId(mediaMetadata.albumTitle, simpleArtist.id)
    } ?: mediaMetadata.toSimpleAlbum(
        artists = listOf(simpleArtist),
        images = mediaMetadata.toImage()?.let(::listOf) ?: emptyList(),
    )
    return Track(
        id = TrackId(mediaId),
        title = mediaMetadata.title ?: path.name,
        artists = listOf(simpleArtist),
        durationMs = mediaMetadata.durationMs?.toInt() ?: 0,
        trackNumber = null,
        uri = path.toString(),
        simpleAlbum = simpleAlbum,
    )
}
