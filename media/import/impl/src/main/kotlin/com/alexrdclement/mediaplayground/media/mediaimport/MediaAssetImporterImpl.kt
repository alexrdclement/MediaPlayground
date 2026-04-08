package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.mapFailure
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.fold
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import com.alexrdclement.mediaplayground.model.result.map
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

@Inject
class MediaAssetImporterImpl(
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val transactionRunner: MediaStoreTransactionRunner,
    private val albumImporter: Lazy<AlbumImporterImpl>,
    private val artistImporter: Lazy<ArtistImporterImpl>,
    private val audioFileImporter: Lazy<AudioFileImporterImpl>,
    private val imageImporter: Lazy<ImageImporterImpl>,
): MediaAssetImporter {
    override suspend fun import(
        uri: Uri,
        source: Source,
    ): Result<MediaAssetImportResult, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            when (val metadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri)) {
                is MediaMetadata.Audio -> importAudio(
                    uri = uri,
                    mediaMetadata = metadata,
                    source = source,
                ).map { it }
                is MediaMetadata.Image -> importImage(
                    uri = uri,
                    mediaMetadata = metadata,
                    source = source,
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
        source: Source,
    ): Result<MediaAssetImportResult.Audio, MediaImportError> = withContext(Dispatchers.IO) {
        val image = mediaMetadata.embeddedPicture?.let {
            imageImporter.value.import(byteArray = it)
                .fold(onSuccess = { it }, onFailure = { null })
        }

        val simpleAlbum = transactionRunner.run {
            importSimpleAlbumAndArtist(
                metadata = mediaMetadata,
                source = source,
                images = image?.let { persistentSetOf(image) } ?: persistentSetOf(),
            )
        }.guardSuccess { return@withContext Result.Failure(it) }

        val path = audioFileImporter.value.copyFiles(
            uri = uri,
            albumId = simpleAlbum.id,
            mediaMetadata = mediaMetadata,
        ).guardSuccess { return@withContext Result.Failure(it) }

        val mediaAsset = transactionRunner.run {
            audioFileImporter.value.importTransaction(
                filePath = path,
                mediaMetadata = mediaMetadata,
                source = source,
                simpleAlbum = simpleAlbum,
            )
        }.guardSuccess { return@withContext Result.Failure(it) }

        val result = MediaAssetImportResult.Audio(
            filePath = path,
            mediaAsset = mediaAsset,
            simpleAlbum = simpleAlbum,
        )
        Result.Success(result)
    }

    internal suspend fun importImage(
        uri: Uri,
        mediaMetadata: MediaMetadata.Image,
        source: Source,
    ): Result<MediaAssetImportResult.Image, MediaImportError> = withContext(Dispatchers.IO) {
        val (filePath, imageId) = imageImporter.value.copyFile(
            uri = uri,
            mediaMetadata = mediaMetadata,
        ).guardSuccess { return@withContext Result.Failure(it) }

        val image = transactionRunner.run {
            imageImporter.value.importImageTransaction(
                filePath = filePath,
                imageId = imageId,
                mediaMetadata = mediaMetadata,
            )
        }.guardSuccess { return@withContext Result.Failure(it) }

        val result = MediaAssetImportResult.Image(
            path = filePath,
            image = image,
        )
        Result.Success(result)
    }

    context(scope: MediaStoreTransactionScope)
    private suspend fun importSimpleAlbumAndArtist(
        metadata: MediaMetadata.Audio,
        source: Source,
        images: PersistentSet<Image>,
    ): Result<SimpleAlbum, MediaImportError> {
        val artist = when (val result = artistImporter.value.importTransaction(metadata)) {
            is Result.Failure -> return result.mapFailure()
            is Result.Success -> result.value
        }
        return albumImporter.value.importSimpleAlbum(
            metadata = metadata,
            source = source,
            artist = artist,
            images = images,
        )
    }
}
