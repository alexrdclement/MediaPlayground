package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.util.runTracked
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
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
    private val audioFileImporter: Lazy<AudioAssetImporterImpl>,
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
        val audioCopyResult = audioFileImporter.value.copyFiles(
            uri = uri,
            mediaMetadata = mediaMetadata,
        ).guardSuccess { return@withContext Result.Failure(it) }

        val audioAsset = syncStateStore.runTracked(audioCopyResult.id, transactionRunner) {
            audioFileImporter.value.importTransaction(
                id = audioCopyResult.id,
                filePath = audioCopyResult.path,
                originUri = MediaAssetOriginUri.AndroidContentUri(uri.toString()),
                mediaMetadata = mediaMetadata,
                simpleAlbum = audioCopyResult.simpleAlbum,
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
        val (destinationPath, imageId) = imageImporter.value.copyFile(
            uri = uri,
            mediaMetadata = mediaMetadata,
        ).guardSuccess { return@withContext Result.Failure(it) }

        val image = syncStateStore.runTracked(imageId, transactionRunner) {
            imageImporter.value.importImageTransaction(
                imageId = imageId,
                originUri = MediaAssetOriginUri.AndroidContentUri(uri.toString()),
                destinationPath = destinationPath,
                mediaMetadata = mediaMetadata,
            )
        }.guardSuccess { return@withContext Result.Failure(it) }

        Result.Success(
            MediaAssetImportResult.Image(
                path = destinationPath,
                image = image,
            ),
        )
    }
}
