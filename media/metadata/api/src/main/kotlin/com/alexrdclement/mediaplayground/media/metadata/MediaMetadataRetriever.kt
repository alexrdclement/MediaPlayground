package com.alexrdclement.mediaplayground.media.metadata

import android.net.Uri
import com.alexrdclement.mediaplayground.media.metadata.model.MediaMetadata

interface MediaMetadataRetriever {
    suspend fun getMediaMetadata(contentUri: Uri): MediaMetadata
}
