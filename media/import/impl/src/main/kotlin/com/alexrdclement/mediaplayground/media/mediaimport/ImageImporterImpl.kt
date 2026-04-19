package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeImage
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toMediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.util.sha256
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.mediaimport.util.runTracked
import com.alexrdclement.mediaplayground.media.store.FileReader
import com.alexrdclement.mediaplayground.media.store.FileWriter
import com.alexrdclement.mediaplayground.media.store.ImageMediaStore
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
import kotlinx.io.IOException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

@Inject
class ImageImporterImpl(
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val pathProvider: PathProvider,
    private val imageMediaStore: ImageMediaStore,
    private val syncStateStore: MediaAssetSyncStateStore,
    private val transactionRunner: MediaStoreTransactionRunner,
    private val fileReader: FileReader,
    private val fileWriter: FileWriter,
) : ImageImporter {

    override suspend fun import(uri: Uri): Result<Image, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val metadata = mediaMetadataRetriever.getMediaMetadata(uri) as? MediaMetadata.Image
                ?: return@withContext Result.Failure(MediaImportError.InputFileError)

            val (destinationPath, imageId) = copyFile(
                uri = uri,
                mediaMetadata = metadata,
            ).guardSuccess { return@withContext Result.Failure(it) }

            syncStateStore.runTracked(imageId, transactionRunner) {
                importImageTransaction(
                    imageId = imageId,
                    destinationPath = destinationPath,
                    originUri = MediaAssetOriginUri.AndroidContentUri(uri.toString()),
                    mediaMetadata = metadata,
                )
            }
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    override suspend fun import(uris: List<Uri>): Map<Uri, Result<Image, MediaImportError>> =
        uris.associateWith { import(it) }

    internal suspend fun import(
        byteArray: ByteArray,
    ): Result<Image, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val (destinationPath, imageId) = copyBitmap(
                byteArray = byteArray,
            ).guardSuccess { return@withContext Result.Failure(it) }

            val metadata = mediaMetadataRetriever.getMediaMetadata(filePath = destinationPath) as? MediaMetadata.Image
                ?: return@withContext Result.Failure(MediaImportError.InputFileError)

            transactionRunner.run {
                importImageTransaction(
                    imageId = imageId,
                    destinationPath = destinationPath,
                    originUri = MediaAssetOriginUri.AndroidContentUri(""),
                    mediaMetadata = metadata,
                )
            }
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    internal suspend fun copyFile(
        uri: Uri,
        mediaMetadata: MediaMetadata.Image,
    ): Result<Pair<Path, ImageId>, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val bytes = fileReader.readBytes(uri)
                .guardSuccess { return@withContext Result.Failure(it.toMediaImportError()) }
            val imageId = ImageId(bytes.sha256())
            val destination = pathProvider.getImagePath(imageId, mediaMetadata.extension)
            if (SystemFileSystem.exists(destination)) {
                return@withContext Result.Success(destination to imageId)
            }
            try {
                destination.parent?.let { SystemFileSystem.createDirectories(it) }
            } catch (e: IOException) {
                return@withContext Result.Failure(MediaImportError.MkdirError)
            }
            val path = fileWriter.writeFileToDisk(
                contentUri = uri,
                destination = destination,
            ).guardSuccess { return@withContext Result.Failure(it.toMediaImportError()) }

            Result.Success(path to imageId)
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    internal suspend fun copyBitmap(
        byteArray: ByteArray,
        imageId: ImageId = ImageId(byteArray.sha256()),
    ): Result<Pair<Path, ImageId>, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val destination = pathProvider.getImagePath(
                imageId = imageId,
                extension = "png",
            )
            if (SystemFileSystem.exists(destination)) {
                return@withContext Result.Success(destination to imageId)
            }
            try {
                destination.parent?.let { SystemFileSystem.createDirectories(it) }
            } catch (e: IOException) {
                return@withContext Result.Failure(MediaImportError.MkdirError)
            }
            val path = fileWriter.writeBitmapToDisk(
                byteArray = byteArray,
                destination = destination,
            ).guardSuccess { return@withContext Result.Failure(it.toMediaImportError()) }

            Result.Success(path to imageId)
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    context(context: MediaStoreTransactionScope)
    internal suspend fun importImageTransaction(
        imageId: ImageId,
        destinationPath: Path,
        originUri: MediaAssetOriginUri,
        mediaMetadata: MediaMetadata.Image,
    ): Result<Image, MediaImportError> {
        val assetUri = MediaAssetUri.Shared(destinationPath.name)
        val image = makeImage(
            id = imageId,
            uri = assetUri,
            originUri = originUri,
            mediaMetadata = mediaMetadata,
        )
        imageMediaStore.put(setOf(image))
        return Result.Success(image)
    }

    context(context: MediaStoreTransactionScope)
    internal suspend fun importImagesTransaction(
        image: Set<Image>,
    ) = imageMediaStore.put(image)
}
