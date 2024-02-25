package com.alexrdclement.mediaplayground.media.mediaimport.factory

import androidx.core.net.toUri
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleArtist
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import java.io.File

internal fun makeTrack(
    mediaId: String,
    file: File,
    mediaMetadata: MediaMetadata,
) = Track(
    id = TrackId(mediaId),
    title = mediaMetadata.title ?: file.name,
    artists = listOf(mediaMetadata.toSimpleArtist()),
    durationMs = mediaMetadata.durationMs?.toInt() ?: 0,
    trackNumber = null,
    uri = file.toUri().toString(),
    simpleAlbum = mediaMetadata.toSimpleAlbum(),
)
