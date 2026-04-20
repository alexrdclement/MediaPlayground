package com.alexrdclement.mediaplayground.media.engine.mapper

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.largeImageUrl
import com.alexrdclement.mediaplayground.media.store.PathProvider
import java.io.File

fun Track.toMediaItem(pathProvider: PathProvider): MediaItem = when (this) {
    is AudioTrack -> toAudioMediaItem(pathProvider)
}

fun Track.toMediaMetadata(): MediaMetadata = when (this) {
    is AudioTrack -> toAudioMediaMetadata()
}

private fun AudioTrack.toAudioMediaItem(pathProvider: PathProvider): MediaItem {
    val trackClip = clips.firstOrNull() ?: return MediaItem.Builder().build()
    val clip = trackClip.clip
    val path = pathProvider.getPath(clip.mediaAsset.uri)
    return MediaItem.Builder()
        .setMediaId(id.value)
        .setUri(Uri.fromFile(File(path.toString())))
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
