package com.alexrdclement.media.metadata

import android.net.Uri
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import kotlinx.io.files.Path

class FakeMediaMetadataRetriever : MediaMetadataRetriever {

    var mediaMetadata: MediaMetadata = MediaMetadata.Audio(
        title = null,
        durationUs = null,
        sampleRate = 44100,
        channelCount = null,
        bitRate = null,
        bitDepth = null,
        trackNumber = null,
        artistName = null,
        albumTitle = null,
        embeddedPicture = null,
        mimeType = null,
        extension = "mp3",
    )

    var fileMetadata: MediaMetadata? = null

    override suspend fun getMediaMetadata(filePath: Path): MediaMetadata = fileMetadata ?: mediaMetadata

    override suspend fun getMediaMetadata(contentUri: Uri): MediaMetadata = mediaMetadata
}
