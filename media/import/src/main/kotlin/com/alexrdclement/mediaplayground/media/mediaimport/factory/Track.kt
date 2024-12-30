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

private const val UnknownArtistName = "Unknown artist"
private const val UnknownAlbumName = "Unknown album"

internal suspend fun makeTrack(
    mediaId: String,
    path: Path,
    mediaMetadata: MediaMetadata,
    getArtistByName: suspend (String) -> SimpleArtist?,
    getAlbumByTitleAndArtistId: suspend (String, String) -> SimpleAlbum?,
): Track {
    val artistName = mediaMetadata.artistName ?: UnknownArtistName
    val albumName = mediaMetadata.albumTitle ?: UnknownAlbumName
    val simpleArtist = getArtistByName(artistName) ?: mediaMetadata.toSimpleArtist(artistName)
    val simpleAlbum =
        getAlbumByTitleAndArtistId(albumName, simpleArtist.id) ?: mediaMetadata.toSimpleAlbum(
            title = albumName,
            artists = listOf(simpleArtist),
            images = mediaMetadata.toImage()?.let(::listOf) ?: emptyList(),
        )
    return Track(
        id = TrackId(mediaId),
        title = mediaMetadata.title ?: path.name,
        artists = listOf(simpleArtist),
        durationMs = mediaMetadata.durationMs?.toInt() ?: 0,
        trackNumber = mediaMetadata.trackNumber,
        uri = path.toString(),
        simpleAlbum = simpleAlbum,
    )
}
