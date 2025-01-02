package com.alexrdclement.media.mediaimport.fakes

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import javax.inject.Inject

class FakeMediaMetadataRetriever @Inject constructor() : MediaMetadataRetriever {

    var mediaMetadata = MediaMetadata(
        title = null,
        durationMs = null,
        trackNumber = null,
        artistName = null,
        albumTitle = null,
        embeddedPicture = null,
    )

    override suspend fun getMediaMetadata(contentUri: Uri): MediaMetadata {
        return mediaMetadata
    }
}
