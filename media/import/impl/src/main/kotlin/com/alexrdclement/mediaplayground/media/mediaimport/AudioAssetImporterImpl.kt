package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeAudioAsset
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toMediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.util.sha256
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.util.runTracked
import com.alexrdclement.mediaplayground.media.store.AudioAssetStore
import com.alexrdclement.mediaplayground.media.store.FileReader
import com.alexrdclement.mediaplayground.media.store.FileWriter
import com.alexrdclement.mediaplayground.media.store.MediaAssetSyncStateStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.media.store.PathProvider
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.time.Clock

@Inject
class AudioAssetImporterImpl(
    private val audioAssetStore: AudioAssetStore,
    private val syncStateStore: MediaAssetSyncStateStore,
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val artistImporter: ArtistImporterImpl,
    private val albumImporter: AlbumImporterImpl,
    private val pathProvider: PathProvider,
    private val fileWriter: FileWriter,
    private val imageImporter: ImageImporterImpl,
    private val transactionRunner: MediaStoreTransactionRunner,
    private val fileReader: FileReader,
) : AudioAssetImporter {

    internal data class AudioCopyResult(
        val path: Path,
        val id: AudioAssetId,
        val simpleAlbum: SimpleAlbum,
    )

    override suspend fun import(
        uri: Uri,
    ): Result<AudioAsset, MediaImportError> = withContext(Dispatchers.IO) {
        val metadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri) as? MediaMetadata.Audio
            ?: return@withContext Result.Failure(MediaImportError.InputFileError)

        val audioCopyResult = copyFiles(
            uri = uri,
            mediaMetadata = metadata,
        ).guardSuccess { return@withContext Result.Failure(it) }

        syncStateStore.runTracked(id = audioCopyResult.id, transactionRunner = transactionRunner) {
            importTransaction(
                id = audioCopyResult.id,
                filePath = audioCopyResult.path,
                mediaMetadata = metadata,
                simpleAlbum = audioCopyResult.simpleAlbum,
                originUri = MediaAssetOriginUri.AndroidContentUri(uri.toString()),
            )
        }
    }

    override suspend fun import(
        uris: List<Uri>,
    ): Map<Uri, Result<AudioAsset, MediaImportError>> =
        uris.associateWith { import(uri = it) }

    internal suspend fun copyFiles(
        uri: Uri,
        mediaMetadata: MediaMetadata.Audio,
    ): Result<AudioCopyResult, MediaImportError> = withContext(Dispatchers.IO) {
        val simpleAlbum = transactionRunner.run {
            val artist = artistImporter.importTransaction(
                metadata = mediaMetadata,
            ).guardSuccess { return@run Result.Failure(it) }

            albumImporter.importSimpleAlbum(
                metadata = mediaMetadata,
                artist = artist,
            )
        }.guardSuccess { return@withContext Result.Failure(it) }

        val embeddedPicture = mediaMetadata.embeddedPicture
        if (embeddedPicture != null) {
            imageImporter.copyBitmap(byteArray = embeddedPicture)
        }

        val (path, id) = copyFile(
            uri = uri,
            mediaMetadata = mediaMetadata,
        ).guardSuccess { return@withContext Result.Failure(it) }

        Result.Success(AudioCopyResult(path = path, id = id, simpleAlbum = simpleAlbum))
    }

    private suspend fun copyFile(
        uri: Uri,
        mediaMetadata: MediaMetadata.Audio,
    ): Result<Pair<Path, AudioAssetId>, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val bytes = fileReader.readBytes(uri)
                .guardSuccess { return@withContext Result.Failure(it.toMediaImportError()) }
            val id = AudioAssetId(bytes.sha256())
            val destination = pathProvider.getAudioFilePath(id.value, mediaMetadata.extension)
            if (SystemFileSystem.exists(destination)) {
                return@withContext Result.Success(destination to id)
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

            Result.Success(path to id)
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importTransaction(
        id: AudioAssetId,
        filePath: Path,
        originUri: MediaAssetOriginUri,
        mediaMetadata: MediaMetadata.Audio,
        simpleAlbum: SimpleAlbum,
    ): Result<AudioAsset, MediaImportError> {
        return importTransaction(
            id = id,
            filePath = filePath,
            originUri = originUri,
            mediaMetadata = mediaMetadata,
            artists = simpleAlbum.artists,
            images = simpleAlbum.images,
            simpleAlbum = simpleAlbum,
        )
    }

    context(context: MediaStoreTransactionScope)
    internal suspend fun importTransaction(
        id: AudioAssetId,
        filePath: Path,
        originUri: MediaAssetOriginUri,
        mediaMetadata: MediaMetadata.Audio,
        artists: PersistentList<Artist>,
        images: PersistentList<Image>,
        simpleAlbum: SimpleAlbum,
    ): Result<AudioAsset, MediaImportError> {
        val existing = audioAssetStore.getByFileName(filePath.name)
        if (existing != null) {
            return Result.Success(existing)
        }
        val assetUri = MediaAssetUri.Album(albumId = simpleAlbum.id, fileName = filePath.name)
        val audioAsset = makeAudioAsset(
            id = id,
            uri = assetUri,
            originUri = originUri,
            mediaMetadata = mediaMetadata,
            artists = artists,
            images = images,
            createdAt = Clock.System.now(),
        )
        imageImporter.importImagesTransaction(images.toSet())
        audioAssetStore.put(audioAsset)
        return Result.Success(audioAsset)
    }
}
