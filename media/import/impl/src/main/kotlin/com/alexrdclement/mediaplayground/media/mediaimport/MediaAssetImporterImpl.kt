package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.util.runTracked
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.store.MediaAssetSyncStateStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import com.alexrdclement.mediaplayground.model.result.map
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

@Inject
class MediaAssetImporterImpl(
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val syncStateStore: MediaAssetSyncStateStore,
    private val transactionRunner: MediaStoreTransactionRunner,
    private val audioAssetImporter: Lazy<AudioAssetImporterImpl>,
    private val imageImporter: Lazy<ImageImporterImpl>,
): MediaAssetImporter {
    override suspend fun import(
        uri: Uri,
    ): Result<MediaAssetImportResult, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            when (val metadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri)) {
                is MediaMetadata.Audio -> importAudio(
                    uri = uri,
                    mediaMetadata = metadata,
                ).map { it }
                is MediaMetadata.Image -> importImage(
                    uri = uri,
                    mediaMetadata = metadata,
                ).map { it }
            }
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    internal suspend fun importAudio(
        uri: Uri,
        mediaMetadata: MediaMetadata.Audio,
    ): Result<MediaAssetImportResult.Audio, MediaImportError> = withContext(Dispatchers.IO) {
        val audioCopyResult = audioAssetImporter.value.copyFiles(
            uri = uri,
            mediaMetadata = mediaMetadata,
        ).guardSuccess { return@withContext Result.Failure(it) }

        val audioAsset = syncStateStore.runTracked(audioCopyResult.id, transactionRunner) {
            audioAssetImporter.value.importTransaction(
                audioAsset = audioCopyResult.audioAsset,
                filePath = audioCopyResult.path,
            )
        }.guardSuccess { return@withContext Result.Failure(it) }

        Result.Success(
            MediaAssetImportResult.Audio(
                filePath = audioCopyResult.path,
                audioAsset = audioAsset,
                simpleAlbum = audioCopyResult.simpleAlbum,
            ),
        )
    }

    internal suspend fun importImage(
        uri: Uri,
        mediaMetadata: MediaMetadata.Image,
    ): Result<MediaAssetImportResult.Image, MediaImportError> = withContext(Dispatchers.IO) {
        val imageCopyResult = imageImporter.value.copyFile(
            uri = uri,
            mediaMetadata = mediaMetadata,
        ).guardSuccess { return@withContext Result.Failure(it) }

        val image = syncStateStore.runTracked(imageCopyResult.id, transactionRunner) {
            imageImporter.value.importImageTransaction(image = imageCopyResult.image)
        }.guardSuccess { return@withContext Result.Failure(it) }

        Result.Success(
            MediaAssetImportResult.Image(
                path = imageCopyResult.path,
                image = image,
            ),
        )
    }
}
