package com.alexrdclement.mediaplayground.media.engine.mapper

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.largeImageUri
import com.alexrdclement.mediaplayground.media.store.PathProvider
import java.io.File

fun Track.toMediaItem(pathProvider: PathProvider): MediaItem = when (this) {
    is AudioTrack -> toAudioMediaItem(pathProvider)
}

private fun AudioTrack.toAudioMediaItem(pathProvider: PathProvider): MediaItem {
    val trackClip = clips.firstOrNull() ?: return MediaItem.Builder().build()
    val clip = trackClip.clip
    val path = pathProvider.getPath(clip.mediaAsset.uri)
    return MediaItem.Builder()
        .setMediaId(id.value)
        .setUri(Uri.fromFile(File(path.toString())))
        .setMediaMetadata(toAudioMediaMetadata(pathProvider))
        .build()
}

private fun AudioTrack.toAudioMediaMetadata(pathProvider: PathProvider): MediaMetadata {
    val primaryAlbum = albums.firstOrNull()
    return MediaMetadata.Builder()
        .setAlbumArtist(primaryAlbum?.artists?.joinToString { it.name ?: "Unknown artist" })
        .setArtist(artists.joinToString { it.name ?: "Unknown artist" })
        .setAlbumTitle(primaryAlbum?.name)
        .setDisplayTitle(title)
        .setTitle(title)
        .apply {
            primaryAlbum?.largeImageUri?.let { mediaUri ->
                val artworkPath = pathProvider.getPath(mediaUri)
                setArtworkUri(Uri.fromFile(File(artworkPath.toString())))
            }
        }
        .build()
}
