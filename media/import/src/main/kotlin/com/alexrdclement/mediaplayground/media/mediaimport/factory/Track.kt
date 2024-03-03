package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleArtist
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlinx.io.files.Path

internal fun makeTrack(
    mediaId: String,
    path: Path,
    mediaMetadata: MediaMetadata,
) = Track(
    id = TrackId(mediaId),
    title = mediaMetadata.title ?: path.name,
    artists = listOf(mediaMetadata.toSimpleArtist()),
    durationMs = mediaMetadata.durationMs?.toInt() ?: 0,
    trackNumber = null,
    uri = path.toString(),
    simpleAlbum = mediaMetadata.toSimpleAlbum(),
)
