package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeTrack
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.SimpleTrack
import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackClip
import com.alexrdclement.mediaplayground.media.model.mapper.toSimpleTrack
import com.alexrdclement.mediaplayground.media.model.mapper.toTrack
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.media.store.TrackMediaStore
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import com.alexrdclement.mediaplayground.model.result.map
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path
import java.util.UUID

@Inject
class TrackImporterImpl(
    private val trackMediaStore: TrackMediaStore,
    private val mediaAssetImporter: Lazy<MediaAssetImporterImpl>,
    private val clipImporter: Lazy<ClipImporterImpl>,
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val transactionRunner: MediaStoreTransactionRunner,
) : TrackImporter {

    override suspend fun import(
        uri: Uri,
        source: Source,
    ): Result<Track, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val metadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri) as? MediaMetadata.Audio
                ?: return@withContext Result.Failure(MediaImportError.InputFileError)

            val assetImportResult = mediaAssetImporter.value.importAudio(
                uri = uri,
                mediaMetadata = metadata,
                source = source,
            ).guardSuccess { return@withContext Result.Failure(it) }

            transactionRunner.run {
                importToAlbum(
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
    ): Map<Uri, Result<Track, MediaImportError>> {
        // Album directory is only correct after a track has been saved. Import sequentially.
        return uris.associateWith {
            import(
                uri = it,
                source = source,
            )
        }
    }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importToAlbum(
        filePath: Path,
        mediaMetadata: MediaMetadata.Audio,
        mediaAsset: MediaAsset,
        simpleAlbum: SimpleAlbum,
    ): Result<Track, MediaImportError> {
        val clip = clipImporter.value.importTransaction(
            filePath = filePath,
            metadata = mediaMetadata,
            mediaAsset = mediaAsset,
        ).guardSuccess { return Result.Failure(it) }
        return importSimpleTrack(
            metadata = mediaMetadata,
            simpleAlbum = simpleAlbum,
            clips = setOf(clip),
        ).map { it.toTrack(simpleAlbum = simpleAlbum) }
    }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importSimpleTrack(
        metadata: MediaMetadata.Audio,
        simpleAlbum: SimpleAlbum,
        clips: Set<Clip>,
    ): Result<SimpleTrack, MediaImportError> {
        val trackClips = clips.map {
            TrackClip(clip = it, startFrameInTrack = 0L)
        }.toPersistentSet()
        val track = makeTrack(
            id = UUID.randomUUID(),
            trackClips = trackClips,
            mediaMetadata = metadata,
            artists = simpleAlbum.artists,
            simpleAlbum = simpleAlbum,
        )
        trackMediaStore.put(track)
        return Result.Success(track.toSimpleTrack())
    }
}
