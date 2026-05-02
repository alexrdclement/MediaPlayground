package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeAudioAsset
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toMediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.util.runTracked
import com.alexrdclement.mediaplayground.media.mediaimport.util.sha256
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.store.AlbumMediaStore
import com.alexrdclement.mediaplayground.media.store.AudioAssetStore
import com.alexrdclement.mediaplayground.media.store.FileReader
import com.alexrdclement.mediaplayground.media.store.FileWriter
import com.alexrdclement.mediaplayground.media.store.MediaAssetStore
import com.alexrdclement.mediaplayground.media.store.MediaAssetSyncStateStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.media.store.PathProvider
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import com.alexrdclement.mediaplayground.model.result.successOrNull
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

@Inject
class AudioAssetImporterImpl(
    private val audioAssetStore: AudioAssetStore,
    private val mediaAssetStore: MediaAssetStore,
    private val syncStateStore: MediaAssetSyncStateStore,
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val artistImporter: ArtistImporterImpl,
    private val albumImporter: AlbumImporterImpl,
    private val albumMediaStore: AlbumMediaStore,
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
        val audioAsset: AudioAsset,
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
                audioAsset = audioCopyResult.audioAsset,
                filePath = audioCopyResult.path,
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

        val embeddedImage = mediaMetadata.embeddedPicture?.let { embeddedPicture ->
            val imageId = ImageId(embeddedPicture.sha256())
            imageImporter.copyBitmap(
                byteArray = embeddedPicture,
                uri = MediaAssetUri.Album(albumId = simpleAlbum.id, fileName = "${imageId.value}.png"),
                imageId = imageId,
            ).successOrNull()
        }

        try {
            val bytes = fileReader.readBytes(uri)
                .guardSuccess { return@withContext Result.Failure(it.toMediaImportError()) }
            val id = AudioAssetId(bytes.sha256())
            val assetUri = MediaAssetUri.Album(
                albumId = simpleAlbum.id,
                fileName = "${id.value}.${mediaMetadata.extension}",
            )
            val destination = pathProvider.getPath(assetUri)
            val images = embeddedImage?.let { simpleAlbum.images.plus(it).toPersistentList() } ?: simpleAlbum.images
            val audioAsset = makeAudioAsset(
                id = id,
                uri = assetUri,
                originUri = MediaAssetOriginUri.AndroidContentUri(uri.toString()),
                mediaMetadata = mediaMetadata,
                artists = simpleAlbum.artists,
                images = images,
            )
            mediaAssetStore.put(audioAsset)

            if (SystemFileSystem.exists(destination)) {
                return@withContext Result.Success(
                    AudioCopyResult(
                        path = destination,
                        id = id,
                        simpleAlbum = simpleAlbum,
                        audioAsset = audioAsset,
                    ),
                )
            }

            val path = fileWriter.writeFileToDisk(
                contentUri = uri,
                destination = destination,
            ).guardSuccess { return@withContext Result.Failure(it.toMediaImportError()) }

            Result.Success(
                AudioCopyResult(
                    path = path,
                    id = id,
                    simpleAlbum = simpleAlbum,
                    audioAsset = audioAsset,
                ),
            )
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importTransaction(
        audioAsset: AudioAsset,
        filePath: Path,
    ): Result<AudioAsset, MediaImportError> {
        val existing = audioAssetStore.getByFileName(filePath.name)

        if (existing == null) {
            imageImporter.importImagesTransaction(audioAsset.images.toSet())
            audioAsset.images.forEach { image ->
                syncStateStore.upsert(image.id, MediaAssetSyncState.Synced)
            }
            audioAssetStore.put(audioAsset)
        }

        val albumId = (audioAsset.uri as? MediaAssetUri.Album)?.albumId
        if (albumId != null && audioAsset.images.isNotEmpty()) {
            albumMediaStore.addImagesToAlbum(albumId, audioAsset.images.map { it.id }.toSet())
        }

        return Result.Success(existing ?: audioAsset)
    }
}
