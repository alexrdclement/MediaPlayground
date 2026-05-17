package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.map
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

@Inject
class MediaAssetImporterImpl(
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val audioAssetImporter: Lazy<AudioAssetImporter>,
    private val imageImporter: Lazy<ImageImporter>,
) : MediaAssetImporter {
    override suspend fun import(
        uri: Uri,
    ): Result<MediaAssetImportResult, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            when (val metadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri)) {
                is MediaMetadata.Audio -> audioAssetImporter.value.import(uri, metadata).map { it }
                is MediaMetadata.Image -> imageImporter.value.import(uri, metadata).map { it }
            }
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }
}
