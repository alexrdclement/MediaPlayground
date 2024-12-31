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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalAudioRepositoryImpl @Inject constructor(
    private val pathProvider: PathProvider,
    private val mediaImporter: MediaImporter,
    private val localAudioDataStore: LocalAudioDataStore,
): LocalAudioRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var importJob: Job? = null

    override fun importTracksFromDisk(uris: List<Uri>) {
        importJob?.cancel()
        importJob = coroutineScope.launch {
            uris.forEach { uri ->
                importTrackFromDisk(uri)
            }
        }
    }

    override fun getTracksFlow(): Flow<List<Track>> {
        return localAudioDataStore.getTracksFlow()
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

    override fun getAlbumsFlow(): Flow<List<Album>> {
        return localAudioDataStore.getAlbumsFlow()
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

    private suspend fun importTrackFromDisk(uri: Uri) {
        val result = mediaImporter.importTrackFromDisk(
            uri = uri,
            fileWriteDir = pathProvider.trackImportFileWriteDir,
            getArtistByName = { artistName ->
                localAudioDataStore.getArtistByName(artistName)
            },
            getAlbumByTitleAndArtistId = { albumTitle, artistId ->
                localAudioDataStore.getAlbumByTitleAndArtistId(
                    albumTitle = albumTitle,
                    artistId = artistId,
                )
            },
        )
        when (result) {
            is Result.Failure -> onMediaImportFailure(result.failure)
            is Result.Success -> {
                localAudioDataStore.putTrack(result.value)
            }
        }
    }

    private fun onMediaImportFailure(error: MediaImportError) = when (error) {
        MediaImportError.MkdirError -> TODO("MkdirError")
        MediaImportError.InputFileError -> {
            TODO("InputFileError")
        }
        is MediaImportError.FileWriteError.InputFileNotFound -> TODO("InputFileNotFound")
        MediaImportError.FileWriteError.InputStreamError -> TODO("InputStreamError")
        is MediaImportError.FileWriteError.Unknown -> {
            // TODO
            error.throwable?.let { throw it }
        }
        is MediaImportError.Unknown -> {
            // TODO
            error.throwable?.let { throw it }
        }
    }
}
