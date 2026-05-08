package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeSimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.store.AlbumMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path

@Inject
class AlbumImporterImpl(
    private val albumMediaStore: AlbumMediaStore,
    private val audioAssetImporter: Lazy<AudioAssetImporter>,
    private val transactionRunner: MediaStoreTransactionRunner,
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val trackImporter: Lazy<TrackImporterImpl>,
) : AlbumImporter {

    override suspend fun import(
        uri: Uri,
    ): Result<AudioAlbum, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val metadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri) as? MediaMetadata.Audio
                ?: return@withContext Result.Failure(MediaImportError.InputFileError)

            // NOTE: SimpleAlbum is imported during asset import to find or create the directory

            val assetImportResult = audioAssetImporter.value.import(
                uri = uri,
                metadata = metadata,
            ).guardSuccess { return@withContext Result.Failure(it) }

            transactionRunner.run {
                importAlbum(
                    filePath = assetImportResult.filePath,
                    mediaMetadata = metadata,
                    audioAsset = assetImportResult.audioAsset,
                    simpleAlbum = assetImportResult.simpleAlbum,
                )
            }
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    override suspend fun import(
        uris: List<Uri>,
    ): Map<Uri, Result<AudioAlbum, MediaImportError>> =
        uris.associateWith { import(uri = it) }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importAlbum(
        filePath: Path,
        mediaMetadata: MediaMetadata.Audio,
        audioAsset: AudioAsset,
        simpleAlbum: SimpleAlbum,
    ): Result<AudioAlbum, MediaImportError> {
        trackImporter.value.importToAlbum(
            filePath = filePath,
            mediaMetadata = mediaMetadata,
            audioAsset = audioAsset,
            simpleAlbum = simpleAlbum,
        ).guardSuccess { return Result.Failure(it) }

        // Get Album with newly imported track
        return albumMediaStore.getAlbum(simpleAlbum.id)
            ?.let { Result.Success(it) }
            ?: Result.Failure(MediaImportError.Unknown(null))
    }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importSimpleAlbum(
        metadata: MediaMetadata.Audio,
        artist: Artist,
        images: PersistentSet<Image> = persistentSetOf(),
    ): Result<SimpleAlbum, MediaImportError> {
        val albumName = metadata.albumTitle ?: "Unknown album"
        val existing = albumMediaStore.getAlbumByTitleAndArtistId(
            albumTitle = albumName,
            artistId = artist.id,
        )
        if (existing != null) return Result.Success(existing)

        val album = makeSimpleAlbum(
            mediaMetadata = metadata,
            artist = artist,
            images = images,
            getAlbumByTitleAndArtistId = albumMediaStore::getAlbumByTitleAndArtistId,
        )
        albumMediaStore.put(album)
        return Result.Success(album)
    }
}
