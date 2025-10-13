package com.alexrdclement.mediaplayground.data.audio.local

import android.net.Uri
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporter
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
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
import javax.inject.Inject

class LocalAudioRepositoryImpl @Inject constructor(
    private val pathProvider: PathProvider,
    private val mediaImporter: MediaImporter,
    private val localAudioDataStore: LocalAudioDataStore,
): LocalAudioRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var importJob: Job? = null

    private val trackImportProgress = MutableSharedFlow<Map<Uri, MediaImportState>>(extraBufferCapacity = 1)

    override fun importTracksFromDisk(
        uris: List<Uri>,
    ): Flow<Map<Uri, MediaImportState>> {
        importJob?.cancel()
        importJob = coroutineScope.launch {
            trackImportProgress.emitAll(importTracksFromDiskFlow(uris = uris))
        }
        return trackImportProgress.map {
            it.filter { entry -> uris.contains(entry.key) }
        }
    }

    override fun getTrackImportProgress(): Flow<Map<Uri, MediaImportState>> {
        return trackImportProgress
    }

    override fun getTrackCountFlow(): Flow<Int> {
        return localAudioDataStore.getTrackCountFlow()
    }

    override fun getTrackPagingData(config: PagingConfig): Flow<PagingData<Track>> {
        return localAudioDataStore.getTrackPagingData(config)
    }

    override suspend fun getTrack(id: TrackId): Result<Track?, LocalAudioRepository.Failure> {
        val track = localAudioDataStore.getTrack(id)
        return if (track == null) {
            Result.Failure(LocalAudioRepository.Failure.TrackNotFound)
        } else {
            Result.Success(track)
        }
    }

    override fun getAlbumCountFlow(): Flow<Int> {
        return localAudioDataStore.getAlbumCountFlow()
    }

    override fun getAlbumPagingData(config: PagingConfig): Flow<PagingData<Album>> {
        return localAudioDataStore.getAlbumPagingData(config)
    }

    override suspend fun getAlbum(id: AlbumId): Result<Album?, LocalAudioRepository.Failure> {
        val album = localAudioDataStore.getAlbum(id)
        return if (album == null) {
            Result.Failure(LocalAudioRepository.Failure.AlbumNotFound)
        } else {
            Result.Success(album)
        }
    }

    private fun importTracksFromDiskFlow(
        uris: List<Uri>,
    ): Flow<Map<Uri, MediaImportState>> {
        return channelFlow {
            val results = uris.associateWith<Uri, MediaImportState> {
                MediaImportState.InProgress
            }.toMutableMap()
            send(results.toMap())

            coroutineScope {
                importTracksFromDiskSuspend(
                    uris = uris,
                )
            }
        }
    }

    private suspend fun importTracksFromDiskSuspend(
        uris: List<Uri>,
    ): Map<Uri, MediaImportResult> {
        return try {
            mediaImporter.importTracksFromDisk(
                uris = uris,
                getImportDir = { albumId -> pathProvider.getAlbumDir(albumId.value) },
                getArtistByName = { artistName ->
                    localAudioDataStore.getArtistByName(artistName)
                },
                getAlbumByTitleAndArtistId = { albumTitle, artistId ->
                    localAudioDataStore.getAlbumByTitleAndArtistId(
                        albumTitle = albumTitle,
                        artistId = artistId,
                    )
                },
                saveTrack = { track ->
                    localAudioDataStore.putTrack(track)
                },
            ).map { (uri, result) ->
                uri to mapMediaImportResult(result)
            }.toMap()
        } catch (e: Throwable) {
            yield()
            val result = MediaImportResult.Failure(MediaImportResult.Error.Unknown(throwable = e))
            uris.associateWith { result }
        }
    }

    private fun mapMediaImportResult(result: Result<Track, MediaImportError>): MediaImportResult {
        return when (result) {
            is Result.Failure -> {
                val error = MediaImportResult.Error.ImportError(result.failure)
                MediaImportResult.Failure(error)
            }
            is Result.Success -> {
                MediaImportResult.Success(result.value)
            }
        }
    }
}
