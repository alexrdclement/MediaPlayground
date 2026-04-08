package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeSimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.Source
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
    private val mediaAssetImporter: Lazy<MediaAssetImporterImpl>,
    private val transactionRunner: MediaStoreTransactionRunner,
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val trackImporter: Lazy<TrackImporterImpl>,
) : AlbumImporter {

    override suspend fun import(
        uri: Uri,
        source: Source,
    ): Result<Album, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val metadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri) as? MediaMetadata.Audio
                ?: return@withContext Result.Failure(MediaImportError.InputFileError)

            // NOTE: SimpleAlbum is imported during asset import to find or create the directory

            val assetImportResult = mediaAssetImporter.value.importAudio(
                uri = uri,
                mediaMetadata = metadata,
                source = source,
            ).guardSuccess { return@withContext Result.Failure(it) }

            transactionRunner.run {
                importAlbum(
                    filePath = assetImportResult.filePath,
                    mediaMetadata = metadata,
                    mediaAsset = assetImportResult.mediaAsset,
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
        source: Source,
    ): Map<Uri, Result<Album, MediaImportError>> {
        return uris.associateWith {
            import(
                uri = it,
                source = source,
            )
        }
    }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importAlbum(
        filePath: Path,
        mediaMetadata: MediaMetadata.Audio,
        mediaAsset: MediaAsset,
        simpleAlbum: SimpleAlbum,
    ): Result<Album, MediaImportError> {
        trackImporter.value.importToAlbum(
            filePath = filePath,
            mediaMetadata = mediaMetadata,
            mediaAsset = mediaAsset,
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
        source: Source,
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
            source = source,
            artist = artist,
            images = images,
            getAlbumByTitleAndArtistId = albumMediaStore::getAlbumByTitleAndArtistId,
        )
        albumMediaStore.put(album)
        return Result.Success(album)
    }
}
