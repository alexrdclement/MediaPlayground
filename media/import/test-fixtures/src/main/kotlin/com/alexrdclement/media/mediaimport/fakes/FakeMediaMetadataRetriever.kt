package com.alexrdclement.media.mediaimport.fakes

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata

class FakeMediaMetadataRetriever : MediaMetadataRetriever {

    var mediaMetadata: MediaMetadata = MediaMetadata.Audio(
        title = null,
        durationMs = null,
        trackNumber = null,
        artistName = null,
        albumTitle = null,
        embeddedPicture = null,
        mimeType = null,
        extension = "mp3",
    )

    override suspend fun getMediaMetadata(contentUri: Uri): MediaMetadata {
        return mediaMetadata
    }
}
