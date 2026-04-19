package com.alexrdclement.mediaplayground.data.track

import android.net.Uri
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackDataStore
import com.alexrdclement.mediaplayground.media.mediaimport.ImageImporter
import com.alexrdclement.mediaplayground.media.mediaimport.TrackImporter
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class TrackRepositoryImpl @Inject constructor(
    private val mediaImporter: ImageImporter,
    private val localTrackDataStore: LocalTrackDataStore,
    private val trackImporter: TrackImporter,
) : TrackRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var importJob: Job? = null

    private val trackImportProgress = MutableSharedFlow<Map<Uri, TrackImportState>>(extraBufferCapacity = 1)

    override fun getTrackFlow(id: TrackId): Flow<Track?> =
        localTrackDataStore.getTrackFlow(id)

    override suspend fun getTrack(id: TrackId): Track? =
        localTrackDataStore.getTrack(id)

    override fun getTrackCountFlow(): Flow<Int> =
        localTrackDataStore.getTrackCountFlow()

    override fun getTrackPagingData(config: PagingConfig): Flow<PagingData<Track>> =
        localTrackDataStore.getTrackPagingData(config)

    override fun getTrackImportProgress(): Flow<Map<Uri, TrackImportState>> {
        return trackImportProgress
    }

    override fun importTracksFromDisk(uris: List<Uri>): Flow<Map<Uri, TrackImportState>> {
        importJob?.cancel()
        importJob = coroutineScope.launch {
            trackImportProgress.emitAll(importTracksFromDiskFlow(uris = uris))
        }
        return trackImportProgress.map {
            it.filter { entry -> uris.contains(entry.key) }
        }
    }

    override suspend fun put(track: Track) =
        localTrackDataStore.put(track)

    override suspend fun updateTrackTitle(id: TrackId, title: String) =
        localTrackDataStore.updateTrackTitle(id, title)

    override suspend fun updateTrackNumber(id: TrackId, trackNumber: Int?) =
        localTrackDataStore.updateTrackNumber(id, trackNumber)

    override suspend fun updateTrackNotes(id: TrackId, notes: String?) =
        localTrackDataStore.updateTrackNotes(id, notes)

    override suspend fun delete(id: TrackId) =
        localTrackDataStore.delete(id = id)

    private fun importTracksFromDiskFlow(uris: List<Uri>): Flow<Map<Uri, TrackImportState>> {
        return channelFlow {
            val results = uris.associateWith<Uri, TrackImportState> {
                TrackImportState.InProgress
            }.toMutableMap()
            send(results.toMap())

            val completedResults = coroutineScope {
                importTracksFromDiskSuspend(uris = uris)
            }
            completedResults.forEach { (uri, result) ->
                results[uri] = TrackImportState.Completed(result)
            }
            send(results.toMap())
        }
    }

    private suspend fun importTracksFromDiskSuspend(uris: List<Uri>): Map<Uri, TrackImportResult> {
        return try {
            trackImporter.import(uris = uris).map { (uri, result) ->
                uri to mapMediaImportResult(result)
            }.toMap()
        } catch (e: Throwable) {
            yield()
            val result = TrackImportResult.Failure(TrackImportResult.Error.Unknown(throwable = e))
            uris.associateWith { result }
        }
    }

    private fun mapMediaImportResult(result: Result<AudioTrack, MediaImportError>): TrackImportResult {
        return when (result) {
            is Result.Failure -> {
                val error = TrackImportResult.Error.ImportError(result.failure)
                TrackImportResult.Failure(error)
            }
            is Result.Success -> {
                TrackImportResult.Success(result.value)
            }
        }
    }
}
