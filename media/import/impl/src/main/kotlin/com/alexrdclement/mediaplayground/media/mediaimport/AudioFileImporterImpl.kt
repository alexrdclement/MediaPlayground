package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeMediaAsset
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toMediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.media.store.FileWriter
import com.alexrdclement.mediaplayground.media.store.MediaAssetStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.media.store.PathProvider
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import com.alexrdclement.mediaplayground.model.result.mapFailure
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

@Inject
class AudioFileImporterImpl(
    private val mediaAssetStore: MediaAssetStore,
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val artistImporter: ArtistImporterImpl,
    private val albumImporter: AlbumImporterImpl,
    private val pathProvider: PathProvider,
    private val fileWriter: FileWriter,
    private val imageImporter: ImageImporterImpl,
    private val transactionRunner: MediaStoreTransactionRunner,
) : AudioFileImporter {

    override suspend fun import(
        uri: Uri,
        destinationDir: Path,
        source: Source,
    ): Result<MediaAsset, MediaImportError> = withContext(Dispatchers.IO) {
        val metadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri) as? MediaMetadata.Audio
            ?: return@withContext Result.Failure(MediaImportError.InputFileError)

        val (filePath, simpleAlbum) = copyFiles(
            uri = uri,
            mediaMetadata = metadata,
            source = source,
        ).guardSuccess { return@withContext Result.Failure(it) }

        transactionRunner.run {
            importTransaction(
                filePath = filePath,
                mediaMetadata = metadata,
                source = source,
                simpleAlbum = simpleAlbum,
            )
        }
    }

    override suspend fun import(
        uris: List<Uri>,
        destinationDir: Path,
        source: Source,
    ): Map<Uri, Result<MediaAsset, MediaImportError>> =
        uris.associateWith {
            import(
                uri = it,
                destinationDir = destinationDir,
                source = source,
            )
        }


    internal suspend fun copyFiles(
        uri: Uri,
        mediaMetadata: MediaMetadata.Audio,
        source: Source,
    ): Result<Pair<Path, SimpleAlbum>, MediaImportError> = withContext(Dispatchers.IO) {
        val simpleAlbum = transactionRunner.run {
            val artist = artistImporter.importTransaction(
                metadata = mediaMetadata,
            ).guardSuccess { return@run Result.Failure(it) }

            albumImporter.importSimpleAlbum(
                metadata = mediaMetadata,
                source = source,
                artist = artist,
            )
        }.guardSuccess { return@withContext Result.Failure(it) }

        val path = copyFiles(
            uri = uri,
            albumId = simpleAlbum.id,
            mediaMetadata = mediaMetadata,
        ).guardSuccess { return@withContext Result.Failure(it) }

        Result.Success(path to simpleAlbum)
    }

    internal suspend fun copyFiles(
        uri: Uri,
        albumId: AlbumId,
        mediaMetadata: MediaMetadata.Audio,
    ): Result<Path, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val embeddedPicture = mediaMetadata.embeddedPicture
            if (embeddedPicture != null) {
                imageImporter.copyBitmap(byteArray = embeddedPicture)
            }

            val destinationDir = pathProvider.getAlbumDir(albumId = albumId)
            try {
                SystemFileSystem.createDirectories(destinationDir)
            } catch (e: IOException) {
                return@withContext Result.Failure(MediaImportError.MkdirError)
            }
            fileWriter.writeToDisk(
                contentUri = uri,
                destinationDir = destinationDir,
            ).mapFailure { it.toMediaImportError() }
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importTransaction(
        filePath: Path,
        mediaMetadata: MediaMetadata.Audio,
        source: Source,
        simpleAlbum: SimpleAlbum,
    ): Result<MediaAsset, MediaImportError> {
        return importTransaction(
            filePath = filePath,
            mediaMetadata = mediaMetadata,
            source = source,
            artists = simpleAlbum.artists,
            images = simpleAlbum.images,
        )
    }

    context(context: MediaStoreTransactionScope)
    internal suspend fun importTransaction(
        filePath: Path,
        mediaMetadata: MediaMetadata.Audio,
        source: Source,
        artists: PersistentList<Artist>,
        images: PersistentList<Image>,
    ): Result<MediaAsset, MediaImportError> {
        val existing = mediaAssetStore.getByFileName(filePath.name)
        if (existing != null) {
            return Result.Success(existing.copy(uri = filePath.toString()))
        }
        val mediaAsset = makeMediaAsset(
            uri = filePath.toString(),
            mediaMetadata = mediaMetadata,
            source = source,
            artists = artists,
            images = images,
        )
        imageImporter.importImagesTransaction(images.toSet())
        mediaAssetStore.put(mediaAsset)
        return Result.Success(mediaAsset)
    }
}
