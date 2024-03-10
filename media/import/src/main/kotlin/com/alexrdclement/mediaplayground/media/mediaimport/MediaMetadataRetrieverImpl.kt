package com.alexrdclement.mediaplayground.media.mediaimport

import android.content.Context
import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.io.files.Path
import javax.inject.Inject
import android.media.MediaMetadataRetriever as AndroidMediaMetadataRetriever

class MediaMetadataRetrieverImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : MediaMetadataRetriever {

    override suspend fun getMediaMetadata(
        contentUri: Uri,
        onEmbeddedPictureFound: suspend (ByteArray) -> Path?,
    ): MediaMetadata {
        return AndroidMediaMetadataRetriever().use { retriever ->
            retriever.setDataSource(context, contentUri)

            MediaMetadata(
                title = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_TITLE),
                durationMs = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_DURATION)
                    ?.toLongOrNull(),
                trackNumber = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
                    ?.toIntOrNull(),
                artistName = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_ARTIST),
                albumTitle = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_ALBUM),
                imagePath = retriever.embeddedPicture?.let { onEmbeddedPictureFound(it) },
            )
        }
    }
}
