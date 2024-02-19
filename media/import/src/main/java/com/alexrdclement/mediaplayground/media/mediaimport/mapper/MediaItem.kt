package com.alexrdclement.mediaplayground.media.mediaimport.mapper

import androidx.media3.common.MediaItem
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Image
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import java.util.UUID

fun MediaItem.toTrack(
    mediaMetadata: MediaMetadata,
): Track {
    return Track(
        id = if (mediaId.isNotEmpty()) {
            TrackId(mediaId)
        } else {
            TrackId(UUID.randomUUID().toString())
        },
        title = mediaMetadata.title ?: "Unknown",
        artists = listOf(mediaMetadata.toSimpleArtist()),
        durationMs = mediaMetadata.durationMs?.toInt() ?: 0,
        trackNumber = null,
        previewUrl = null,
        simpleAlbum = mediaMetadata.toSimpleAlbum(),
    )
}

fun MediaMetadata.toSimpleArtist(): SimpleArtist {
    return SimpleArtist(
        id = UUID.randomUUID().toString(),
        name = artistName ?: "Unknown artist",
    )
}

fun MediaMetadata.toSimpleAlbum(): SimpleAlbum {
    return SimpleAlbum(
        id = AlbumId(UUID.randomUUID().toString()),
        name = albumTitle ?: "Unknown album",
        artists = listOf(this.toSimpleArtist()),
        images = this.toAlbumArtwork()?.let { listOf(it) } ?: emptyList()
    )
}

fun MediaMetadata.toAlbumArtwork(): Image? {
    // TODO
    return null
}
