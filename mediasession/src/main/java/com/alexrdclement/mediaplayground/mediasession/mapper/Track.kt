package com.alexrdclement.mediaplayground.mediasession.mapper

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.largeImageUrl

fun Track.toMediaItem(): MediaItem {
    return MediaItem.Builder()
        .setUri(previewUrl)
        .setMediaMetadata(this.toMediaMetadata())
        .build()
}

fun Track.toMediaMetadata(): MediaMetadata {
    return MediaMetadata.Builder()
        .setAlbumArtist(simpleAlbum.artists.joinToString { it.name })
        .setArtist(simpleAlbum.artists.joinToString { it.name })
        .setAlbumTitle(simpleAlbum.name)
        .setDisplayTitle(name)
        .setTitle(name)
        .apply {
            simpleAlbum.largeImageUrl?.let {
                setArtworkUri(Uri.parse(it))
            }
        }
        .build()
}
