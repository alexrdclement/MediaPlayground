package com.alexrdclement.mediaplayground.media.mediaimport

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.net.toUri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class MediaMetadataResolver @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend fun getMediaMetadata(
        contentUri: Uri,
        onEmbeddedPictureFound: suspend (ByteArray) -> File?,
    ): MediaMetadata {
        return MediaMetadataRetriever().use { retriever ->
            retriever.setDataSource(context, contentUri)

            MediaMetadata(
                title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
                durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    ?.toLongOrNull(),
                trackNumber = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
                    ?.toIntOrNull(),
                artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),
                albumTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                imageUri = retriever.embeddedPicture?.let { onEmbeddedPictureFound(it) }?.toUri(),
            )
        }
    }
}
