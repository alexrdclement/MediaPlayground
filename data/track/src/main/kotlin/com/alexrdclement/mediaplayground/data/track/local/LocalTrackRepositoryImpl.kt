package com.alexrdclement.mediaplayground.data.track.local

import android.net.Uri
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.data.album.local.LocalAlbumRepository
import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistRepository
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporter
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.audio.Track
import com.alexrdclement.mediaplayground.media.model.audio.TrackId
import kotlinx.io.files.Path
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

class LocalTrackRepositoryImpl @Inject constructor(
    private val pathProvider: PathProvider,
    private val mediaImporter: MediaImporter,
    private val localTrackDataStore: LocalTrackDataStore,
    private val localAlbumRepository: LocalAlbumRepository,
    private val localArtistRepository: LocalArtistRepository,
) : LocalTrackRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var importJob: Job? = null

    private val trackImportProgress = MutableSharedFlow<Map<Uri, TrackImportState>>(extraBufferCapacity = 1)

    override fun importTracksFromDisk(uris: List<Uri>): Flow<Map<Uri, TrackImportState>> {
        importJob?.cancel()
        importJob = coroutineScope.launch {
            trackImportProgress.emitAll(importTracksFromDiskFlow(uris = uris))
        }
        return trackImportProgress.map {
            it.filter { entry -> uris.contains(entry.key) }
        }
    }

    override fun getTrackImportProgress(): Flow<Map<Uri, TrackImportState>> {
        return trackImportProgress
    }

    override fun getTrackFlow(id: TrackId): Flow<Track?> =
        localTrackDataStore.getTrackFlow(id)

    override suspend fun getTrack(id: TrackId): Track? =
        localTrackDataStore.getTrack(id)

    override fun getTrackCountFlow(): Flow<Int> =
        localTrackDataStore.getTrackCountFlow()

    override fun getTrackPagingData(config: PagingConfig): Flow<PagingData<Track>> =
        localTrackDataStore.getTrackPagingData(config)

    override suspend fun updateTrackTitle(id: TrackId, title: String) =
        localTrackDataStore.updateTrackTitle(id, title)

    override suspend fun updateTrackNumber(id: TrackId, trackNumber: Int?) =
        localTrackDataStore.updateTrackNumber(id, trackNumber)

    override suspend fun updateTrackNotes(id: TrackId, notes: String?) =
        localTrackDataStore.updateTrackNotes(id, notes)

    override suspend fun putTrack(track: Track) =
        localTrackDataStore.putTrack(track)

    override suspend fun deleteTrack(track: Track) =
        localTrackDataStore.deleteTrack(track)

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
            mediaImporter.importTracksFromDisk(
                uris = uris,
                getImportDir = { albumId -> pathProvider.getAlbumDir(albumId.value) },
                getImagePath = { imageId, extension -> Path(pathProvider.getImagesDir(), "${imageId.value}.$extension") },
                getArtistByName = { artistName ->
                    localArtistRepository.getArtistByName(artistName)
                },
                getAlbumByTitleAndArtistId = { albumTitle, artistId ->
                    localAlbumRepository.getAlbumByTitleAndArtistId(
                        albumTitle = albumTitle,
                        artistId = artistId,
                    )
                },
                saveTrack = { track ->
                    localTrackDataStore.putTrack(track)
                },
            ).map { (uri, result) ->
                uri to mapMediaImportResult(result)
            }.toMap()
        } catch (e: Throwable) {
            yield()
            val result = TrackImportResult.Failure(TrackImportResult.Error.Unknown(throwable = e))
            uris.associateWith { result }
        }
    }

    private fun mapMediaImportResult(result: Result<Track, MediaImportError>): TrackImportResult {
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
