package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata

interface MediaMetadataRetriever {
    suspend fun getMediaMetadata(contentUri: Uri): MediaMetadata
}
