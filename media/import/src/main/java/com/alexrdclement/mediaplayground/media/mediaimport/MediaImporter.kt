package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toTrack
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportFailure
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.result.Result
import javax.inject.Inject

@OptIn(UnstableApi::class)
class MediaImporter @Inject constructor(
    private val mediaMetadataResolver: MediaMetadataResolver,
) {
    fun importTrackFromDisk(uri: Uri): Result<Track, MediaImportFailure> {
        return try {
            val mediaItem = MediaItem.fromUri(uri)
            val mediaMetadata = mediaMetadataResolver.getMediaMetadata(contentUri = uri)
            val track = mediaItem.toTrack(mediaMetadata = mediaMetadata)
            Result.Success(track)
        } catch (e: Throwable) {
            Result.Failure(MediaImportFailure.Unknown(throwable = e))
        }
    }
}
