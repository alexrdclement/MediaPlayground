package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeClip
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.media.store.ClipMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path

@Inject
class ClipImporterImpl(
    private val clipDataStore: ClipMediaStore,
    private val mediaAssetImporter: Lazy<MediaAssetImporterImpl>,
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val transactionRunner: MediaStoreTransactionRunner,
) : ClipImporter {

    override suspend fun import(
        uri: Uri,
        source: Source,
    ): Result<Clip, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val metadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri) as? MediaMetadata.Audio
                ?: return@withContext Result.Failure(MediaImportError.InputFileError)

            val assetImportResult = mediaAssetImporter.value.importAudio(
                uri = uri,
                mediaMetadata = metadata,
                source = source,
            ).guardSuccess { return@withContext Result.Failure(it) }

            transactionRunner.run {
                importTransaction(
                    filePath = assetImportResult.filePath,
                    metadata = metadata,
                    mediaAsset = assetImportResult.mediaAsset,
                )
            }
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    override suspend fun import(
        uris: List<Uri>,
        source: Source,
    ): Map<Uri, Result<Clip, MediaImportError>> =
        uris.associateWith { import(it, source = source) }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importTransaction(
        filePath: Path,
        metadata: MediaMetadata.Audio,
        mediaAsset: MediaAsset,
    ): Result<Clip, MediaImportError> {
        val clip = makeClip(
            filePath = filePath,
            mediaMetadata = metadata,
            mediaAsset = mediaAsset,
        )
        clipDataStore.put(clip)
        return Result.Success(clip)
    }
}
