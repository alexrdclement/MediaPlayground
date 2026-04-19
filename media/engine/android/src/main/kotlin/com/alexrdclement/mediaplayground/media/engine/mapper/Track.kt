package com.alexrdclement.mediaplayground.media.engine.mapper

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.largeImageUrl

fun Track.toMediaItem(): MediaItem = when (this) {
    is AudioTrack -> toAudioMediaItem()
}

fun Track.toMediaMetadata(): MediaMetadata = when (this) {
    is AudioTrack -> toAudioMediaMetadata()
}

private fun AudioTrack.toAudioMediaItem(): MediaItem {
    val trackClip = clips.firstOrNull() ?: return MediaItem.Builder().build()
    val clip = trackClip.clip
    return MediaItem.Builder()
        .setMediaId(id.value)
        .setUri(clip.mediaAsset.uri.toUriString())
        .setMediaMetadata(this.toAudioMediaMetadata())
        .build()
}

private fun AudioTrack.toAudioMediaMetadata(): MediaMetadata {
    return MediaMetadata.Builder()
        .setAlbumArtist(simpleAlbum.artists.joinToString { it.name ?: "Unknown artist" })
        .setArtist(simpleAlbum.artists.joinToString { it.name ?: "Unknown artist" })
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
