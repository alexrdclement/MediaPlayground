package com.alexrdclement.mediaplayground.media.session.mapper

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.largeImageUrl

fun Track.toMediaItem(): MediaItem {
    return MediaItem.Builder()
        .setMediaId(id.value)
        .setUri(uri)
        .setMediaMetadata(this.toMediaMetadata())
        .build()
}

fun Track.toMediaMetadata(): MediaMetadata {
    return MediaMetadata.Builder()
        .setAlbumArtist(simpleAlbum.artists.joinToString { it.name })
        .setArtist(simpleAlbum.artists.joinToString { it.name })
        .setAlbumTitle(simpleAlbum.name)
        .setDisplayTitle(title)
        .setTitle(title)
        .apply {
            simpleAlbum.largeImageUrl?.let {
                setArtworkUri(Uri.parse(it))
            }
        }
        .build()
}
