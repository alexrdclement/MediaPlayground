package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeTrack
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.TimeUnit
import com.alexrdclement.mediaplayground.media.model.TrackClip
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.media.store.TrackMediaStore
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path
import java.util.UUID

@Inject
class TrackImporterImpl(
    private val trackMediaStore: TrackMediaStore,
    private val audioAssetImporter: Lazy<AudioAssetImporter>,
    private val clipImporter: Lazy<ClipImporterImpl>,
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val transactionRunner: MediaStoreTransactionRunner,
) : TrackImporter {

    override suspend fun import(
        uri: Uri,
    ): Result<AudioTrack, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val metadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri) as? MediaMetadata.Audio
                ?: return@withContext Result.Failure(MediaImportError.InputFileError)

            val assetImportResult = audioAssetImporter.value.import(
                uri = uri,
                metadata = metadata,
            ).guardSuccess { return@withContext Result.Failure(it) }

            transactionRunner.run {
                importToAlbum(
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
    ): Map<Uri, Result<AudioTrack, MediaImportError>> {
        // Album directory is only correct after a track has been saved. Import sequentially.
        return uris.associateWith { import(uri = it) }
    }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importToAlbum(
        filePath: Path,
        mediaMetadata: MediaMetadata.Audio,
        audioAsset: AudioAsset,
        simpleAlbum: SimpleAlbum,
    ): Result<AudioTrack, MediaImportError> {
        val clip = clipImporter.value.importTransaction(
            filePath = filePath,
            metadata = mediaMetadata,
            audioFile = audioAsset,
        ).guardSuccess { return Result.Failure(it) }
        return importSimpleAudioTrack(
            metadata = mediaMetadata,
            simpleAlbum = simpleAlbum,
            clips = setOf(clip),
        )
    }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importSimpleTrack(
        metadata: MediaMetadata.Audio,
        simpleAlbum: SimpleAlbum,
        clips: Set<Clip>,
    ): Result<AudioTrack, MediaImportError> = importSimpleAudioTrack(metadata, simpleAlbum, clips)

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importSimpleAudioTrack(
        metadata: MediaMetadata.Audio,
        simpleAlbum: SimpleAlbum,
        clips: Set<Clip>,
    ): Result<AudioTrack, MediaImportError> {
        val trackClips: PersistentSet<TrackClip<TimeUnit.Samples>> = clips.map { clip ->
            val sampleRate = (clip.assetOffset as TimeUnit.Samples).sampleRate
            TrackClip(
                clip = clip,
                trackOffset = TimeUnit.Samples(0L, sampleRate),
            )
        }.toPersistentSet()
        val track = makeTrack(
            id = UUID.randomUUID(),
            trackClips = trackClips,
            mediaMetadata = metadata,
            artists = simpleAlbum.artists,
            simpleAlbum = simpleAlbum,
        )
        trackMediaStore.put(track)
        return Result.Success(track)
    }
}
