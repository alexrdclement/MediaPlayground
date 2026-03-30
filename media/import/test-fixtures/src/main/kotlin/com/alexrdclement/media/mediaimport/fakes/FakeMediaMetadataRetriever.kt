package com.alexrdclement.media.mediaimport.fakes

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata

class FakeMediaMetadataRetriever : MediaMetadataRetriever {

    var mediaMetadata = MediaMetadata(
        title = null,
        durationMs = null,
        trackNumber = null,
        artistName = null,
        albumTitle = null,
        embeddedPicture = null,
        mimeType = null,
        extension = "jpg",
    )

    override suspend fun getMediaMetadata(contentUri: Uri): MediaMetadata {
        return mediaMetadata
    }
}
