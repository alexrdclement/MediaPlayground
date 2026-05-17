package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeImage
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toMediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.util.runTrackedImport
import com.alexrdclement.mediaplayground.media.mediaimport.util.sha256
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.store.ContentUriReader
import com.alexrdclement.mediaplayground.media.store.FileWriter
import com.alexrdclement.mediaplayground.media.store.ImageMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaAssetStore
import com.alexrdclement.mediaplayground.media.store.MediaAssetSyncStateStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.media.store.PathProvider
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

@Inject
class ImageImporterImpl(
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val pathProvider: PathProvider,
    private val imageMediaStore: ImageMediaStore,
    private val mediaAssetStore: MediaAssetStore,
    private val syncStateStore: MediaAssetSyncStateStore,
    private val transactionRunner: MediaStoreTransactionRunner,
    private val contentUriReader: ContentUriReader,
    private val fileWriter: FileWriter,
) : ImageImporter {

    override suspend fun import(
        uri: Uri,
        metadata: MediaMetadata.Image,
    ): Result<MediaAssetImportResult.Image, MediaImportError> = withContext(Dispatchers.IO) {
        val copyResult = copyFile(
            uri = uri,
            mediaMetadata = metadata,
        ).guardSuccess { return@withContext Result.Failure(it) }

        val image = syncStateStore.runTrackedImport(
            asset = copyResult.image,
            mediaAssetStore = mediaAssetStore,
            transactionRunner = transactionRunner,
        ) {
            importImageTransaction(image = copyResult.image)
        }.guardSuccess { return@withContext Result.Failure(it) }

        Result.Success(
            MediaAssetImportResult.Image(
                path = copyResult.path,
                image = image,
            ),
        )
    }

    internal suspend fun copyFile(
        uri: Uri,
        mediaMetadata: MediaMetadata.Image,
    ): Result<ImageCopyResult, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val bytes = contentUriReader.readBytes(uri)
                .guardSuccess { return@withContext Result.Failure(it.toMediaImportError()) }
            val imageId = ImageId(bytes.sha256())
            val mediaUri = MediaAssetUri.Shared("${imageId.value}.${mediaMetadata.extension}")
            val destination = pathProvider.getPath(mediaUri)
            val image = makeImage(
                id = imageId,
                uri = mediaUri,
                originUri = MediaAssetOriginUri.AndroidContentUri(uri.toString()),
                mediaMetadata = mediaMetadata,
            )

            if (SystemFileSystem.exists(destination)) {
                return@withContext Result.Success(
                    ImageCopyResult(
                        path = destination,
                        id = imageId,
                        image = image,
                    ),
                )
            }
            val path = fileWriter.write(
                byteArray = bytes,
                destination = destination,
            ).guardSuccess { return@withContext Result.Failure(it.toMediaImportError()) }

            Result.Success(
                ImageCopyResult(
                    path = path,
                    id = imageId,
                    image = image,
                ),
            )
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    internal suspend fun copyBitmap(
        byteArray: ByteArray,
        uri: MediaAssetUri,
        imageId: ImageId = ImageId(byteArray.sha256()),
    ): Result<Image, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val destination = pathProvider.getPath(uri)

            if (!SystemFileSystem.exists(destination)) {
                fileWriter.write(
                    byteArray = byteArray,
                    destination = destination,
                ).guardSuccess { return@withContext Result.Failure(it.toMediaImportError()) }
            }

            val metadata = mediaMetadataRetriever.getMediaMetadata(filePath = destination) as? MediaMetadata.Image
                ?: return@withContext Result.Failure(MediaImportError.InputFileError)

            Result.Success(
                makeImage(
                    id = imageId,
                    uri = uri,
                    originUri = MediaAssetOriginUri.AndroidContentUri(""),
                    mediaMetadata = metadata,
                )
            )
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    context(context: MediaStoreTransactionScope)
    internal suspend fun importImageTransaction(
        image: Image,
    ): Result<Image, MediaImportError> {
        imageMediaStore.put(setOf(image))
        return Result.Success(image)
    }

    context(context: MediaStoreTransactionScope)
    internal suspend fun importImagesTransaction(
        image: Set<Image>,
    ) = imageMediaStore.put(image)

}

internal data class ImageCopyResult(
    val path: Path,
    val id: ImageId,
    val image: Image,
)
