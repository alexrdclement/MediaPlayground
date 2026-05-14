package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeClip
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioClip
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
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
    private val audioAssetImporter: Lazy<AudioAssetImporter>,
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val transactionRunner: MediaStoreTransactionRunner,
) : ClipImporter {

    override suspend fun import(
        uri: Uri,
    ): Result<AudioClip, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val metadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri) as? MediaMetadata.Audio
                ?: return@withContext Result.Failure(MediaImportError.InputFileError)

            val assetImportResult = audioAssetImporter.value.import(
                uri = uri,
                metadata = metadata,
            ).guardSuccess { return@withContext Result.Failure(it) }

            transactionRunner.run {
                importTransaction(
                    filePath = assetImportResult.filePath,
                    metadata = metadata,
                    audioFile = assetImportResult.audioAsset,
                )
            }
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    override suspend fun import(
        uris: List<Uri>,
    ): Map<Uri, Result<AudioClip, MediaImportError>> =
        uris.associateWith { import(it) }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importTransaction(
        filePath: Path,
        metadata: MediaMetadata.Audio,
        audioFile: AudioAsset,
    ): Result<AudioClip, MediaImportError> {
        val clip = makeClip(
            filePath = filePath,
            mediaMetadata = metadata,
            audioFile = audioFile,
        )
        clipDataStore.put(clip)
        return Result.Success(clip)
    }
}
