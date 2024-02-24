package com.alexrdclement.mediaplayground.media.mediaimport.factory

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toSimpleArtist
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId

internal fun makeTrack(
    mediaId: String,
    fileName: String,
    contentUri: Uri,
    mediaMetadata: MediaMetadata,
) = Track(
    id = TrackId(mediaId),
    title = mediaMetadata.title ?: fileName,
    artists = listOf(mediaMetadata.toSimpleArtist()),
    durationMs = mediaMetadata.durationMs?.toInt() ?: 0,
    trackNumber = null,
    uri = contentUri.toString(),
    simpleAlbum = mediaMetadata.toSimpleAlbum(),
)
