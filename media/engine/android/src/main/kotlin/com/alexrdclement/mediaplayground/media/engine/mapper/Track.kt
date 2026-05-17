package com.alexrdclement.mediaplayground.media.engine.mapper

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.AlbumTrack
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.largeImageUri
import com.alexrdclement.mediaplayground.media.store.PathProvider
import java.io.File

fun Track.toMediaItem(pathProvider: PathProvider): MediaItem = when (this) {
    is AudioTrack -> toAudioMediaItem(pathProvider)
}

fun AlbumTrack.toMediaItem(pathProvider: PathProvider): MediaItem {
    val trackClip = track.items.firstOrNull() ?: return MediaItem.Builder().build()
    val clip = trackClip.clip
    val path = pathProvider.getPath(clip.mediaAsset.uri)
    return MediaItem.Builder()
        .setMediaId(track.id.value)
        .setUri(Uri.fromFile(File(path.toString())))
        .setMediaMetadata(toAlbumTrackMediaMetadata(pathProvider))
        .build()
}

private fun AlbumTrack.toAlbumTrackMediaMetadata(pathProvider: PathProvider): MediaMetadata {
    val artistString = artists.joinToString { it.name ?: "Unknown artist" }
    return MediaMetadata.Builder()
        .setAlbumArtist(artistString)
        .setArtist(artistString)
        .setDisplayTitle(track.title)
        .setTitle(track.title)
        .apply {
            largeImageUri?.let { mediaUri ->
                val artworkPath = pathProvider.getPath(mediaUri)
                setArtworkUri(Uri.fromFile(File(artworkPath.toString())))
            }
        }
        .build()
}

internal fun AudioTrack.toAudioMediaItem(pathProvider: PathProvider): MediaItem {
    val trackClip = items.firstOrNull() ?: return MediaItem.Builder().build()
    val clip = trackClip.clip
    val path = pathProvider.getPath(clip.mediaAsset.uri)
    return MediaItem.Builder()
        .setMediaId(id.value)
        .setUri(Uri.fromFile(File(path.toString())))
        .setMediaMetadata(toAudioMediaMetadata(pathProvider))
        .build()
}

private fun AudioTrack.toAudioMediaMetadata(pathProvider: PathProvider): MediaMetadata {
    return MediaMetadata.Builder()
        .setDisplayTitle(title)
        .setTitle(title)
        .apply {
            largeImageUri?.let { mediaUri ->
                val artworkPath = pathProvider.getPath(mediaUri)
                setArtworkUri(Uri.fromFile(File(artworkPath.toString())))
            }
        }
        .build()
}
